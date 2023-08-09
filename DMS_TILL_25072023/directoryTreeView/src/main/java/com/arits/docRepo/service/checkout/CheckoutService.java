package com.arits.docRepo.service.checkout;

import com.arits.docRepo.dto.CheckOutFile;
import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.dto.TempFileDetails;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.repo.CheckoutRepository;
import com.arits.docRepo.repo.FileDetailsRepository;
import com.arits.docRepo.repo.TempFileDetailsRepository;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    private CheckoutRepository checkoutRepository;
    private FileDetailsRepository fileDetailsRepository;

    private TempFileDetailsRepository tempFileDetailsRepository;

    @Autowired
    private Environment env;


    public CheckoutService(CheckoutRepository checkoutRepository,
                           FileDetailsRepository fileDetailsRepository,
                           TempFileDetailsRepository tempFileDetailsRepository) {
        this.checkoutRepository = checkoutRepository;
        this.fileDetailsRepository = fileDetailsRepository;
        this.tempFileDetailsRepository = tempFileDetailsRepository;
    }

    public void checkOut(UserDetails userDetails, String fileId) {
        List<CheckOutFile> checkOutFiles = checkoutRepository.findCheckOutFileByUserNameEquals(userDetails.getUserName());
        if (checkOutFiles.stream().filter(checkOutFile -> checkOutFile.getFileDetails().getFileId() == Long.parseLong(fileId))
                .collect(Collectors.toList()).size() > 0)
            throw new RuntimeException("Already Checked out");
        List<FileDetails> fileDetails = fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileId));
        if (fileDetails.size() == 1) {
            FileDetails file = fileDetails.get(0);
            CheckOutFile checkOutFile = new CheckOutFile();
            checkOutFile.setFileDetails(file);
            checkOutFile.setUserName(userDetails.getUserName());
            checkoutRepository.save(checkOutFile);
        } else {
            throw new RuntimeException("Unable to find matching file in database");
        }
    }

    public List<FileDetails> getCheckedOutFiles(UserDetails userDetails) {
        List<CheckOutFile> checkOutFiles = checkoutRepository.findCheckOutFileByUserNameEquals(userDetails.getUserName());
        List<FileDetails> fileDetails = new ArrayList<>();
        checkOutFiles.stream().forEach(checkOutFile -> {
            fileDetails.add(checkOutFile.getFileDetails());
        });
        return fileDetails;
    }

    public boolean getCheckoutStatus(UserDetails userDetails, String fileId) {
        List<CheckOutFile> checkOutFiles = checkoutRepository.findCheckOutFileByUserNameEqualsAndFileDetails_FileId(
                userDetails.getUserName(),
                Long.parseLong(fileId));
        if (checkOutFiles.size() > 0) {
            return true;
        }
        return false;
    }

    public void checkIn(UserDetails userDetails, String fileId) {
        try {
            CheckOutFile checkOutFile = checkoutRepository.findCheckOutFileByUserNameEqualsAndFileDetails_FileId(
                    userDetails.getUserName(),
                    Long.parseLong(fileId)).get(0);
            checkoutRepository.delete(checkOutFile);
        } catch (Exception e) {
            throw new RuntimeException("File is not checked out.");
        }
    }

    @Transactional
    public void checkIn(MultipartFile file, String fileId, UserDetails userDetails) {
        try {
            createTempFile(file, fileId, userDetails);
            CheckOutFile checkOutFile = checkoutRepository.findCheckOutFileByUserNameEqualsAndFileDetails_FileId(
                    userDetails.getUserName(),
                    Long.parseLong(fileId)).get(0);
            checkoutRepository.delete(checkOutFile);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isCheckedOut(long fileId) {
        return checkoutRepository.existsByFileDetails_FileId(fileId);
    }

    private void createTempFile(MultipartFile file, String fileId, UserDetails userDetails) {
        FileDetails fileDetails = fileDetailsRepository.getByFileId(Long.parseLong(fileId));
        String fileName = file.getOriginalFilename();
        if (!fileDetails.getFileName().equals(fileName)) {
            throw new RuntimeException("Checkin file name not matching with real file name");
        }
        TempFileDetails tempFileDetails = new TempFileDetails();
        String extension = null;

        assert fileName != null;
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }

        String encryptedFileName = getEncryptedFileName();
        String filePathWithExt = env.getProperty("file.base.directory") + encryptedFileName + "." + extension;
        String filePath = env.getProperty("file.base.directory") + encryptedFileName;
        InputStream is = null;
        try {
            is = file.getInputStream();

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

            tempFileDetails.setOrigFileId(fileDetails.getFileId());
            tempFileDetails.setCurrentVersion(Integer.parseInt(fileDetails.getCurrentVersion()));
            tempFileDetails.setFileName(fileName);
            tempFileDetails.setFilePath(filePathWithExt);
            tempFileDetails.setFolderPath(folderPath);
            tempFileDetails.setCreationDate(dateCreated);
            tempFileDetails.setFileSize(String.valueOf(file.getSize()));
            tempFileDetails.setFileExtension(extension);

            tempFileDetails.setFileCurrentStatus("ACTIVE");
            tempFileDetails.setDateUploaded(currDate);
            tempFileDetails.setUserName(userDetails.getUserName());
            tempFileDetails.setTreePathName(fileDetails.getTreePath());
            tempFileDetails.setEncryptedFileName(encryptedFileName);

            tempFileDetails.setFolderId(fileDetails.getFolderId());
            tempFileDetails.setUserGroup(userDetails.getUserGroup());
            tempFileDetails.setIndexing("Yes");
            tempFileDetails.setLanguage("English");
            tempFileDetails.setTag("Enter Values");
            tempFileDetails.setUsersTFD("English");
            tempFileDetails.setFileLock("NO");
            tempFileDetailsRepository.save(tempFileDetails);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void performOCR(String originalFileName, String newFileName) {
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
}
