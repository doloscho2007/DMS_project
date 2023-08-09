package com.arits.docRepo.dto;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookmark")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID_BM", nullable = false, unique = true)
    private Long bookmarkId;

    @Column(name = "FILE_ID_BM", nullable = false)
    private Long fileId;

    @Column(name = "FILE_NAME_BM")
    private String fileName;

    @Column(name = "FILE_EXTN_BM")
    private String fileExtension;

    @Column(name = "USER_NAME_BM")
    private String userName;

    @Column(name = "DATE_BOOKMARK_BM")
    private Date bookmarkDate;

    public Long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(Long bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(Date bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }
}
