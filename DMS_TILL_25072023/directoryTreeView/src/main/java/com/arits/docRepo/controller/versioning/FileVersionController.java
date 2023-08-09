package com.arits.docRepo.controller.versioning;

import com.arits.docRepo.dto.FileVersionDetails;
import com.arits.docRepo.service.versioning.FileVersionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileVersionController {
    private FileVersionService fileVersionService;

    public FileVersionController(FileVersionService fileVersionService) {
        this.fileVersionService = fileVersionService;
    }

    @GetMapping("/getAllVersions")
    public List<FileVersionDetails> getBookmarkedFiles(@RequestParam("fileId") String fileId) {
        return fileVersionService.getFileVersions(fileId);
    }
}
