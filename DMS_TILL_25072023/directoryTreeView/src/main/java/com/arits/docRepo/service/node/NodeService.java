package com.arits.docRepo.service.node;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.dto.FolderDetails;
import com.arits.docRepo.dto.TempFileDetails;
import com.arits.docRepo.model.AddFolderRequest;
import com.arits.docRepo.model.FileOperation;
import com.arits.docRepo.model.Node;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.repo.*;
import com.arits.docRepo.service.versioning.FileVersionService;
import com.arits.docRepo.util.EventDetailsUtil;
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

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import ooo.connector.BootstrapSocketConnector;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;


@Service
public class NodeService {

    // Map<Long, Node> nodesMap;
    Node rootNode;
    List<Node> nodeList = new ArrayList<>();

    List<Node> updateNodeList = new ArrayList<>();

    @Autowired
    private EventDetailsUtil eventDetailsUtil;
    
    @Autowired
    private FolderDetailsRepository folderDetailsReposiory;

    @Autowired
    private FileVersionService fileVersionService;

    @Autowired
    private FileDetailsRepository fileDetailsRepository;

    @Autowired
    private FileExtensionRepository fileExtensionRepository;

    @Autowired
    private TempFileDetailsRepository tempFileDetailsRepository;

    @Autowired
    private LoginRegistrationRepository loginRegistrationRepository;

    @Autowired
    private Environment env;

    private boolean recreateIndexIfExists = true;
	private boolean threadStartStop = true;

//	@Value("${server.ip.address}")
	
	private String systemIP = "192.168.1.150";

	private String textFilePath = "";
	private String textFileName = "";

	private String cachePdfFilePath = "//" + systemIP + "/Repo/4nxxc67t/";
	private String cacheTxtFilePath = "//" + systemIP + "/Repo/b2hGHv/";
	private String INDEX_DIRECTORY = "\\\\" + systemIP + "/Repo";

	public static final String FIELD_PATH = "path";
	public static final String FIELD_CONTENTS = "contents";

	@Autowired
	private DMSSimulationProcRepository dmsSimulationProcRepository;


    
    public List<Node> getAllNodeDetails(UserDetails userDetails) {
    	System.out.println("getAllNodes");
        Map<Long, Node> nodesMap = new HashMap<Long, Node>();
        List<String> userGroupList = new ArrayList<String>();
        List<FolderDetails> folderDetailsList = null;
        userGroupList.add(userDetails.getUserGroup());
        userGroupList.add("ALL");
        if (userDetails.getUserName().equals("Admin")) {
            folderDetailsList = folderDetailsReposiory.findByFolderCurrentStatusEqualsOrderByFolderNameAsc("ACTIVE");
        } else {
            folderDetailsList = folderDetailsReposiory
                    .findByUserGroupInAndFolderCurrentStatusEqualsOrderByFolderNameAsc(userGroupList, "ACTIVE");
        }

        List<Node> nodes = new ArrayList<>();
        folderDetailsList.forEach(folderDetails -> {
            if (folderDetails.getParentId() == 0) {
                rootNode = new Node(folderDetails.getFolderId().toString(), folderDetails.getParentId().toString(),
                        folderDetails.getFolderName());
            }
            nodesMap.put(folderDetails.getFolderId(), new Node(folderDetails.getFolderId().toString(),
                    folderDetails.getParentId().toString(), folderDetails.getFolderName()));
            nodes.add(new Node(folderDetails.getFolderId().toString(), folderDetails.getParentId().toString(),
                    folderDetails.getFolderName()));
        });
        nodeList.clear();
        buildHierarchy(rootNode, nodesMap);
        printNodes(rootNode, 1);

        return nodeList;
    }

    /**
     * printNodes
     *
     * @param rootNode2
     * @param nodesMap
     * @return void
     */
    private void printNodes(Node rootNode2, int tabLevel) {
        nodeList.add(rootNode2);
        List<Node> subordinates = rootNode2.getSubNodes();
        if (subordinates.size() > 0) {
            for (Node e : subordinates) {
                printNodes(e, tabLevel + 1);
            }
        }

    }

    /**
     * buildHierarchy
     *
     * @param rootNode
     * @param nodesMap
     * @return void
     */
    private void buildHierarchy(Node node, Map<Long, Node> nodesMap) {
        List<Node> listNode = findAllNodesByParentId(Long.parseLong(node.getNodeId()), nodesMap);
        Collections.sort(listNode);
        node.setSubNodes(listNode);
        if (listNode.size() == 0) {
            return;
        }

        for (Node e : listNode) {
            buildHierarchy(e, nodesMap);
        }
    }

