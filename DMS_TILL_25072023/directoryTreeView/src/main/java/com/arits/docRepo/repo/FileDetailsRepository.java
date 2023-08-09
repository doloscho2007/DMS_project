package com.arits.docRepo.repo;

import com.arits.docRepo.dto.FileDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileDetailsRepository extends CrudRepository<FileDetails, Integer> {

    FileDetails getByFileId(Long fileId);

    List<FileDetails> findByFolderIdEqualsOrderByFileIdAsc(Long folderId);

    List<FileDetails> findByFileIdEquals(Long fileId);

    List<FileDetails> findByFileIdIn(List<Long> fileIdList);

    List<FileDetails> findByTextFileNameContainsAndFileCurrentStatusEqualsOrderByFileIdAsc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndTextFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByTextFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByTextFileNameEqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileNameContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndFileNameEqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileNameEqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByFolderIdEqualsAndFileCurrentStatusOrderByFileIdAsc(long parseLong, String status);

    List<FileDetails> findByFolderIdEqualsAndFileCurrentStatusOrderByCreatedAtDesc(long parseLong, String status);

    List<FileDetails> findByUserGroupInAndFileSearch1ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileSearch1ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndFileSearch1EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileSearch1EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndFileSearch2ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileSearch2ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndFileSearch2EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileSearch2EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndFileSearch3ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileSearch3ContainsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);

    List<FileDetails> findByUserGroupInAndFileSearch3EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(List<String> userGroup, String textFileName, String status);

    List<FileDetails> findByFileSearch3EqualsAndFileCurrentStatusEqualsOrderByCreatedAtDesc(String textFileName, String status);
}
