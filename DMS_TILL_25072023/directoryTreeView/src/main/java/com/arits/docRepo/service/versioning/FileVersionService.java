package com.arits.docRepo.service.versioning;

import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.dto.FileVersionDetails;
import com.arits.docRepo.repo.FileVersionRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class FileVersionService {
    private FileVersionRepository fileVersionRepository;

    public FileVersionService(FileVersionRepository fileVersionRepository) {
        this.fileVersionRepository = fileVersionRepository;
    }

    public List<FileVersionDetails> getFileVersions(String fileId) {
        return fileVersionRepository.findFileVersionDetailsByFileDetails_FileIdOrderByFileVersionDesc
                (Long.parseLong(fileId));
    }

    public void addVersionDetailsForPdf(FileDetails fileDetails, String versionComment, String userName) {
        FileVersionDetails fileVersionDetails = new FileVersionDetails();
        fileVersionDetails.setFileDetails(fileDetails);
        fileVersionDetails.setFileVersion(Integer.parseInt(fileDetails.getCurrentVersion()));
        fileVersionDetails.setFilePath(fileDetails.getFilePath());
        fileVersionDetails.setVersionComment(versionComment);
        fileVersionDetails.setUserName(userName);
        fileVersionDetails.setCreatedAt(Calendar.getInstance());
        fileVersionRepository.save(fileVersionDetails);
    }
}
