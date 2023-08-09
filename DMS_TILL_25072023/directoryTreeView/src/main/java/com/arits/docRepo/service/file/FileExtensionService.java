package com.arits.docRepo.service.file;

import com.arits.docRepo.dto.FileExtestion;
import com.arits.docRepo.repo.FileExtensionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileExtensionService {
    private FileExtensionRepository fileExtensionRepository;

    public FileExtensionService(FileExtensionRepository fileExtensionRepository) {
        this.fileExtensionRepository = fileExtensionRepository;
    }

    public String getContentType(String fileName) {
        // TODO Auto-generated method stub

        String extension = "";
        String contentType = "";

        if (fileName.contains("."))
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (extension == null || extension.equals("")) {
            extension = "pdf";
        }
        List<FileExtestion> fileExtList = fileExtensionRepository.findByFileExtEquals(extension);
        if (fileExtList.size() > 0) {
            FileExtestion fileExtestion = fileExtList.get(0);
            contentType = fileExtestion.getContentType();
        }
        return contentType;
    }
}
