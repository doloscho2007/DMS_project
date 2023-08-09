package com.arits.docRepo.repo;

import com.arits.docRepo.dto.CheckOutFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CheckoutRepository extends CrudRepository<CheckOutFile, Long> {
    List<CheckOutFile> findCheckOutFileByUserNameEquals(String userName);

    List<CheckOutFile> findCheckOutFileByUserNameEqualsAndFileDetails_FileId(String userName, long fileId);

    boolean existsByFileDetails_FileId(long fileId);
}
