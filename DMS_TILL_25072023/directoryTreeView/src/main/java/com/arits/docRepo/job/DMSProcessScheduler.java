/*
 File: FileProcesser.java
 Date 			    	Author 			Changes
 May 10, 2020 	       NTT DATA 		Created
 */
package com.arits.docRepo.job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.arits.docRepo.constants.ApplicationConstants;
import com.arits.docRepo.dto.TempFileDetails;
import com.arits.docRepo.repo.DMSSimulationProcRepository;
import com.arits.docRepo.repo.TempFileDetailsRepository;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import ooo.connector.BootstrapSocketConnector;



/**
 * Scheduler job to load enroll/unroll/void/override order based on incoming
 * file.
 *
 * @author NTT DATA
 * @version 1.0
 * @created May 10, 2020
 */
@Component
public class DMSProcessScheduler implements Job {
	/**
	 * Holds the value of logger
	 */
	private static final Logger logger = Logger.getLogger(DMSProcessScheduler.class.getName());

	/**
	 * Holds the value of env
	 */
	@Autowired
	private Environment env;
	
	boolean recreateIndexIfExists = true, threadStartStop = true;
	
	@Value("${server.ip.address}")
	private String systemIP ;
	
    String cachePdfFilePath = "", cacheTxtFilePath = "", INDEX_DIRECTORY = "", textFilePath = "", textFileName = "";
    public static final String FIELD_PATH = "path";
    public static final String FIELD_CONTENTS = "contents";

	

	@Autowired
	private DMSSimulationProcRepository dmsSimulationProcRepository;
	
	@Autowired
	private TempFileDetailsRepository tempFileDetailsRepository;

