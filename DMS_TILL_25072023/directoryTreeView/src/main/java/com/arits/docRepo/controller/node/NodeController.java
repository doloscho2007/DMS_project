package com.arits.docRepo.controller.node;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.dto.FolderDetails;
import com.arits.docRepo.model.*;
import com.arits.docRepo.service.node.NodeService;
import com.arits.docRepo.service.user.UserDetailsService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.arits.docRepo.util.UserDetailsUtil.validateSession;

@RestController
@CrossOrigin
public class NodeController {
	
	@Autowired
    private EventDetailsUtil eventDetailsUtil;
	
	@Autowired
    private JWTUtil jwtUtil;

    private NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }
    
    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping("/nodes")
    public List<Node> nodes(HttpServletRequest request,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
    	
        List<Node> nodes = null;
        System.out.println("Inside validate nodes");
        
        UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
        
        if (userDetails != null) {
			nodes = nodeService.getAllNodeDetails(userDetails);
		}
        List<Node> nodes1 = new ArrayList<Node>();
        nodes1.add(nodes.get(0));
		
		//return nodes;
        return nodes1;
		
    }

    @PostMapping(value = "/addFolderName")
    public ResponseEntity<Object> addFolderName(@RequestBody AddFolderRequest addFolderRequest,
                                                HttpServletRequest request,@RequestHeader(value="Authorization") String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	 UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
    	 System.out.println("userDetails:"+userDetails );
    	 System.out.println("addFolderRequest:"+addFolderRequest.getParentId());
    	 System.out.println("addFolderRequest:"+addFolderRequest.getFolderName());
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to add the folder");
        try {
            //validateSession(request);
//            UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
            nodeService.addNewFolder(addFolderRequest, userDetails);
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully added the folder");
            return ResponseEntity.ok().body(new AddFolderResponse("Added Successfully"));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occured : " + exception.getMessage()));
        }
    }


    @PostMapping(value = "/deleteFolder")
    public ResponseEntity<Object> deleteFolder(@RequestParam("folderId") String folderId, HttpServletRequest request
    												,@RequestHeader(value="Authorization") String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
   	         UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to delete the folder : " + folderId);
        try {
//            validateSession(request);
            String updateResponse = nodeService.deleteFolder(folderId);
            if (updateResponse.equals("Defult Node can't be deleted")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AddFolderResponse("Error Occured : " + updateResponse));
            }
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully deleted the folder : " + folderId);
            return ResponseEntity.ok().body(new AddFolderResponse(nodeService.deleteFolder(folderId)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occured : " + exception.getMessage()));
        }
    }

    @PostMapping(value = "/updateFolder")
    public ResponseEntity<Object> updateFolder(@RequestParam("folderId") String folderId,
                                               @RequestParam("folderName") String folderName, HttpServletRequest request
                                               ,@RequestHeader(value="Authorization") String bearerToken) {
//    	UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	   UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to update the folder name : " + folderId);
        try {
//            validateSession(request);
            String updateResponse = nodeService.updateFolder(folderId, folderName);
            if (updateResponse.equals("Defult Node can't be edited")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AddFolderResponse("Error Occured : " + updateResponse));
            }
            eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully updated the folder name : " + folderId);
            return ResponseEntity.ok().body(new AddFolderResponse(updateResponse));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occured : " + exception.getMessage()));
        }
    }

    @GetMapping("/getAllFolders")
    public List<FolderDetails> getAllFolderDetails(HttpServletRequest request,@RequestHeader(value="Authorization", required=false ) String bearerToken){
        //UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
        UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
        return nodeService.getAllFolders(userDetails);
    }

    @PostMapping(value = "/searchFolder")
    public List<FileDetails> searchFolder(@RequestParam("searchParam") String searchParam,
                                          @RequestParam("fileSearch") String fileSearchParam,
                                          @RequestParam("fileNameSearch") String fileName,
                                          @RequestParam("folderSearch") String folderPathName,
                                          @RequestParam("fileDate1") String fileDate1,
                                          @RequestParam("fileDate2") String fileDate2,
                                          @RequestParam("creationDate1") String creationDate1,
                                          @RequestParam("creationDate2") String creationDate2,
                                          @RequestParam("W") boolean isWholeWord,
                                          HttpServletRequest request
                                          ,@RequestHeader(value="Authorization", required=false ) String bearerToken
                                          ) {
        List<FileDetails> result;
    UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
      //  UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
        System.out.println("userDetails:" + userDetails);
        folderPathName=folderPathName.replace("-"," ");
        result = nodeService.searchIndex(searchParam, fileSearchParam, fileName, folderPathName, isWholeWord, userDetails);
        if (!StringUtils.isEmpty(fileDate1) && !StringUtils.isEmpty(fileDate2)) {
            result = nodeService.filterByFileDate(Date.valueOf(fileDate1), Date.valueOf(fileDate2), result);
        }
        if (!StringUtils.isEmpty(creationDate1) && !StringUtils.isEmpty(creationDate2)) {
            result = nodeService.filterByCreationDate(Date.valueOf(creationDate1), Date.valueOf(creationDate2), result);
        }
        return result;
    }


    @PostMapping("/fileOperation")
    public ResponseEntity<Object> fileOperation(@RequestParam("fileId") String fileId,
                                                @RequestParam("nodeId") String nodeId, @RequestParam("action") String action, HttpServletRequest request
                                                ,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
        System.out.println("File id " + fileId);
        FileOperation fileOperationRequest = new FileOperation();
        fileOperationRequest.setAction(action);
        fileOperationRequest.setFileId(fileId);

        fileOperationRequest.setNodeId(nodeId);
        try {
//            validateSession(request);
            return ResponseEntity.ok().body(new AddFolderResponse(nodeService.fileChange(fileOperationRequest)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }

    }
    
    @PostMapping("/upload")
    public ResponseEntity<Object> uploadData(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request            
    		, @RequestParam String nodeId,@RequestHeader(value="Authorization") String bearerToken) {
    	//UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	 UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	 MultipartFile[] files = new MultipartFile[1];
    	 files[0]=multipartFile;
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to upload the file");
        try {
            validateSession(request);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }

        eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully uploaded the file");
        return ResponseEntity.ok().body(nodeService.fileUpload(files, nodeId, userDetails));
    }

    @PostMapping("/upload1")
    public ResponseEntity<Object> uploadData1(@RequestParam("files") MultipartFile[] files, @RequestParam String nodeId,
                                             HttpServletRequest request
                                             ,@RequestHeader(value="Authorization") String bearerToken) {
    	//UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	 UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to upload the file");
       
        return ResponseEntity.ok().body(nodeService.fileUpload(files, nodeId, userDetails));
    }

    @PostMapping("/mergeFile")
    public ResponseEntity<Object> MergeFile(@RequestParam MultipartFile[] files, @RequestParam String nodeId,
                                            @RequestParam String fileId, HttpServletRequest request
                                            ,@RequestHeader(value="Authorization") String bearerToken) {
//        UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
        UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to merge the file : " + fileId);
        try {
//            validateSession(request);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occured : " + exception.getMessage()));
        }

        eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully merged the file : " + fileId);
        return ResponseEntity.ok().body(nodeService.mergeFile(files, nodeId, userDetails, fileId));
    }

    @PostMapping("/splitFile")

    public ResponseEntity<Object> SplitFile(@RequestParam String nodeId,
                                            @RequestParam String fileId,
                                            @RequestParam String newFileName,	
                                            @RequestParam String fromRange,
                                            @RequestParam String toRange,
                                            HttpServletRequest request ,@RequestHeader(value="Authorization") String bearerToken) {
//        UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	 UserDetails userDetails = JWTUtil.validateTokenAndGetJws(bearerToken);
    	 System.out.println("userDetails is "+userDetails );
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to split the file : " + fileId);
        try {
//            validateSession(request);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + exception.getMessage()));
        }

        eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully splitted the file : " + fileId);
        return ResponseEntity.ok().body(nodeService.splitFile(nodeId, userDetails, fileId, newFileName, Integer.parseInt(fromRange),
                Integer.parseInt(toRange)));
    }
}
