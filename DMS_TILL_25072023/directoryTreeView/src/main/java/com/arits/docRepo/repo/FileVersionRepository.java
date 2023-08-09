package com.arits.docRepo.repo;

import com.arits.docRepo.dto.FileVersionDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileVersionRepository extends CrudRepository<FileVersionDetails, Integer> {
    List<FileVersionDetails> findFileVersionDetailsByFileDetails_FileIdOrderByFileVersionDesc(long fileId);
}
