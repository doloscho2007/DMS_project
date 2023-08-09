package com.arits.docRepo.controller.checkout;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.model.AddFolderResponse;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.service.checkout.CheckoutService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.arits.docRepo.util.UserDetailsUtil.validateSession;

@RestController
public class CheckoutController {
    private CheckoutService checkoutService;
    
    @Autowired
    private EventDetailsUtil eventDetailsUtil;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping("/checkout")
    public ResponseEntity<Object> checkout(@RequestParam("fileId") String fileId,
                                           HttpServletRequest request
                                           ,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to checkout the file : " +  fileId);
        try {
//            validateSession(request);
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            checkoutService.checkOut(userDetails, fileId);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully checked out the file : " + fileId);
            return ResponseEntity.ok().body(new AddFolderResponse("File Checked Out"));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse(exception.getMessage()));
        }
    }

    @GetMapping("/getAllCheckedOutFiles")
    public List<FileDetails> getCheckedOutFiles(HttpServletRequest request
    		 ,@RequestHeader(value="Authorization", required=false ) String bearerToken) throws Exception {
//        validateSession(request);
//        UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to fetch all the checked out file");
    	 eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully fetched all the checked out file");
        return checkoutService.getCheckedOutFiles(userDetails);
    }

    @GetMapping("/getCheckoutStatus")
    public ResponseEntity<Object> getCheckoutStatus(@RequestParam("fileId") String fileId,
                                                    HttpServletRequest request
           ,@RequestHeader(value="Authorization", required=false ) String bearerToken) throws Exception {
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
        try {
//            validateSession(request);
        	
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            if (checkoutService.getCheckoutStatus(userDetails, fileId)) {
                return ResponseEntity.ok().body(new AddFolderResponse("File checked out"));
            } else {
                return ResponseEntity.ok().body(new AddFolderResponse("File is not checked out."));
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse(exception.getMessage()));
        }
    }

    @PostMapping("/checkin")
    public ResponseEntity<Object> checkin(@RequestParam MultipartFile file, @RequestParam String fileId,
                                          HttpServletRequest request
                                          ,@RequestHeader(value="Authorization", required=false ) String bearerToken) throws Exception {
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to checkin the file : " +  fileId);
    	System.out.println("file"+ file + "fileId" + fileId+ "userDetails" + userDetails);
        try {
//            validateSession(request);
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            checkoutService.checkIn(file, fileId, userDetails);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully checked in the file : " + fileId);
            return ResponseEntity.ok().body(new AddFolderResponse("File Checked In"));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }
    }
}
