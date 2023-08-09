package com.arits.docRepo.repo;

import com.arits.docRepo.dto.FolderDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FolderDetailsRepository extends CrudRepository<FolderDetails, Integer> {

    List<FolderDetails> findByParentIdEquals(Long parentId);

    List<FolderDetails> findByFolderIdEquals(Long folderId);

    List<FolderDetails> findByUserGroupInAndFolderCurrentStatusEqualsOrderByFolderIdAsc(List<String> userGroup, String status);

    List<FolderDetails> findByFolderCurrentStatusEqualsOrderByFolderIdAsc(String status);

    List<FolderDetails> findByUserGroupInAndFolderCurrentStatusEqualsOrderByFolderNameAsc(List<String> userGroup, String status);

    List<FolderDetails> findByFolderCurrentStatusEqualsOrderByFolderNameAsc(String status);


    List<FolderDetails> findByFolderPathContainsAndFolderCurrentStatusEqualsOrderByFolderNameAsc
            (String folderPath, String Status);

    List<FolderDetails> findByFolderPathContainsAndUserGroupInAndFolderCurrentStatusEqualsOrderByFolderNameAsc
            (String folderPath, List<String> userGroup, String Status);

    List<FolderDetails> getAllByFolderCurrentStatusEqualsOrderByFolderPathAsc(String status);

    List<FolderDetails> getAllByUserGroupInAndFolderCurrentStatusEqualsOrderByFolderPathAsc
            (List<String> userGroup, String status);
}