    public List<Node> findAllNodesByParentId(Long nodeId, Map<Long, Node> nodesMap) {
        List<Node> nodeList = new ArrayList<Node>();
        for (Node e : nodesMap.values()) {
            if (Long.parseLong(e.getPid()) == nodeId) {
                nodeList.add(e);
            }
        }
        return nodeList;
    }

    public void addNewFolder(AddFolderRequest addFolderRequest, UserDetails userDetails) {

    	try {
        List<FolderDetails> folderDetailsList = folderDetailsReposiory
                .findByFolderIdEquals(Long.parseLong(addFolderRequest.getParentId()));
        FolderDetails folderDetails = new FolderDetails();

        folderDetailsList.forEach(folderDet -> {
            folderDetails.setFolderName(addFolderRequest.getFolderName());
            folderDetails.setParentId(Long.parseLong(addFolderRequest.getParentId()));
            folderDetails.setCreationDate(LocalDateTime.now());
            folderDetails.setFolderCurrentStatus("ACTIVE");
            folderDetails.setUserGroup(userDetails.getUserGroup());
            folderDetails.setUsername(userDetails.getUserName());
            folderDetails.setFolderDescription("");
            String folderPath = folderDet.getFolderPath();
            String[] folderPathArray = folderPath.split("]");
            folderDetails.setFolderPath(folderPathArray[0] + ", " + addFolderRequest.getFolderName() + "]");
        });

        folderDetailsReposiory.save(folderDetails);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    }

    public String fileUpload(MultipartFile[] files, String nodeId, UserDetails userDetails) {
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                TempFileDetails tempFileDetails = new TempFileDetails();
                String extension = null;
                String fileName = file.getOriginalFilename();

                String[] fileExtenstion = fileName.split(".");

                int index = fileName.lastIndexOf('.');
                if (index > 0) {
                    extension = fileName.substring(index + 1);
                }

                String encryptedFileName = getEncryptedFileName();
                String filePathWithExt = env.getProperty("file.base.directory") + encryptedFileName + "." + extension;
                String filePath = env.getProperty("file.base.directory") + encryptedFileName;
                InputStream is = file.getInputStream();
                FileOutputStream os = new FileOutputStream(filePath);

                int b = 0;
                while ((b = is.read()) != -1) {
                    os.write(b);
                }
                os.close();
                Path file1 = Paths.get(filePath);

                String folderPath = env.getProperty("file.base.directory") + encryptedFileName;
                BasicFileAttributes attr = Files.readAttributes(file1, BasicFileAttributes.class);
                FileTime creationDate = attr.creationTime();
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss");
                String dateCreated = df.format(creationDate.toMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date();
                String currDate = formatter.format(currentDate);

                if (fileName.toLowerCase().contains(".png") || fileName.toLowerCase().contains(".gif")
                        || fileName.toLowerCase().contains(".tif") || fileName.toLowerCase().contains(".jpg")) {
                    performOCR(fileName, encryptedFileName);
                    tempFileDetails.setTextFile(env.getProperty("text.file.base.directory") + encryptedFileName);
                }

                List<FolderDetails> folderDetailsList = folderDetailsReposiory
                        .findByFolderIdEquals(Long.parseLong(nodeId));
                String[] treePathName = {""};
                folderDetailsList.forEach(folderDet -> {
                    treePathName[0] = folderDet.getFolderPath();
                });

                tempFileDetails.setFileName(fileName);
                tempFileDetails.setFilePath(filePathWithExt);
                tempFileDetails.setFolderPath(folderPath);
                tempFileDetails.setCreationDate(dateCreated);
                tempFileDetails.setFileSize(String.valueOf(file.getSize()));
                tempFileDetails.setFileExtension(extension);

                tempFileDetails.setFileCurrentStatus("ACTIVE");
                tempFileDetails.setDateUploaded(currDate);
                tempFileDetails.setUserName(userDetails.getUserName());
                tempFileDetails.setAuthor(userDetails.getUserName());
                tempFileDetails.setTreePathName(treePathName[0]);
                tempFileDetails.setEncryptedFileName(encryptedFileName);

                tempFileDetails.setFolderId(Long.parseLong(nodeId));
                tempFileDetails.setUserGroup(userDetails.getUserGroup());
                tempFileDetails.setIndexing("Yes");
                tempFileDetails.setLanguage("English");
                tempFileDetails.setTag("Enter Values");
                tempFileDetails.setUsersTFD("English");
                tempFileDetails.setFileLock("NO");
                
                tempFileDetailsRepository.save(tempFileDetails);
                eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to upload the file : " + fileName);
                
                System.out.println("Before image processing");
                imageProcessing(tempFileDetails);
                System.out.println("After image processing");
                
            } catch (Exception exception) {
                exception.printStackTrace();

            }

        }

        return "Success";

    }

