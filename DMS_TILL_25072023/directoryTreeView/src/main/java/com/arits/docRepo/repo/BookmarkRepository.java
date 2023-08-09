package com.arits.docRepo.repo;

import com.arits.docRepo.dto.Bookmark;
import com.arits.docRepo.dto.FileDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookmarkRepository  extends CrudRepository<Bookmark, Long> {
    List<Bookmark> findBookmarkByUserNameEquals(String userName);

    List<Bookmark> findBookmarkByUserNameEqualsAndAndFileIdEquals(String userName,long fileId);
}
