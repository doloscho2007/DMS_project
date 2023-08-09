package com.arits.docRepo.controller.bookmark;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.model.AddFolderResponse;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.service.bookmark.BookmarkService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.arits.docRepo.util.UserDetailsUtil.validateSession;

@RestController
public class BookmarkController {
    private BookmarkService bookmarkService;
    
    @Autowired
    private EventDetailsUtil eventDetailsUtil;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @GetMapping("/addBookmark")
    public ResponseEntity<Object> addBookmark(@RequestParam("fileId") String fileId,
                                              HttpServletRequest request
                                              ,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to bookmark the file : " +  fileId);
        try {
//            validateSession(request);
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            bookmarkService.addBookmark(userDetails, fileId);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully bookmarked the file : " + fileId);
            return ResponseEntity.ok().body(new AddFolderResponse("Bookmark Added"));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse(exception.getMessage()));
        }
    }

    @GetMapping("/deleteBookmark")
    public ResponseEntity<Object> deleteBookmark(@RequestParam("fileId") String fileId,
                                                 HttpServletRequest request
                                                 ,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to delete the bookmarked file : " + fileId);
        try {
//            validateSession(request);
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            bookmarkService.deleteBookmark(userDetails, fileId);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully delete bookmarked file : " + fileId);
            return ResponseEntity.ok().body(new AddFolderResponse("Bookmark Removed"));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse(exception.getMessage()));
        }
    }

    @GetMapping("/getAllBookmarkedFiles")
    public List<FileDetails> getBookmarkedFiles(HttpServletRequest request
    		                                   ,@RequestHeader(value="Authorization", required=false ) String bearerToken) throws Exception {
//        validateSession(request);
//        UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
        UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to fetch all the bookmarked file");
    	 eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully fetched all the bookmarked file");
        return bookmarkService.getBookmarkedFiles(userDetails);
    }

    @GetMapping("/getBookmarkStatus")
    public ResponseEntity<Object> getBookmarkStatus(@RequestParam("fileId") String fileId,
                                                    HttpServletRequest request
                                                    ,@RequestHeader(value="Authorization", required=false ) String bearerToken) throws Exception {
    	UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
        try {
//            validateSession(request);
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            if (bookmarkService.getBookmarkStatus(userDetails, fileId)) {
                return ResponseEntity.ok().body(new AddFolderResponse("Bookmark Present"));
            } else {
                return ResponseEntity.ok().body(new AddFolderResponse("Bookmark Not Present"));
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AddFolderResponse(exception.getMessage()));
        }
    }

}
