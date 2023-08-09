package com.arits.docRepo.controller.file;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.model.AddFolderResponse;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.service.file.FileService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;

import static com.arits.docRepo.util.UserDetailsUtil.validateSession;

@RestController
@CrossOrigin
public class FileController {

	 @Autowired
	    private EventDetailsUtil eventDetailsUtil;
	 
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/getFiles")
    public List<FileDetails> getFiles(@RequestParam("nodeId") String nodeId) {
        List<FileDetails> fileDetailsList = fileService.getFileDetails(nodeId);
        return fileDetailsList;
    }

    @PostMapping(value = "/deleteFile")
    public ResponseEntity<Object> deleteFile(@RequestParam("fileId") String fileId, HttpServletRequest request
    		                                 ,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
    	 UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to delete the file : " +  fileId);
        try {
//            validateSession(request);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully deleted the file : " + fileId);
            return ResponseEntity.ok().body(new AddFolderResponse(fileService.deleteFile(fileId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }
    }

    @PostMapping(value = "/updateFileName")
    public ResponseEntity<Object> updateFileName(@RequestParam("fileId") String fileId,
                                                 @RequestParam("fileName") String fileName,
                                                 @RequestParam("fileSearch1") String fileSearch1,
                                                 @RequestParam("fileSearch2") String fileSearch2,
                                                 @RequestParam("fileSearch3") String fileSearch3,
                                                 @RequestParam("createdDate") Date fileDate,
                                                 HttpServletRequest request
                                                 ,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	 UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to update the file property : " +  fileId);
        try {
//            validateSession(request);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully updated the file property : " + fileId);
            return ResponseEntity.ok()
                    .body(new AddFolderResponse(fileService.updateFileName(fileId, fileName,
                            fileSearch1,fileSearch2,fileSearch3,fileDate)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }
    }

    @PostMapping(value = "/updateFileProperty")
    public ResponseEntity<Object> updateFileProperty(@RequestParam("fileId") String fileId,
                                                     @RequestParam("cprId") String cprId,
                                                     @RequestParam("staffId") String staffId,
                                                     @RequestParam("staffCode") String staffCode,
                                                     HttpServletRequest request) {
    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to update the file property : " +  fileId);
        try {
            validateSession(request);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully updated the file property : " + fileId);
            return ResponseEntity.ok()
                    .body(new AddFolderResponse(fileService.updateFileProperty(fileId, cprId, staffId, staffCode)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }
    }
   
    @GetMapping(value = "/stampFile")
    public ResponseEntity<Object> stampFiles(@RequestParam String fileId,
                                             @RequestParam String stampStatus,
                                             @RequestParam String pagePreference,
                                             HttpServletRequest request 
                                             ,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to stamp the file : " +  fileId);
        try {
//            validateSession(request);
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully stamped the file : " + fileId);
            return ResponseEntity.ok()
                    .body(new AddFolderResponse(fileService.stampFile(fileId, stampStatus,pagePreference,userDetails.getUserName())));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }
    }
//    @GetMapping(value = "/updatePreviousVersions")
//    public ResponseEntity<Object> updateVersions() {
//        try {
//            return ResponseEntity.ok()
//                    .body(new AddFolderResponse(fileService.updateVersions()));
//        } catch (Exception exception) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
//        }
//    }

}
