package com.arits.docRepo.controller.user;

import com.arits.docRepo.model.AddFolderResponse;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.service.user.UserDetailsService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;
import com.arits.docRepo.util.UserDetailsUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.arits.docRepo.util.UserDetailsUtil.validateSession;

@RestController
public class UserDetailsController {
    private UserDetailsService userDetailsService;

    @Autowired
    private EventDetailsUtil eventDetailsUtil;
    
    public UserDetailsController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/getUsers")
    public List<UserDetails> getUsers() {
        List<UserDetails> userList = userDetailsService.extractUsers();
        return userList;
    }

    @PostMapping(value = "/deleteUser")
    public ResponseEntity<Object> deleteUser(@RequestParam("userId") String userId, HttpServletRequest request,
    		                             @RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	 UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin is trying to delete the user");
        System.out.println("User ID :" + userId);
        try {
//            UserDetailsUtil.validateSession(request);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin successfully deleted the user");
            return ResponseEntity.ok().body(new AddFolderResponse(userDetailsService.deleteUser(userId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }
    }

    @PostMapping(value = "/updateUserDetails")
    public ResponseEntity<Object> updateUserDetails(@RequestParam("loginId") String loginId,
                                                    @RequestParam("userName") String userName, @RequestParam("status") String status,
                                                    @RequestParam("userGroup") String userGroup, @RequestParam("pin") String pin,
                                                    HttpServletRequest request,
                                                    @RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	System.out.println("userDetails.getUserName()"+ userDetails.getUserName());
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin is trying to update the user details");
        try {
//            validateSession(request);
        	UserDetails userDetails1 = userDetailsService.validateLogin(pin);
         

            if (userDetails1 == null || userDetails1.getLoginId() == Integer.parseInt(loginId)) {
//            	eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin successfully updated the user details");
                return ResponseEntity.ok().body(new AddFolderResponse(
                        userDetailsService.updateUserDetails(loginId, userName, status, userGroup, pin)));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AddFolderResponse("Error Occured : Please choose different pin"));
            }


        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occured : " + exception.getMessage()));
        }
    }

    @PostMapping(value = "/addUserDetails")
    public ResponseEntity<Object> addUserDetails(@RequestParam("userName") String userName,
                                                 @RequestParam("status") String status,
                                                 @RequestParam("userGroup") String userGroup,
                                                 @RequestParam("pin") String pin,
                                                 HttpServletRequest request , 
                                                 @RequestHeader(value="Authorization", required=false ) String bearerToken) {

//        UserDetails userDetails2 = (UserDetails) request.getSession().getAttribute("userDetails");
        UserDetails userDetails2 = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails2.getUserName(),"Admin is trying to add the user");
        try {
//            validateSession(request);
             UserDetails userDetails = userDetailsService.validateLogin(pin);
            

            if (userDetails == null) {
            	
            	eventDetailsUtil.loadEvents(userDetails2.getUserName(),"Admin successfully added the user");
                return ResponseEntity.ok().body(new AddFolderResponse(
                        userDetailsService.addUserDetails(userName, status, userGroup, pin)));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AddFolderResponse("Error Occurred : Please choose different pin"));
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }
    }
}
