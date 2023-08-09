package com.arits.docRepo.dto;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "file_details")
public class FileDetails {


    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID_FD", nullable = false, unique = true)
    private Long fileId;

    @Column(name = "FILE_NAME_FD", nullable = false)
    private String fileName;

    @Column(name = "FILE_PATH_FD", nullable = false)
    private String filePath;

    @Column(name = "TREE_PATH_NAME", nullable = false)
    private String treePath;

    @Column(name = "PDF_FILE_PATH_FD", nullable = false)
    private String pdfFilePath;

    @Column(name = "FILE_CURRENT_STATUS_FD", nullable = true)
    private String fileCurrentStatus;

    @Column(name = "TEXT_FILE_NAME_FD", nullable = false)
    private String textFileName;

    @Column(name = "DATE_UPLOADED_FD", nullable = true)
    private Date createdAt;

    @Column(name = "CUSTOM_ID_FD", nullable = true)
    private String cprId;

    @Column(name = "COVERAGE_FD", nullable = true)
    private String staffId;

    @Column(name = "FILE_SEARCH1_FD", nullable = true)
    private String fileSearch1;

    @Column(name = "FILE_SEARCH2_FD", nullable = true)
    private String fileSearch2;

    @Column(name = "FILE_SEARCH3_FD", nullable = true)
    private String fileSearch3;

    @Column(name = "FILE_CURRENT_VERSION", nullable = false)
    private String currentVersion;

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getFileSearch1() {
        return fileSearch1;
    }


    public void setFileSearch1(String fileSearch1) {
        this.fileSearch1 = fileSearch1;
    }


    public String getFileSearch2() {
        return fileSearch2;
    }


    public void setFileSearch2(String fileSearch2) {
        this.fileSearch2 = fileSearch2;
    }


    public String getFileSearch3() {
        return fileSearch3;
    }


    public void setFileSearch3(String fileSearch3) {
        this.fileSearch3 = fileSearch3;
    }

    @Column(name = "OBJECT_FD", nullable = true)
    private String staffCode;

    @Column(name = "ENCRYPTED_FILE_NAME_FD", nullable = true)
    private String encryptedFileName;


    @Column(name = "INDEXING_FD", nullable = true)
    private String indexingFd;

    @Column(name = "LANGUAGE_FD", nullable = true)
    private String languageFd;

    @Column(name = "TAG_FD", nullable = true)
    private String tagFd;

    @Column(name = "USERS_FD", nullable = true)
    private String usersFd;

    @Column(name = "FILE_LOCK_FD", nullable = true)
    private String fileLockFd;

    @Column(name = "FOLDER_PATH_FD", nullable = true)
    private String folderPathFd;

    @Column(name = "RATING_FD", nullable = true)
    private String ratingFd;

    @Column(name = "CREATED_ON_FD", nullable = true)
    private String createdOn;

    @Column(name = "CHECKOUT_DATE_FD", nullable = true)
    private Date checkoutDate;

    @Column(name = "FILE_SIZE_FD", nullable = true)
    private String fileSizeFd;

    @Column(name = "USER_NAME_FD", nullable = true)
    private String userNameFd;

    @Column(name = "CHECK_IN_OUT_STATUS_FD", nullable = true)
    private String checkInStatus;

    @Column(name = "USER_GROUP_FD", nullable = true)
    private String userGroup;


    public String getIndexingFd() {
        return indexingFd;
    }


    public void setIndexingFd(String indexingFd) {
        this.indexingFd = indexingFd;
    }


    public String getLanguageFd() {
        return languageFd;
    }


    public void setLanguageFd(String languageFd) {
        this.languageFd = languageFd;
    }


    public String getTagFd() {
        return tagFd;
    }


    public void setTagFd(String tagFd) {
        this.tagFd = tagFd;
    }


    public String getUsersFd() {
        return usersFd;
    }


    public void setUsersFd(String usersFd) {
        this.usersFd = usersFd;
    }


    public String getFileLockFd() {
        return fileLockFd;
    }


    public void setFileLockFd(String fileLockFd) {
        this.fileLockFd = fileLockFd;
    }


    public String getFolderPathFd() {
        return folderPathFd;
    }


    public void setFolderPathFd(String folderPathFd) {
        this.folderPathFd = folderPathFd;
    }


    public String getRatingFd() {
        return ratingFd;
    }


    public void setRatingFd(String ratingFd) {
        this.ratingFd = ratingFd;
    }


    public String getCreatedOn() {
        return createdOn;
    }


    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }


    public Date getCheckoutDate() {
        return checkoutDate;
    }


    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }


    public String getFileSizeFd() {
        return fileSizeFd;
    }


    public void setFileSizeFd(String fileSizeFd) {
        this.fileSizeFd = fileSizeFd;
    }


    public String getUserNameFd() {
        return userNameFd;
    }


    public void setUserNameFd(String userNameFd) {
        this.userNameFd = userNameFd;
    }


    public String getCheckInStatus() {
        return checkInStatus;
    }


    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }


    public String getUserGroup() {
        return userGroup;
    }


    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }


    public String getEncryptedFileName() {
        return encryptedFileName;
    }


    public void setEncryptedFileName(String encryptedFileName) {
        this.encryptedFileName = encryptedFileName;
    }


    public String getTreePath() {
        return treePath;
    }


    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }


    public Date getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;

    }


    public String getCprId() {
        return cprId;
    }


    public void setCprId(String cprId) {
        this.cprId = cprId;
    }


    public String getStaffId() {
        return staffId;
    }


    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }


    public String getStaffCode() {
        return staffCode;
    }


    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }


    public String getTextFileName() {
        return textFileName;
    }


    public void setTextFileName(String textFileName) {
        this.textFileName = textFileName;
    }


    public String getFileCurrentStatus() {
        return fileCurrentStatus;
    }


    public void setFileCurrentStatus(String fileCurrentStatus) {
        this.fileCurrentStatus = fileCurrentStatus;
    }


    public String getPdfFilePath() {
        return pdfFilePath;
    }


    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }


    @Column(name = "FOLDER_ID_FD", nullable = false)
    private Long folderId;

    @Column(name = "FILE_EXTENSION_FD")
    private String fileExtension;

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
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


    public String getFilePath() {
        return filePath;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public Long getFolderId() {
        return folderId;
    }


    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }


    /**
     * Instantiates a new apple devices.
     */
    public FileDetails() {
        super();

    }


}