    public String mergeFile(MultipartFile[] files, String nodeId, UserDetails userDetails, String fileId) {
        FileDetails currentFileDetails = null;
        String mergeFileName = getEncryptedFileName();
        String originalFileName = null;
        boolean isMergeCompleted = false;
        String currentFileName = null;
        List<FileDetails> fileDetailsList = fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileId));
        if (fileDetailsList.size() > 0) {
            currentFileDetails = fileDetailsList.get(0);
            originalFileName = currentFileDetails.getFileName();
            currentFileName = currentFileDetails.getEncryptedFileName();
            isMergeCompleted = performMerge(currentFileName, files, mergeFileName);
        }
        try {
            if (isMergeCompleted) {
                currentFileDetails.setFilePath(env.getProperty("file.base.directory") + mergeFileName + ".pdf");
                currentFileDetails.setPdfFilePath(env.getProperty("file.base.directory") + mergeFileName);
                currentFileDetails.setCurrentVersion(String.valueOf(Integer.valueOf(currentFileDetails.getCurrentVersion()) + 1));
                currentFileDetails.setEncryptedFileName(mergeFileName);
                fileDetailsRepository.save(currentFileDetails);
                fileVersionService.addVersionDetailsForPdf(currentFileDetails, "File Merged.", userDetails.getUserName());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return "Error occurred";
        }

        return "Success";

    }

    /**
     * performMerge
     *
     * @param currentFileName
     * @param files
     * @param newFileName
     * @return void
     */
    private boolean performMerge(String currentFileName, MultipartFile[] files, String newFileName) {
        String filePath = env.getProperty("file.base.directory") + currentFileName;
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        PDFmerger.setDestinationFileName(env.getProperty("file.base.directory") + "temp\\Merge_" + newFileName);
        File currentFile = new File(filePath);
        List<String> filePathList = new ArrayList<String>();
        filePathList.add(filePath);
        try {
            PDFmerger.addSource(currentFile);
            for (int i = 0; i < files.length; i++) {
                String fileName = getEncryptedFileName();
                String tempFilePath = env.getProperty("file.base.directory") + "temp\\" + fileName;
                MultipartFile file = files[i];
                InputStream is = file.getInputStream();
                FileOutputStream os = new FileOutputStream(tempFilePath);
                int b = 0;
                while ((b = is.read()) != -1) {
                    os.write(b);
                }
                os.close();
                File newFile = new File(tempFilePath);
                PDFmerger.addSource(newFile);
                filePathList.add(tempFilePath);
            }
            PDFmerger.mergeDocuments();
            File currentMergedFile = new File(
                    env.getProperty("file.base.directory") + "temp\\" + "Merge_" + newFileName);
            File newFile = new File(env.getProperty("file.base.directory") + newFileName);
            FileUtils.copyFile(currentMergedFile, newFile);

            filePathList.forEach(deleteFilePath -> {
                File deleteFile = new File(deleteFilePath);
                try {
                    FileUtils.forceDelete(deleteFile);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            });
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private String getEncryptedFileName() {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public String deleteFolder(String folderId) {
        List<FolderDetails> folderDetailsList = folderDetailsReposiory.findByFolderIdEquals(Long.parseLong(folderId));

        if (folderDetailsList.size() > 0) {

            FolderDetails folderDetails = folderDetailsList.get(0);
            if (folderDetails.getParentId() == 0) {
                return "Defult Node can't be deleted";
            }
            folderDetails.setFolderCurrentStatus("DELETED");
            folderDetailsReposiory.save(folderDetails);


            List<FileDetails> fileDetailsList = fileDetailsRepository
                    .findByFolderIdEqualsOrderByFileIdAsc(Long.parseLong(folderId));
            /*
             * if (fileDetailsList.size() > 0) { FileDetails fileDetails =
             * fileDetailsList.get(0); fileDetails.setFileCurrentStatus("DELETED");
             * fileDetailsRepository.save(fileDetails);
             *
             * }
             */

            fileDetailsList.forEach(fileDetails -> {
                fileDetails.setFileCurrentStatus("DELETED");
                fileDetailsRepository.save(fileDetails);
            });

            return "Folder Deleted";
        }
        return "An Error occured";

    }

    public String updateFolder(String folderId, String folderName) {
        Node updateNode = null;
        String currentFolderPath = null;
        String newFolderPath = null;
        String currentFolderName = null;
        List<FolderDetails> folderDetailsList1 = folderDetailsReposiory.findByFolderIdEquals(Long.parseLong(folderId));
        if (folderDetailsList1.size() > 0) {
            FolderDetails folderDetails = folderDetailsList1.get(0);
            updateNode = new Node(folderDetails.getFolderId().toString(), folderDetails.getParentId().toString(),
                    folderDetails.getFolderName());
            if (folderDetails.getParentId() == 0) {
                return "Defult Node can't be edited";
            }
            currentFolderPath = folderDetails.getFolderPath();
            currentFolderName = folderDetails.getFolderName();
            folderDetails.setFolderName(folderName);
            newFolderPath = folderDetails.getFolderPath().replace(currentFolderName, folderName);
            folderDetails.setFolderPath(newFolderPath);
            folderDetailsReposiory.save(folderDetails);

        }

        List<FolderDetails> folderDetailsList = null;
        folderDetailsList = folderDetailsReposiory.findByFolderCurrentStatusEqualsOrderByFolderIdAsc("ACTIVE");
        Map<Long, Node> updateNodesMap = new HashMap<Long, Node>();
        List<Node> nodes = null;
        folderDetailsList.forEach(folderDetails -> {
            if (folderDetails.getParentId() == 0) {
                rootNode = new Node(folderDetails.getFolderId().toString(), folderDetails.getParentId().toString(),
                        folderDetails.getFolderName());
            }
            updateNodesMap.put(folderDetails.getFolderId(), new Node(folderDetails.getFolderId().toString(),
                    folderDetails.getParentId().toString(), folderDetails.getFolderName()));

        });
        updateNodeList.clear();
        buildHierarchy(updateNode, updateNodesMap);
        printSubNodes(updateNode);
        updateSubFolder(folderDetailsList, updateNodeList, folderId, updateNode.getText(), folderName,
                currentFolderPath, newFolderPath);
        return "Folder name got updated";

    }

    /**
     * updateSubFolder
     *
     * @param folderDetailsList
     * @param updateNodeList2
     * @param newFolderName
     * @param currentFoldeName
     * @param newFolderName
     * @param newFolderPath
     * @param currentFolderPath2
     * @return void
     */
    private void updateSubFolder(List<FolderDetails> folderDetailsList, List<Node> updateNodList,
                                 String currentFolderId, String currentFoldeName, String newFolderName, String currentFolderPath,
                                 String newFolderPath) {
        folderDetailsList.forEach(folderDetails -> {
            updateNodList.forEach(node -> {
                if (folderDetails.getFolderId() == Long.parseLong(node.getNodeId())) {
                    if (folderDetails.getFolderId() != Long.parseLong(currentFolderId)) {
                        String currentFolderPath1 = currentFolderPath.replace("]", "");
                        String newFolderPath1 = newFolderPath.replace("]", "");
                        folderDetails.setFolderPath(
                                folderDetails.getFolderPath().replace(currentFolderPath1, newFolderPath1));
                        folderDetailsReposiory.save(folderDetails);
                    }
                }
            });

        });

    }

    private void printSubNodes(Node rootNode2) {
        updateNodeList.add(rootNode2);
        List<Node> subordinates = rootNode2.getSubNodes();
        if (subordinates.size() > 0) {
            for (Node e : subordinates) {
                printSubNodes(e);
            }
        }

    }

    private void createIndex() {
        File file = new File("C:\\Repo\\DocumentCare.txt");
        String INDEX_DIRECTORY = "C:\\Repo\\";
        String FIELD_CONTENTS = "contents";
        String FIELD_PATH = "path";
        Analyzer analyzer = new StandardAnalyzer();
        try {
            IndexWriter indexWriter = new IndexWriter(INDEX_DIRECTORY, analyzer, true);
            Document document = new Document();
            String path = file.getCanonicalPath();
            document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.UN_TOKENIZED));
            Reader reader = new FileReader(file);
            document.add(new Field(FIELD_CONTENTS, reader));
            indexWriter.addDocument(document);
            indexWriter.optimize();
            indexWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public List<FileDetails> filterByFileDate(java.sql.Date startDate, java.sql.Date endDate, List<FileDetails> fileDetailsList) {
        return fileDetailsList.stream().filter(fileDetails -> {
            java.sql.Date date = fileDetails.getCreatedAt();
            return (date.equals(startDate) || date.equals(endDate))
                    || (date.before(endDate) && date.after(startDate));
        }).collect(Collectors.toList());
    }

    public List<FileDetails> filterByCreationDate(java.sql.Date startDate, java.sql.Date endDate, List<FileDetails> fileDetailsList) {
        return fileDetailsList.stream().filter(fileDetails -> {
            java.sql.Date date = parseCreationDate(fileDetails.getCreatedOn());
            return (date.equals(startDate) || date.equals(endDate))
                    || (date.before(endDate) && date.after(startDate));
        }).collect(Collectors.toList());
    }

    private java.sql.Date parseCreationDate(String s) {
        if (s == null) {
            throw new java.lang.IllegalArgumentException();
        }
        final int YEAR_LENGTH = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int MAX_DAY = 31;
        java.sql.Date d = null;
        int firstDash = s.indexOf('/');
        int secondDash = s.indexOf('/', firstDash + 1);
        int len = s.length();

        if ((firstDash > 0) && (secondDash > 0) && (secondDash < len - 1)) {
            if (firstDash == MONTH_LENGTH && (secondDash - firstDash > 1 && secondDash - firstDash <= MONTH_LENGTH + 1)) {
            	
                int month = Integer.parseInt(s, 0, firstDash, 10);
            	
                int day = Integer.parseInt(s, firstDash + 1, secondDash, 10);
                int year = Integer.parseInt(s, secondDash + 1, secondDash + 5, 10);

                if ((month >= 1 && month <= MAX_MONTH) && (day >= 1 && day <= MAX_DAY)) {
                    d = new java.sql.Date(year - 1900, month - 1, day);
                }
            }
        }
        if (d == null) {
            throw new java.lang.IllegalArgumentException();
        }

        return d;
    }

    public List<FileDetails> searchIndex(String searchString, String fileSearchParam, String fileName, String folderPathName,
                                         boolean isWholeWord, UserDetails userDetails) {
        // createIndex();
        List<FileDetails> responseFileList = new ArrayList<>();
        List<FileDetails> responseList = new ArrayList<>();
        List<FileDetails> fileDetailsByContentList = null;
        List<FileDetails> fileDetailsByNameList = null;
        List<FileDetails> fileDetailsByParam1List = null;
        List<FileDetails> fileDetailsByParam2List = null;
        List<FileDetails> fileDetailsByParam3List = null;

        List<String> userGroupList = new ArrayList<String>();

        userGroupList.add(userDetails.getUserGroup());
        userGroupList.add("ALL");

        try {

            // File Content
            if (searchString != null && !searchString.equals("")) {
                String INDEX_DIRECTORY = "C:\\Repo\\";
                String FIELD_CONTENTS = "contents";
                String FIELD_PATH = "path";
                Directory directory = FSDirectory.getDirectory(INDEX_DIRECTORY);
                IndexReader indexReader = IndexReader.open(directory);
                IndexSearcher indexSearcher = new IndexSearcher(indexReader);

                Analyzer analyzer = new StandardAnalyzer();
                QueryParser queryParser = new QueryParser(FIELD_CONTENTS, analyzer);
                Query query = queryParser.parse(searchString);
                Hits hits = indexSearcher.search(query);

                Iterator<Hit> it = hits.iterator();

                while (it.hasNext()) {
                    Hit hit = it.next();
                    Document document = hit.getDocument();
                    String path = document.get(FIELD_PATH);
                    String path1 = path.replaceAll("\\\\", "/");
                    String[] pathArray = path1.split("/");
                    String searchParam = pathArray[(pathArray.length) - 1];
                    System.out.println("search param " + path);

                    if (userDetails.getUserName().equals("Admin")) {
                        fileDetailsByContentList = fileDetailsRepository
                                .findByTextFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(searchParam,
                                        "ACTIVE");
                    } else {
                        fileDetailsByContentList = fileDetailsRepository
                                .findByUserGroupInAndTextFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, searchParam,
                                        "ACTIVE");
                    }


                    fileDetailsByContentList.forEach(fileDetails -> {
                        responseFileList.add(fileDetails);
                    });
                }

            }

            // File Name
            if (fileName != null && !fileName.equals("")) {
                if (isWholeWord) {
                    if (userDetails.getUserName().equals("Admin")) {
                        fileDetailsByNameList = fileDetailsRepository
                                .findByFileNameEqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileName, "ACTIVE");
                    } else {
                        fileDetailsByNameList = fileDetailsRepository
                                .findByUserGroupInAndFileNameEqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileName, "ACTIVE");
                    }


                } else {

                    if (userDetails.getUserName().equals("Admin")) {
                        fileDetailsByNameList = fileDetailsRepository
                                .findByFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileName, "ACTIVE");
                    } else {
                        fileDetailsByNameList = fileDetailsRepository
                                .findByUserGroupInAndFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileName, "ACTIVE");
                    }


                }

                fileDetailsByNameList.forEach(fileDetails -> {
                    boolean[] duplicateId = new boolean[1];
                    duplicateId[0] = false;
                    responseFileList.forEach(fileD -> {
                        if (fileD.getFileId().equals(fileDetails.getFileId())) {
                            duplicateId[0] = true;
                        }
                    });
                    if (!duplicateId[0]) {
                        responseFileList.add(fileDetails);
                    }
                });

            }

            if (fileSearchParam != null && fileSearchParam != "") {
                if (isWholeWord) {

                    if (userDetails.getUserName().equals("Admin")) {
                        fileDetailsByParam1List = fileDetailsRepository
                                .findByFileSearch1EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam2List = fileDetailsRepository
                                .findByFileSearch2EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam3List = fileDetailsRepository
                                .findByFileSearch3EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileSearchParam,
                                        "ACTIVE");
                    } else {
                        fileDetailsByParam1List = fileDetailsRepository
                                .findByUserGroupInAndFileSearch1EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam2List = fileDetailsRepository
                                .findByUserGroupInAndFileSearch2EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam3List = fileDetailsRepository
                                .findByUserGroupInAndFileSearch3EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileSearchParam,
                                        "ACTIVE");
                    }


                } else {

                    if (userDetails.getUserName().equals("Admin")) {
                        fileDetailsByParam1List = fileDetailsRepository
                                .findByFileSearch1ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam2List = fileDetailsRepository
                                .findByFileSearch2ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam3List = fileDetailsRepository
                                .findByFileSearch3ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(fileSearchParam,
                                        "ACTIVE");
                    } else {
                        fileDetailsByParam1List = fileDetailsRepository
                                .findByUserGroupInAndFileSearch1ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam2List = fileDetailsRepository
                                .findByUserGroupInAndFileSearch2ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileSearchParam,
                                        "ACTIVE");
                        fileDetailsByParam3List = fileDetailsRepository
                                .findByUserGroupInAndFileSearch3ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(userGroupList, fileSearchParam,
                                        "ACTIVE");
                    }


                }

                fileDetailsByParam1List.forEach(fileDetails -> {
                    boolean[] duplicateId = new boolean[1];
                    duplicateId[0] = false;
                    responseFileList.forEach(fileD -> {
                        if (fileD.getFileId().equals(fileDetails.getFileId())) {
                            duplicateId[0] = true;
                        }
                    });
                    if (!duplicateId[0]) {
                        responseFileList.add(fileDetails);
                    }
                });

                fileDetailsByParam2List.forEach(fileDetails -> {
                    boolean[] duplicateId = new boolean[1];
                    duplicateId[0] = false;
                    responseFileList.forEach(fileD -> {
                        if (fileD.getFileId().equals(fileDetails.getFileId())) {
                            duplicateId[0] = true;
                        }
                    });
                    if (!duplicateId[0]) {
                        responseFileList.add(fileDetails);
                    }
                });

                fileDetailsByParam3List.forEach(fileDetails -> {
                    boolean[] duplicateId = new boolean[1];
                    duplicateId[0] = false;
                    responseFileList.forEach(fileD -> {
                        if (fileD.getFileId().equals(fileDetails.getFileId())) {
                            duplicateId[0] = true;
                        }
                    });
                    if (!duplicateId[0]) {
                        responseFileList.add(fileDetails);
                    }
                });
            }
            if (folderPathName != null && !folderPathName.equals("")) {
                List<FolderDetails> folderDetails;
                if (userDetails.getUserName().equals("Admin")) {

                    folderDetails = folderDetailsReposiory.findByFolderPathContainsAndFolderCurrentStatusEqualsOrderByFolderNameAsc
                            (folderPathName, "ACTIVE");
                } else {
                    folderDetails = folderDetailsReposiory.findByFolderPathContainsAndUserGroupInAndFolderCurrentStatusEqualsOrderByFolderNameAsc
                            (folderPathName, userGroupList, "ACTIVE");
                }

                if (folderDetails == null) {
                    folderDetails = Collections.emptyList();
                }

                List<FileDetails> allFilesInFolder = new ArrayList<>();
                folderDetails.stream().forEach(folder -> {
                    allFilesInFolder.addAll(getAllFilesByFolder(folder.getFolderId()));
                });

                List<Long> fileIds = new ArrayList<>();
                allFilesInFolder.stream().forEach(fileDetails -> fileIds.add(fileDetails.getFileId()));
                List<FileDetails> list = responseFileList.stream()
                        .filter(fileDetails -> fileIds.contains(fileDetails.getFileId().longValue()))
                        .collect(Collectors.toList());
                return list;
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseFileList;
    }

    public List<FolderDetails> getAllFolders(UserDetails userDetails) {
        List<FolderDetails> folderDetailsList;
        List<String> userGroupList = new ArrayList<String>();
        userGroupList.add(userDetails.getUserGroup());
        userGroupList.add("ALL");

        if (userDetails.getUserName().equals("Admin")) {
            folderDetailsList = folderDetailsReposiory
                    .getAllByFolderCurrentStatusEqualsOrderByFolderPathAsc("ACTIVE");
        } else {
            folderDetailsList = folderDetailsReposiory
                    .getAllByUserGroupInAndFolderCurrentStatusEqualsOrderByFolderPathAsc(userGroupList, "ACTIVE");
        }
        return folderDetailsList;
    }

    public void performOCR(String originalFileName, String newFileName) {
        File newFile = new File(env.getProperty("file.base.directory") + newFileName);
        File originalFile = new File(env.getProperty("file.base.directory") + originalFileName);
        File textFile = new File(env.getProperty("text.file.base.directory") + newFileName);
        boolean success = newFile.renameTo(originalFile);
        ITesseract instance = new Tesseract();
        String result = null;
        try {
            result = instance.doOCR(originalFile);
            FileWriter myWriter = new FileWriter(textFile);
            myWriter.write(result);
            myWriter.close();
            boolean successRename = originalFile.renameTo(newFile);
            System.out.println("Successfully done OCR for file name : " + originalFileName);
        } catch (Exception e) {
            System.out.println("Unable to perform OCR" + e.getMessage());
        }
    }


    /**
     * fileChange
     *
     * @param fileOperationRequest
     * @return
     * @return Object
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public String fileChange(FileOperation fileOperationRequest)
            throws IllegalAccessException, InvocationTargetException {

        // List<FileDetails> fileDetailsList =
        // fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileOperationRequest.getFileId()));
        String[] fileIdArray = fileOperationRequest.getFileId().split(",");
        List<Long> fileIdList = new ArrayList<>();
        if (fileIdArray != null && fileIdArray.length > 0) {
            for (int i = 0; i < fileIdArray.length; i++) {
                fileIdList.add(Long.parseLong(fileIdArray[i]));
            }

        }

        List<FileDetails> fileDetailsList = fileDetailsRepository.findByFileIdIn(fileIdList);

        List<FolderDetails> folderDetailsList = folderDetailsReposiory
                .findByFolderIdEquals(Long.parseLong(fileOperationRequest.getNodeId()));
        String[] treePathName = {""};
        folderDetailsList.forEach(folderDet -> {
            treePathName[0] = folderDet.getFolderPath();
        });

        if (fileDetailsList.size() > 0) {
            fileDetailsList.forEach(fileDetails -> {

                if (fileOperationRequest.getAction().equals("cut")) {
                    fileDetails.setFolderId(Long.parseLong(fileOperationRequest.getNodeId()));
                    fileDetails.setTreePath(treePathName[0]);
                    fileDetailsRepository.save(fileDetails);

                } else {
                    FileDetails fileDetailsNew = new FileDetails();
                    Long newFileId = fileDetailsNew.getFileId();
                    try {
                        BeanUtils.copyProperties(fileDetailsNew, fileDetails);
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    fileDetailsNew.setFolderId(Long.parseLong(fileOperationRequest.getNodeId()));
                    fileDetailsNew.setTreePath(treePathName[0]);
                    fileDetailsNew.setFileId(newFileId);
                    fileDetailsRepository.save(fileDetailsNew);
                }

            });
        }

        // TODO Auto-generated method stub
        return "Added Succesfully";
    }

    /**
     * splitFile
     *
     * @param file
     * @param nodeId
     * @param userDetails
     * @param fileId
     * @param fromRange
     * @param toRange
     * @return
     * @return Object
     */
    public String splitFile(String nodeId, UserDetails userDetails, String fileId, String newFileName, int fromRange,
                            int toRange) {
        // TODO Auto-generated method stub
        FileDetails currentFileDetails = null;
        String getFileName = getEncryptedFileName();
        String splittedFileName = null;
        if (newFileName != null && newFileName != "") {
            splittedFileName = newFileName + "_" + getFileName;
        } else {
            splittedFileName = getFileName;
        }
        String originalFileName = null;
        String currentFileName = null;
        boolean isSplitCompleted = false;

        List<FileDetails> fileDetailsList = fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileId));
        try {
            if (fileDetailsList.size() > 0) {
                currentFileDetails = fileDetailsList.get(0);
                originalFileName = currentFileDetails.getFileName();
                currentFileName = currentFileDetails.getEncryptedFileName();
                isSplitCompleted = splitFile(splittedFileName, currentFileName, fromRange, toRange);
            }

            if (isSplitCompleted) {
                TempFileDetails tempFileDetails = new TempFileDetails();
                String extension = null;
                String[] fileExtenstion = originalFileName.split(".");

                int index = originalFileName.lastIndexOf('.');
                if (index > 0) {
                    extension = originalFileName.substring(index + 1);
                }

                String filePathWithExt = env.getProperty("file.base.directory") + splittedFileName + "." + extension;
                String filePath = env.getProperty("file.base.directory") + splittedFileName;

                Path file1 = Paths.get(filePath);
                long bytes = Files.size(file1);

                String folderPath = env.getProperty("file.base.directory") + splittedFileName;
                BasicFileAttributes attr = Files.readAttributes(file1, BasicFileAttributes.class);
                FileTime creationDate = attr.creationTime();
                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss");
                String dateCreated = df.format(creationDate.toMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date();
                String currDate = formatter.format(currentDate);

                List<FolderDetails> folderDetailsList = folderDetailsReposiory
                        .findByFolderIdEquals(Long.parseLong(nodeId));
                String[] treePathName = {""};
                folderDetailsList.forEach(folderDet -> {
                    treePathName[0] = folderDet.getFolderPath();
                });

                tempFileDetails.setFileName("split_" + splittedFileName + ".pdf");
                tempFileDetails.setFilePath(filePathWithExt);
                tempFileDetails.setFolderPath(folderPath);
                tempFileDetails.setCreationDate(dateCreated);
                tempFileDetails.setFileSize(String.valueOf(bytes));
                tempFileDetails.setFileExtension(extension);

                tempFileDetails.setFileCurrentStatus("ACTIVE");
                tempFileDetails.setDateUploaded(currDate);
                tempFileDetails.setUserName(userDetails.getUserName());
                tempFileDetails.setTreePathName(treePathName[0]);
                tempFileDetails.setEncryptedFileName(splittedFileName);

                tempFileDetails.setFolderId(Long.parseLong(nodeId));
                tempFileDetails.setUserGroup(userDetails.getUserGroup());
                tempFileDetails.setIndexing("Yes");
                tempFileDetails.setLanguage("English");
                tempFileDetails.setTag("Enter Values");
                tempFileDetails.setUsersTFD("English");
                tempFileDetails.setFileLock("NO");
                tempFileDetailsRepository.save(tempFileDetails);
                System.out.println("Before image processing");
                imageProcessing(tempFileDetails);
                System.out.println("After image processing");
               
            	eventDetailsUtil.loadEvents(userDetails.getUserName(),"New splitted file created : " + "split_" + splittedFileName + ".pdf");
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error occured";

        }

        return "Success";
    }

    /**
     * splitFile
     *
     * @param splitFileName
     * @param currentFileName
     * @param fromRange
     * @param toRange
     * @return
     * @return boolean
     * @throws IOException
     */
    private boolean splitFile(String splitFileName, String currentFileName, int fromRange, int toRange)
            throws IOException {
        // TODO Auto-generated method stub
        String filePath = env.getProperty("file.base.directory") + currentFileName;

        File pdfFile = new File(filePath);
        PDDocument pdfDocument = PDDocument.load(pdfFile);
        System.out.println("pdf size " + pdfDocument.getNumberOfPages());
        if (pdfDocument.getNumberOfPages() < toRange) {
            toRange = pdfDocument.getNumberOfPages();
        }

        Splitter splitter = new Splitter();

        splitter.setStartPage(fromRange);
        splitter.setEndPage(toRange);
        splitter.setSplitAtPage(toRange - fromRange + 1);

        List<PDDocument> lst = splitter.split(pdfDocument);

        PDDocument pdfDocPartial = lst.get(0);
        File newFile = new File(env.getProperty("file.base.directory") + splitFileName);
        pdfDocPartial.save(newFile);
        return true;
    }

    private List<FileDetails> getAllFilesByFolder(Long folderId) {
        List<FileDetails> fileDetailsList = fileDetailsRepository
                .findByFolderIdEqualsAndFileCurrentStatusOrderByCreatedAtDesc(folderId, "ACTIVE");

        return fileDetailsList;
    }
    
public void imageProcessing(TempFileDetails tempFileDetails) {
    	
        
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
//           myTemplate = cachePath.substring(0, cachePath.lastIndexOf(".")).concat(".pdf");
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
               + new java.text.SimpleDateFormat("ddMMyyyy").format(new java.util.Date()) + ".log";
       try {
           BufferedWriter out = new BufferedWriter(new FileWriter(fname, true));
           out.write('"'
                   + "Message Sent Time : "
                   + new java.text.SimpleDateFormat("h:mm:ss").format(new java.util.Date()) + '"' + "\r\n");
           out.write(message + "\r" + "\n" + '"' + "\n" + '"' + "\n");
           out.write("--------------------------------------------------------------------------------------------------------\n");
           out.close();
       } catch (java.lang.Exception e) {
           JOptionPane.showMessageDialog(null, "Log file fails : " + e.getMessage());
       }
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
}
