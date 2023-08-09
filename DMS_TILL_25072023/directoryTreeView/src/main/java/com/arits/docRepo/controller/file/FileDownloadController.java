package com.arits.docRepo.controller.file;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.service.file.FileExtensionService;
import com.arits.docRepo.service.file.FileService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;

import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class FileDownloadController {
	
	 @Autowired
	    private EventDetailsUtil eventDetailsUtil;

    @Value("${server.path}")
    private String serverPath;

    
    @Value("${repo.base.directory}")
    private String localPath;
    
    @Value("${server.file.path}")
    private String serverFilePath;
    private FileService fileService;

    @Autowired
    private JWTUtil jwtUtil;
    
    private FileExtensionService fileExtensionService;

    public FileDownloadController(FileService fileService,
                                  FileExtensionService fileExtensionService) {
        this.fileService = fileService;
        this.fileExtensionService = fileExtensionService;
    }

    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response ,HttpServletRequest request,
                             @RequestParam("fileId") String fileId,@RequestHeader(value="Authorization", required=false ) String bearerToken,
                             @RequestParam("pdfFile") String pdfFile,
                             @RequestParam(required = false) String version
                             ) {
    	if(version==null) {
    		version = "1";
    	}
    	//UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
//    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to download the file : " +  fileId);
    	
        Date dateValue = new Date();
        String[] fileIdArray = null;
        String filePath = null;
        String zipFileName = "Documents_" + dateValue.getTime() + ".zip";
        String contentType = "application/zip";
        String headerKey = "Content-Disposition";
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        String fileName = null;
        byte[] buffer = new byte[128];
        try {
            if (fileId.contains(",")) {
//            	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to download the multiple files : " +  fileId + " : version : " +version);
                fos = new FileOutputStream(localPath+"\\temp\\" + zipFileName);
                zos = new ZipOutputStream(fos);
                fileIdArray = fileId.split(",");
                eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to download the multiple files : " +  fileIdArray.length + " : version : " +version);
                for (int i = 0; i < fileIdArray.length; i++) {
                    FileDetails fileDetails = fileService.getFileName(fileIdArray[i]);
                    fileName = fileDetails.getFileName();
                    filePath = fileDetails.getFilePath();
                    
                    if (pdfFile.equals("Yes")) {
                        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + i + ".pdf";
                        filePath = fileDetails.getPdfFilePath();
                    } else {
                        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + i + fileName.substring(fileName.lastIndexOf("."), fileName.length());
                        filePath = filePath.substring(0, filePath.lastIndexOf("."));
                    }
                    System.out.println("filepath 3 :" + filePath);
                    
                    File currentFile = new File(filePath);
                    File newFile = new File(localPath+"\\temp\\" + fileName);
                    System.out.println("filepath 4 :" + currentFile);
                    System.out.println("filepath 5 :" + newFile);
                    FileUtils.copyFile(currentFile, newFile);
                    ZipEntry entry = new ZipEntry(newFile.getName());
                    FileInputStream fis = new FileInputStream(currentFile);
                    zos.putNextEntry(entry);
                    int read = 0;
                    while ((read = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, read);
                    }
                    zos.closeEntry();
                    fis.close();
                    FileUtils.forceDelete(new File(localPath+"\\temp\\" + newFile.getName()));
                }
                zos.close();
                String headerValue = String.format("attachment; filename=\"%s\"", zipFileName);
                response.setHeader(headerKey, headerValue);
                response.setContentType(contentType);
               
                System.setProperty("http.agent", "Chrome");
                InputStream inputStream = new FileInputStream(new File(localPath+"\\temp\\" + zipFileName));
                IOUtils.copy(inputStream, response.getOutputStream());
                inputStream.close();
                if (pdfFile.equals("Yes")) {
                	 eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully downloads multiple files in pdf as zip");
                } else {
                	 eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully downloads multiple files as zip");
                }
               
                //FileUtils.forceDelete(new File(localPath+"\\temp\\" + zipFileName));

            } else {
                FileDetails fileDetails = fileService.getFileName(fileId);
                fileName = fileDetails.getFileName();
                filePath = fileDetails.getFilePath();
                filePath = filePath.replace(serverPath, serverFilePath);
                contentType = fileExtensionService.getContentType(filePath);
                System.out.println("filepath 3 :" + filePath);
                if (pdfFile.equals("Yes")) {
                    fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";
                    filePath = fileDetails.getPdfFilePath();
                    filePath = filePath.replace(serverPath, serverFilePath);
                    //filePath = filePath.substring(0, filePath.lastIndexOf("."));
                } else {
                    filePath = filePath.substring(0, filePath.lastIndexOf("."));
                }
                System.out.println("filepath 3 :" + filePath);

                String headerValue = String.format("attachment; filename=\"%s\"", fileName);
                response.setHeader(headerKey, headerValue);
                response.setContentType(contentType);

                URLConnection openConnection = new URL(filePath).openConnection();
                System.setProperty("http.agent", "Chrome");
                InputStream inputStream = openConnection.getInputStream();
                IOUtils.copy(inputStream, response.getOutputStream());
                if (pdfFile.equals("Yes")) {
                	 eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully download the file as a pdf : " + fileId  + " : version : " +version);
                } else {
                	 eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully download the file : " + fileId  + " : version : " +version);
                }
               
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/downloadFileOld")
    public void downloadFileOld(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam("fileId") String fileId, @RequestParam("filePath") String filePath,
                                @RequestParam("pdfFile") String pdfFile) {
    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to download the older version of the file : " +  fileId);
        filePath = filePath.replace(serverPath, serverFilePath);
        String contentType = fileExtensionService.getContentType(filePath);
        String headerKey = "Content-Disposition";
        String fileName = fileService.getFileName(fileId).getFileName();

        if (pdfFile.equals("Yes")) {
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";
        } else {
            filePath = filePath.substring(0, filePath.lastIndexOf("."));
        }
        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader(headerKey, headerValue);
        response.setContentType(contentType);
        try {

            URLConnection openConnection = new URL(filePath).openConnection();
            System.setProperty("http.agent", "Chrome");
            InputStream inputStream = openConnection.getInputStream();
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
