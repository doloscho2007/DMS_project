package com.arits.docRepo.controller.user;

import com.arits.docRepo.dto.UserGroup;
import com.arits.docRepo.model.MessageResponse;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.service.user.UserGroupService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;
import com.arits.docRepo.util.UserDetailsUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.arits.docRepo.util.UserDetailsUtil.validateSession;

@RestController
public class UserGroupController {
    private UserGroupService userGroupService;
    
    @Autowired
    private EventDetailsUtil eventDetailsUtil;

    public UserGroupController(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @PostMapping("/usergroup")
    public ResponseEntity<Object> createUserGroup(@RequestParam("userGroupName") String userGroupName,
                                                  @RequestParam("userGroupStatus") String userGroupStatus,
                                                  @RequestParam("userGroupDescription") String userGroupDescription,
                                                  HttpServletRequest request ,
                                                  @RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
     	eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin is trying to add the user group");
        try {
//            validateSession(request);
            if (userGroupService.createUserGroup(userGroupName, userGroupStatus, userGroupDescription)) {
            	eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin successfully added the user group");
                return ResponseEntity.ok().body(new MessageResponse("User Group Added"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("User Group Already Present"));
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error Occurred : " + exception.getMessage()));
        }
    }

    @GetMapping("/usergroup")
    public List<UserGroup> getAllUserGroup(HttpServletRequest request) {
        return userGroupService.getAllUserGroup();
    }

    @PutMapping("/usergroup")
    public ResponseEntity<Object> updateUserGroup(@RequestParam("userGroupId") String userGroupId,
                                                  @RequestParam("userGroupName") String userGroupName,
                                                  @RequestParam("userGroupStatus") String userGroupStatus,
                                                  @RequestParam("userGroupDescription") String userGroupDescription,
                                                  HttpServletRequest request , 
                                                  @RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
     	eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin is trying to update the user group");
        try {
//            validateSession(request);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin successfully updated the user group");
            return ResponseEntity.ok().body(new MessageResponse(userGroupService.updateUserGroup(userGroupId,userGroupName, userGroupStatus, userGroupDescription)));

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error Occurred : " + exception.getMessage()));
        }
    }

    @DeleteMapping("/usergroup")
    public ResponseEntity<Object> deleteUserGroup(@RequestParam("userGroupId") String userGroupId,
                                                  HttpServletRequest request ,
                                                  @RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
     	eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin is trying to delete the user group");
        try {
//            UserDetailsUtil.validateSession(request);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"Admin successfully deleted the user group");
            return ResponseEntity.ok().body(new MessageResponse(userGroupService.deleteUserGroup(userGroupId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error Occurred : " + exception.getMessage()));
        }
    }
}
