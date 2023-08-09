package com.arits.docRepo.service.file;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.repo.FileDetailsRepository;
import com.arits.docRepo.service.checkout.CheckoutService;
import com.arits.docRepo.service.versioning.FileVersionService;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {

    private FileDetailsRepository fileDetailsRepository;
    private FileVersionService fileVersionService;

    private CheckoutService checkoutService;

    @Autowired
    private Environment env;

    public FileService(FileDetailsRepository fileDetailsRepository,
                       FileVersionService fileVersionService,
                       CheckoutService checkoutService) {
        this.fileDetailsRepository = fileDetailsRepository;
        this.fileVersionService = fileVersionService;
        this.checkoutService = checkoutService;
    }

    public List<FileDetails> getFileDetails(String nodeId) {
        List<FileDetails> fileDetailsList = fileDetailsRepository
                .findByFolderIdEqualsAndFileCurrentStatusOrderByCreatedAtDesc(Long.parseLong(nodeId), "ACTIVE");

        return fileDetailsList;
    }

    public String deleteFile(String fileId) {
        String[] fileIdArray = null;
        boolean success = false;

        if (fileId.contains(",")) {
            fileIdArray = fileId.split(",");
        } else {
            fileIdArray = new String[1];
            fileIdArray[0] = fileId;
        }
        for (int i = 0; i < fileIdArray.length; i++) {
            List<FileDetails> fileDetailsList = fileDetailsRepository
                    .findByFileIdEquals(Long.parseLong(fileIdArray[i]));

            if (fileDetailsList.size() > 0) {
                FileDetails fileDetails =
                        fileDetailsList.get(0);
                if (checkoutService.isCheckedOut(fileDetails.getFileId())) {
                    return "Some Files are checked out. Can not be deleted.";
                }
                fileDetails.setFileCurrentStatus("DELETED");
                fileDetailsRepository.save(fileDetails);
                success = true;
            }

        }
        if (success) {
            return "File Deleted";
        } else {
            return "An Error occurred";
        }

    }

    public String updateFileName(String fileId, String fileName, String fileSearch1, String fileSearch2,
                                 String fileSearch3, Date fileDate) {
        List<FileDetails> fileDetailsList = fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileId));
        if (fileDetailsList.size() > 0) {
            FileDetails fileDetails = fileDetailsList.get(0);
            String extension = null;

            int index = fileDetails.getFileName().lastIndexOf('.');

            if (index > 0) {
                extension = fileDetails.getFileName().substring(index + 1);
            }

            if (fileName.contains(".")) {
                fileDetails.setFileName(fileName);
            } else {
                fileDetails.setFileName(fileName + "." + extension);
            }
            if (fileSearch1 != null && !fileSearch1.equals("") && !fileSearch1.equals("null")) {
                fileDetails.setFileSearch1(fileSearch1);
            }
            if (fileSearch2 != null && !fileSearch2.equals("") && !fileSearch2.equals("null")) {
                fileDetails.setFileSearch2(fileSearch2);
            }
            if (fileSearch3 != null && !fileSearch3.equals("") && !fileSearch3.equals("null")) {
                fileDetails.setFileSearch3(fileSearch3);
            }
            if (fileDate != null) {
                fileDetails.setCreatedAt(fileDate);
            }
            fileDetailsRepository.save(fileDetails);
            return "File details got updated";
        }
        return "An Error occurred";
    }

    public String updateFileProperty(String fileId, String cprId, String staffId, String staffCode) {
        List<FileDetails> fileDetailsList = fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileId));
        if (fileDetailsList.size() > 0) {
            FileDetails fileDetails = fileDetailsList.get(0);
            if (cprId != null && cprId != "") {
                fileDetails.setCprId(cprId);
            }
            if (staffId != null && staffId != "") {
                fileDetails.setStaffId(staffId);
            }
            if (staffCode != null && staffCode != "") {
                fileDetails.setStaffCode(staffCode);
            }
            fileDetailsRepository.save(fileDetails);
            return "File property got updated";
        }
        return "An Error occurred";
    }

    public FileDetails getFileName(String fileId) {
        List<FileDetails> fileDetailsList = fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileId));
        FileDetails fileDetails = null;
        if (fileDetailsList.size() > 0) {
            fileDetails = fileDetailsList.get(0);

        } else {
            fileDetails = new FileDetails();
            fileDetails.setFileName("defaultFileName.pdf");
        }
        return fileDetails;
    }

    public String stampFile(String fileId, String stampStatus, String pagePreference, String userName) {
        String[] fileIdArray = null;
        boolean success = false;

        if (fileId.contains(",")) {
            fileIdArray = fileId.split(",");
        } else {
            fileIdArray = new String[1];
            fileIdArray[0] = fileId;
        }
        for (int i = 0; i < fileIdArray.length; i++) {
            List<FileDetails> fileDetailsList = fileDetailsRepository
                    .findByFileIdEquals(Long.parseLong(fileIdArray[i]));


            if (fileDetailsList.size() > 0) {
                FileDetails fileDetails =
                        fileDetailsList.get(0);
                String newEncryptedFileName = getAlphaNumericString(7);
                String filePath = env.getProperty("file.base.directory") + fileDetails.getEncryptedFileName();
                String newFilePath = env.getProperty("file.base.directory") + newEncryptedFileName;
                success = stampFileUtil(filePath, stampStatus, pagePreference, newFilePath);
                if (!success) {
                    return "An Error occurred";
                }
                fileDetails.setFilePath(newFilePath + ".pdf");
                fileDetails.setPdfFilePath(newFilePath);
                fileDetails.setEncryptedFileName(newEncryptedFileName);
                fileDetails.setCurrentVersion(String.valueOf(Integer.valueOf(fileDetails.getCurrentVersion()) + 1));
                fileDetailsRepository.save(fileDetails);
                fileVersionService.addVersionDetailsForPdf(fileDetails, "File Stamped", userName);
            }

        }
        return "File/s Stamped";
    }

    private boolean stampFileUtil(String filePath, String status, String pagePreference, String newFilePath) {
        try {
            PDDocument realDoc = PDDocument.load(new File(filePath));
            PDPage doc = realDoc.getPage(0);
            float height = doc.getMediaBox().getHeight();
            Overlay overlay = new Overlay();
            overlay.setInputPDF(realDoc);
            overlay.setOverlayPosition(Overlay.Position.FOREGROUND);
            if (pagePreference.equals("first")) {
                overlay.setFirstPageOverlayPDF(createOverlay(status, height));
            } else if (pagePreference.equals("last")) {
                overlay.setLastPageOverlayPDF(createOverlay(status, height));
            } else {
                overlay.setAllPagesOverlayPDF(createOverlay(status, height));
            }
            Map<Integer, String> ovmap = new HashMap<>(); // empty map is a dummy
            overlay.overlay(ovmap);
            realDoc.save(new File(newFilePath));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static PDDocument createOverlay(String text, float height) throws IOException {

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        int fontSize = 20;
        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.TIMES_BOLD_ITALIC;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float titleWidth = font.getStringWidth(text) / 1000 * fontSize;
        float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

        // Create the text and position it
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        if (text.equalsIgnoreCase("APPROVED")) {
            contentStream.setNonStrokingColor(0, 255, 0);
        } else {
            contentStream.setNonStrokingColor(255, 0, 0);
        }

        //contentStream.moveTo(xVal, yVal);
        contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, height / 2);
        contentStream.showText(text + " - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        contentStream.endText();

        // Make sure that the content stream is closed:
        contentStream.close();

        //return the string doc
        return document;
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

//    public String updateVersions() {
//        Iterable<FileDetails> fileDetailsList = fileDetailsRepository.findAll();
//        fileDetailsList.forEach(fileDetails -> {
//            fileVersionService.addVersionDetailsForPdf(fileDetails, "New File Created.", "Admin");
//        });
//        return "Successfully Copied.";
//    }
}
