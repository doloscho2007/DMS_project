package com.arits.docRepo.controller;

import com.arits.docRepo.model.AddFolderResponse;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.model.UserDetailsRequest;
import com.arits.docRepo.service.user.UserDetailsService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class RedirectController {

    private UserDetailsService userDetailsService;
    
    @Autowired
    private EventDetailsUtil eventDetailsUtil;
    
    @Autowired
    private JWTUtil jwtUtil;

    public RedirectController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }    
  

    @GetMapping("/")
    public String loginDefault() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String loginAdminPage() {
        return "index";
    }

    @PostMapping(value = "/validateLogin")
    public ResponseEntity<Object> validateLogin(@RequestBody UserDetailsRequest userDetailsRequest) {
    	eventDetailsUtil.loadEvents(userDetailsRequest.getPinCode(),"User is trying to Login");
        try {
            
            UserDetails userDetails = userDetailsService.validateLogin(userDetailsRequest.getPinCode());
            userDetails.setToken(jwtUtil.createJWT(userDetails));
            
            eventDetailsUtil.loadEvents(userDetailsRequest.getPinCode(),"User successfully Logged in to the DMS application");
            if (userDetails == null) {
            	eventDetailsUtil.loadEvents(userDetailsRequest.getPinCode(),"Invalid user");
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse
                        ("Error Occurred : Please enter valid pin"));
            } else {                
                return ResponseEntity.ok().body(userDetails);
            }
        } catch (Exception exception) {
        	eventDetailsUtil.loadEvents(userDetailsRequest.getPinCode(),"Invalid user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse
                    ("Error Occurred" + exception.getMessage()));
        }
    }
    
    @GetMapping(value = "/refreshToken")
    public ResponseEntity<Object> refreshToken(@RequestHeader(value="Authorization") String bearerToken) {

    	
        try {
          
        	UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
        	userDetails.setToken(jwtUtil.refreshJWT(userDetails));
            if (userDetails == null) {
            	
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse
                        ("Error Occurred : Please enter valid pin"));
            } else {
                
                return ResponseEntity.ok().body(userDetails);
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse
                    ("Error Occurred" + exception.getMessage()));
        }
    }


    @PostMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to Logout");
        request.getSession().invalidate();
        eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully Logged out to the DMS application");
        return ResponseEntity.ok().body(null);
    }


}
