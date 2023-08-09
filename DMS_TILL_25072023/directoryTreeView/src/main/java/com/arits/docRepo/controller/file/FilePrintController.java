package com.arits.docRepo.controller.file;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.model.AddFolderResponse;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.service.file.FileService;
import com.arits.docRepo.util.EventDetailsUtil;
import com.arits.docRepo.util.JWTUtil;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.servlet.http.HttpServletRequest;
import java.awt.print.PrinterJob;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.arits.docRepo.util.UserDetailsUtil.validateSession;

@RestController
public class FilePrintController {
	
	 @Autowired
	    private EventDetailsUtil eventDetailsUtil;
	 
		@Autowired
	    private JWTUtil jwtUtil;

    @Value("${server.path}")
    private String serverPath;

    @Value("${server.file.path}")
    private String serverFilePath;

    private FileService fileService;

    public FilePrintController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/printFile")
    public ResponseEntity<Object> printFile(HttpServletRequest request,
                                            @RequestParam("fileId") String fileId,@RequestHeader(value="Authorization", required=false ) String bearerToken) {
    	
    	//UserDetails userDetails = (UserDetails) request.getSession().getAttribute("userDetails");
    	  UserDetails userDetails = jwtUtil.validateTokenAndGetJws(bearerToken);
    	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User is trying to print the file : " +  fileId);

        String[] fileIdArray = null;
        boolean success = false;

        // filePath="http://192.168.0.103:8887/10007441_20200508_014016.pdf";

        if (fileId.contains(",")) {
            fileIdArray = fileId.split(",");
        } else {
            fileIdArray = new String[1];
            fileIdArray[0] = fileId;
        }

        try {
            
            for (int i = 0; i < fileIdArray.length; i++) {
                FileDetails fileDetails = fileService.getFileName(fileIdArray[i]);
                String fileName = fileDetails.getFileName();
               String filePath = fileDetails.getPdfFilePath().replace(serverPath, serverFilePath);
                URLConnection openConnection = new URL(filePath).openConnection();

                InputStream inputStream = openConnection.getInputStream();
                PDDocument document = PDDocument.load(inputStream);
                PrintService myPrintService = PrintServiceLookup.lookupDefaultPrintService();
                PrinterJob pj = PrinterJob.getPrinterJob();
                if (pj.printDialog()) {
                    pj.setPageable(new PDFPageable(document));
                    pj.setPrintService(myPrintService);
                    pj.setJobName(fileName);
                    pj.print();
                    success = true;
                } else {
                    success = false;
                }
            }
            if (success) {
            	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User successfully printed the file : " + fileId);
                return ResponseEntity.ok().body(new AddFolderResponse("Printed Successfully"));
            } else {
            	eventDetailsUtil.loadEvents(userDetails.getUserName(),"User cancelled the print : " + fileId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AddFolderResponse("Error Occurred : Subscriber cancelled the print action"));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AddFolderResponse("Error Occurred : " + ex.getMessage()));
        }
    }
}