	/**
	 * Scheduler job to invoke enroll/unroll/void/override order based on incoming
	 * file.
	 *
	 * @author NTT DATA
	 * @version 1.0
	 * @created May 10, 2020
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("DMS Processor job : Heart Beat Message : ");		
		
		cachePdfFilePath = "//" + systemIP + "/Repo/4nxxc67t/";
        cacheTxtFilePath = "//" + systemIP + "/Repo/b2hGHv/";
        INDEX_DIRECTORY = "\\\\" + systemIP + "/Repo";
        imageProcessing();
        

	}

	private String convertTIFFToPDF(String toString, String string) {
        File tiffFile = new File(toString);
        File pdfFile = new File(string);
        try {
            RandomAccessFileOrArray myTiffFile = new RandomAccessFileOrArray(tiffFile.getCanonicalPath());
            // Find number of images in Tiff file
            int numberOfPages = TiffImage.getNumberOfPages(myTiffFile);
            com.itextpdf.text.Document TifftoPDF = new com.itextpdf.text.Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(TifftoPDF, new FileOutputStream(pdfFile));
            pdfWriter.setStrictImageSequence(true);
            TifftoPDF.open();
            Image tempImage;
            // Run a for loop to extract images from Tiff file
            // into a Image object and add to PDF recursively
            for (int i = 1; i <= numberOfPages; i++) {
                myTiffFile.seek(0);
                tempImage = TiffImage.getTiffImage(myTiffFile, i);
                Rectangle pageSize = new Rectangle(tempImage.getWidth(), tempImage.getHeight());
                TifftoPDF.setPageSize(pageSize);
                TifftoPDF.newPage();
                TifftoPDF.add(tempImage);
            }
            TifftoPDF.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return string;
    }

    private String fileConversionToPdfs(String myTemplate, String cachePath) {
    	System.out.println("myTemplate"+myTemplate);
        try {
            // Initialise
            String oooExeFolder = "C:\\Program Files\\LibreOffice\\program";

            XComponentContext xContext = BootstrapSocketConnector.bootstrap(oooExeFolder);
            // XComponentContext xContext = Bootstrap.bootstrap();

            XMultiComponentFactory xMCF = xContext.getServiceManager();

            Object oDesktop = xMCF.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xContext);

            XDesktop xDesktop = (XDesktop) UnoRuntime.queryInterface(
                    XDesktop.class,
                    oDesktop);

            // Load the Document
            if (!new File(myTemplate).canRead()) {
                throw new RuntimeException("Cannot load template:" + new File(myTemplate));

            }

            XComponentLoader xCompLoader = (XComponentLoader) UnoRuntime
                    .queryInterface(com.sun.star.frame.XComponentLoader.class,
                            xDesktop);

            String sUrl = "file:///" + myTemplate;

            PropertyValue[] propertyValues = new PropertyValue[0];

            propertyValues = new PropertyValue[1];
            propertyValues[0] = new PropertyValue();
            propertyValues[0].Name = "Hidden";
            propertyValues[0].Value = new Boolean(true);

            XComponent xComp = xCompLoader.loadComponentFromURL(
                    sUrl, "_blank", 0, propertyValues);

            // save as a PDF
            XStorable xStorable = (XStorable) UnoRuntime
                    .queryInterface(XStorable.class,
                            xComp);

            propertyValues = new PropertyValue[2];
            // Setting the flag for overwriting
            propertyValues[0] = new PropertyValue();
            propertyValues[0].Name = "Overwrite";
            propertyValues[0].Value = new Boolean(true);
            // Setting the filter name
            propertyValues[1] = new PropertyValue();
            propertyValues[1].Name = "FilterName";
            propertyValues[1].Value = "writer_pdf_Export";

            // Appending the favoured extension to the origin document name
//            myTemplate = cachePath.substring(0, cachePath.lastIndexOf(".")).concat(".pdf");
            xStorable.storeToURL("file:///" + cachePath, propertyValues);
            //\\"+systemIP+"/Repo/fileps/2167D
            // shutdown
            xDesktop.terminate();

        } catch (BootstrapException ex) {
            writeDMSProcess("3. Error in BootstrapException:" + ex.toString());
        } catch (com.sun.star.uno.Exception ex) {
            writeDMSProcess("4. Error in fileConversionToPdfs:" + ex.toString());
        }
        return cachePath;
    }

    private void writeDMSProcess(Object message) {
        String fname = System.getProperty("user.dir")
                + "arits_threat_"
                + new java.text.SimpleDateFormat("ddMMyyyy")
                .format(new Date()) + ".log";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fname, true));
            out.write('"'
                    + "Message Sent Time : "
                    + new java.text.SimpleDateFormat("h:mm:ss")
                    .format(new Date()) + '"' + "\r\n");
            out.write(message + "\r" + "\n" + '"' + "\n" + '"' + "\n");
            out.write("--------------------------------------------------------------------------------------------------------\n");
            out.close();
        } catch (java.lang.Exception e) {
            JOptionPane.showMessageDialog(null, "Log file fails : " + e.getMessage());
        }
    }

    private boolean isImage(File originalFilePath) {
        try {
            return ImageIO.read(originalFilePath) != null;
        } catch (java.lang.Exception e) {
            return false;
        }
    }

    private String getAlphaNumericString(int n) {
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb 
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private void createIndex(File file)  {
        Analyzer analyzer = new StandardAnalyzer();
        try {
        IndexWriter indexWriter = new IndexWriter(INDEX_DIRECTORY, analyzer, recreateIndexIfExists);
        Document document = new Document();
        String path = file.getCanonicalPath();
        document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.UN_TOKENIZED));
        Reader reader = new FileReader(file);
        document.add(new Field(FIELD_CONTENTS, reader));
        
			indexWriter.addDocument(document);
		
        indexWriter.optimize();
        indexWriter.close();
        if (recreateIndexIfExists) {
           
                String queryUpdateIndex = "INSERT INTO INDEX_DMS(RECREATE_INDEX_IF_EXISTS) VALUES(1)";
                dmsSimulationProcRepository.loadData(queryUpdateIndex);
                
            
            recreateIndexIfExists = false;
        }
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void convertPDFToText(String pdfFilePath, String desc) {
        try {
            //create file writer
            FileWriter fw = new FileWriter(desc);
            //create buffered writer
            BufferedWriter bw = new BufferedWriter(fw);
            //create pdf reader
            PdfReader pr = new PdfReader(pdfFilePath);
            //get the number of pages in the document
            int pNum = pr.getNumberOfPages();
            //extract text from each page and write it to the output text file
            for (int page = 1; page <= pNum; page++) {
                String text = PdfTextExtractor.getTextFromPage(pr, page);
                bw.write(text);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            writeDMSProcess("6. Error in convertPDFToText:" + ex.toString());
        }
    }

    private void imageProcessing() {
    	
        Iterable<TempFileDetails> tempFileDetailsList = new ArrayList<>();
      
        
            tempFileDetailsList = tempFileDetailsRepository.findAll();

           
            
        
            tempFileDetailsList.forEach(tempFileDetails ->{
            	 String getTempData = "", pdfFilePath = "", queryInsertFileDetails = "", immediateIndexing = "", deleteQuery = "", queryUpdateFileDetails = "";
                 
                Long idTempTable = tempFileDetails.getFileId();
                if (FilenameUtils.getExtension(tempFileDetails.getFileName()).equalsIgnoreCase("pdf")) {
                    //Case 1:Direct Pdf
                    pdfFilePath = "//" + systemIP + "/Repo/" + tempFileDetails.getEncryptedFileName();
                    textFileName = getAlphaNumericString(7);
                    textFilePath = cacheTxtFilePath + textFileName;
                    convertPDFToText(pdfFilePath, textFilePath);
                    if (tempFileDetails.getIndexing().trim().equalsIgnoreCase("YES")) {
                        createIndex(new File(textFilePath));
                    }
                } else if (FilenameUtils.getExtension(tempFileDetails.getFileName()).trim().equalsIgnoreCase("tif") || FilenameUtils.getExtension(tempFileDetails.getFileName()).trim().equalsIgnoreCase("tiff")) {
                    //Case 3:Convert TIFF file into pdf because Libre office not converted tiff file (only TIFF)
                    pdfFilePath = convertTIFFToPDF(tempFileDetails.getFilePath().substring(0, tempFileDetails.getFilePath().lastIndexOf(".")), cachePdfFilePath + tempFileDetails.getEncryptedFileName());
//                    textFileName = getAlphaNumericString(7);
//                    textFilePath = cacheTxtFilePath + textFileName;
//                    convertPDFToText(pdfFilePath, textFilePath);
                    textFilePath = tempFileDetails.getTextFile();
                    if (tempFileDetails.getIndexing().trim().equalsIgnoreCase("YES")) {
                        createIndex(new File(tempFileDetails.getTextFile()));
                    }
                } else if (FilenameUtils.getExtension(tempFileDetails.getFileName()).trim().equalsIgnoreCase("jpg") || FilenameUtils.getExtension(tempFileDetails.getFileName()).trim().equalsIgnoreCase("png") || FilenameUtils.getExtension(tempFileDetails.getFileName()).trim().equalsIgnoreCase("gif")) {
                    pdfFilePath = fileConversionToPdfs(tempFileDetails.getFilePath().substring(0, tempFileDetails.getFilePath().lastIndexOf(".")), cachePdfFilePath + tempFileDetails.getEncryptedFileName());
//                    pdfFilePath = rsFetchTempDataServer1.getString("PDF_FILE_PATH_TFD");
                    textFilePath = tempFileDetails.getTextFile();
                    createIndex(new File(tempFileDetails.getTextFile()));

                } else {
                    //Case 2 : Libre office conversion except image or pdf file (Exclusive for docs files)
                    pdfFilePath = fileConversionToPdfs(tempFileDetails.getFilePath().substring(0, tempFileDetails.getFilePath().lastIndexOf(".")), cachePdfFilePath + tempFileDetails.getEncryptedFileName());
                    textFileName = getAlphaNumericString(7);
                    textFilePath = cacheTxtFilePath + textFileName;
                    convertPDFToText(pdfFilePath, textFilePath);
                    if (tempFileDetails.getIndexing().trim().equalsIgnoreCase("YES")) {
                        createIndex(new File(textFilePath));
                    }
                }

                if (tempFileDetails.getIndexing().trim().equalsIgnoreCase("YES")) {
                    immediateIndexing = "Yes";
                } else {
                    immediateIndexing = "No";
                }

                String filePath = tempFileDetails.getFilePath();
                String userName = tempFileDetails.getUserName();
                long origFileId = tempFileDetails.getOrigFileId();
                int currentVersion = tempFileDetails.getCurrentVersion();
                int newVersion = currentVersion+1;
                String encryptedFileName = tempFileDetails.getEncryptedFileName();

                if (origFileId != 0 && currentVersion != 0) {
                    queryUpdateFileDetails = "UPDATE FILE_DETAILS SET FILE_PATH_FD='" + filePath + "',PDF_FILE_PATH_FD='" + pdfFilePath + "',ENCRYPTED_FILE_NAME_FD='"
                            + encryptedFileName + "',FILE_CURRENT_VERSION=" + newVersion + " where FILE_ID_FD=" + origFileId;
                    dmsSimulationProcRepository.loadData(queryUpdateFileDetails);
                    
                    deleteQuery = "DELETE FROM TEMP_FILE_DETAILS WHERE ID_TEMP_TFG = '" + idTempTable + "'";
                    dmsSimulationProcRepository.loadData(deleteQuery);
                    String versionQuery = "INSERT INTO file_versions(user_name,file_id,file_version,file_path,comment,created_at)"
                            + "VALUES('" + userName + "'," + origFileId + ",'" + newVersion  + "','" + filePath + "','" +
                            "File Checked In." + "','" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                            .format(Calendar.getInstance().getTime()) + "')";
                    dmsSimulationProcRepository.loadData(versionQuery);
                    
                } else {
                	
                    queryInsertFileDetails = "INSERT INTO FILE_DETAILS(FILE_NAME_FD, FILE_PATH_FD, INDEXING_FD, VERSION_COMMENT_FD, LANGUAGE_FD, TAG_FD, NEW_TAG_FD, CUSTOM_ID_FD, "
                            + "COVERAGE_FD, OBJECT_FD, RECEIPIENT_FD, SOURCE_FD, AUTHOR_FD, ORIGINAL_ID_FD, TYPE_FD, USERS_FD, MESSAGES_FD, FILE_INDEX_FD, FILE_LOCK_FD, FILE_BOOKMARK_FD, "
                            + "FOLDER_PATH_FD, RATING_FD, CREATED_ON_FD, FILE_SIZE_FD, FILE_EXTENSION_FD, FILE_CURRENT_STATUS_FD, USER_NAME_FD, DATE_UPLOADED_FD, TREE_PATH_NAME, "
                            + "PDF_FILE_PATH_FD, ENCRYPTED_FILE_NAME_FD, CHECK_IN_OUT_STATUS_FD, TEXT_FILE_NAME_FD, FOLDER_ID_FD, USER_GROUP_FD) "
                            + "VALUES('" + tempFileDetails.getFileName() + "','" + filePath + "'"
                            + ",'" + immediateIndexing + "','" + tempFileDetails.getVersionComment() + "','" + tempFileDetails.getLanguage() + "','" + tempFileDetails.getTag() + "','" + tempFileDetails.getNewTag() + "'"
                            + ",'" + tempFileDetails.getCustId() + "','" + tempFileDetails.getCoverage() + "','" + tempFileDetails.getObject() + "','" + tempFileDetails.getReceipient() + "','" + tempFileDetails.getSource() + "'"
                            + ",'" + tempFileDetails.getAuthor() + "','" + tempFileDetails.getOriginalId() + "','" + tempFileDetails.getType() + "','" + tempFileDetails.getUsersTFD() + "','" + tempFileDetails.getMessages() + "',"
                            + "'" + tempFileDetails.getFileIndex() + "','" + tempFileDetails.getFileLock() + "','" + tempFileDetails.getFileBookmark() + "',"
                            + "'" + tempFileDetails.getFolderPath()  + "','" + tempFileDetails.getRating() + "','" + tempFileDetails.getCreationDate() + "','" + tempFileDetails.getFileSize() + "', "
                            + "'" + tempFileDetails.getFileExtension() + "','" + tempFileDetails.getFileCurrentStatus() + "','" + userName + "','" + tempFileDetails.getDateUploaded() + "','" + tempFileDetails.getTreePathName() + "', "
                            + "'" + pdfFilePath + "','" + tempFileDetails.getEncryptedFileName() + "','" + tempFileDetails.getCheckInOut() + "', "
                            + "'" + textFilePath + "', " + tempFileDetails.getFolderId() + ",'" + tempFileDetails.getUserGroup() + "');";
	                    int idValue = dmsSimulationProcRepository.insertMessage(queryInsertFileDetails);
	                    
	                    deleteQuery = "DELETE FROM TEMP_FILE_DETAILS WHERE ID_TEMP_TFG = '" + idTempTable + "'";
	                     dmsSimulationProcRepository.loadData(deleteQuery);
	                    
	                        String versionQuery = "INSERT INTO file_versions(user_name,file_id,file_version,file_path,comment,created_at)"
	                                + "VALUES('" + userName + "'," + idValue + ",'" + 1 + "','" + filePath + "','" +
	                                "New File Created." + "','" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
	                                .format(Calendar.getInstance().getTime()) + "')";
	                        dmsSimulationProcRepository.loadData(versionQuery);
                }
            
            });
           
        
    }

	
}
