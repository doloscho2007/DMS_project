package com.arits.docRepo.dto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "folder_details")
public class FolderDetails {


    /**
     * The id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOLDER_ID_FOD", nullable = false, unique = true)
    private Long folderId;

    @Column(name = "FOLDER_NAME_FOD", nullable = false)
    private String folderName;

    @Column(name = "FOLDER_PATH_FOD", nullable = false)
    private String folderPath;

    @Column(name = "USERNAME_FOD", nullable = false)
    private String username;

    @Column(name = "CREATION_DATE_FOD", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "FOLDER_CURRENT_STATUS_FOD", nullable = false)
    private String folderCurrentStatus;

    @Column(name = "PARENT_ID_FOD", nullable = false)
    private Long parentId;


    @Column(name = "FOLDER_DESCRIPTION_FOD", nullable = false)
    private String folderDescription;

    @Column(name = "USER_GROUP_FOD", nullable = false)
    private String userGroup;

    public Long getFolderId() {
        return folderId;
    }


    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }


    public String getFolderName() {
        return folderName;
    }


    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }


    public String getFolderPath() {
        return folderPath;
    }


    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }


    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }


    public String getFolderCurrentStatus() {
        return folderCurrentStatus;
    }


    public void setFolderCurrentStatus(String folderCurrentStatus) {
        this.folderCurrentStatus = folderCurrentStatus;
    }


    public Long getParentId() {
        return parentId;
    }


    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }


    public String getFolderDescription() {
        return folderDescription;
    }


    public void setFolderDescription(String folderDescription) {
        this.folderDescription = folderDescription;
    }


    public String getUserGroup() {
        return userGroup;
    }


    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }


    /**
     * Instantiates a new apple devices.
     */
    public FolderDetails() {
        super();

    }


}
