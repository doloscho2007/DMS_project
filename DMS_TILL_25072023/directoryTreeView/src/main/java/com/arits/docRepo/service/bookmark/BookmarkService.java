package com.arits.docRepo.service.bookmark;

import com.arits.docRepo.dto.Bookmark;
import com.arits.docRepo.dto.FileDetails;
import com.arits.docRepo.model.UserDetails;
import com.arits.docRepo.repo.BookmarkRepository;
import com.arits.docRepo.repo.FileDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private BookmarkRepository bookmarkRepository;
    private FileDetailsRepository fileDetailsRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, FileDetailsRepository fileDetailsRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.fileDetailsRepository = fileDetailsRepository;
    }

    public void addBookmark(UserDetails userDetails, String fileId) {
        List<Bookmark> userBookmarks = bookmarkRepository.findBookmarkByUserNameEquals(userDetails.getUserName());
        if (userBookmarks.stream().filter(bookmark -> bookmark.getFileId() == Long.parseLong(fileId))
                .collect(Collectors.toList()).size() > 0)
            throw new RuntimeException("Already Bookmarked");
        List<FileDetails> fileDetails = fileDetailsRepository.findByFileIdEquals(Long.parseLong(fileId));
        if (fileDetails.size() == 1) {
            FileDetails file = fileDetails.get(0);
            Bookmark bookmark = new Bookmark();
            bookmark.setFileId(file.getFileId());
            bookmark.setFileName(file.getFileName());
            bookmark.setFileExtension(file.getFileExtension());
            bookmark.setUserName(userDetails.getUserName());
            bookmark.setBookmarkDate(new Date());
            bookmarkRepository.save(bookmark);
        } else {
            throw new RuntimeException("Unable to find matching file in database");
        }
    }

    public List<FileDetails> getBookmarkedFiles(UserDetails userDetails) {
        List<Bookmark> userBookmarks = bookmarkRepository.findBookmarkByUserNameEquals(userDetails.getUserName());
        List<FileDetails> fileDetails = new ArrayList<>();
        userBookmarks.stream().forEach(bookmark -> {
            fileDetails.add(fileDetailsRepository.getByFileId(bookmark.getFileId()));
        });
        return fileDetails;
    }

    public void deleteBookmark(UserDetails userDetails, String fileId) {
        try {
            Bookmark bookmark = bookmarkRepository.findBookmarkByUserNameEqualsAndAndFileIdEquals(userDetails.getUserName(),
                    Long.parseLong(fileId)).get(0);
            bookmarkRepository.delete(bookmark);
        } catch (Exception e) {
            throw new RuntimeException("No Bookmark Present");
        }
    }

    public boolean getBookmarkStatus(UserDetails userDetails, String fileId) {
        List<Bookmark> bookmark = bookmarkRepository.findBookmarkByUserNameEqualsAndAndFileIdEquals(userDetails.getUserName(),
                Long.parseLong(fileId));
        if (bookmark.size()>0) {
            return true;
        }
        return false;
    }
}
