package com.arits.docRepo.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="temp_file_details")
public class TempFileDetails {


	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="ID_TEMP_TFG", nullable=false, unique=true)
	private Long fileId;
	
	@Column(name="FILE_NAME_TFD", nullable=false)
	private String fileName;
	
	@Column(name="FILE_PATH_TFD", nullable=false)
	private String filePath;	
	
	@Column(name="INDEXING_TFD", nullable=false)
	private String indexing;	
	
	@Column(name="VERSION_COMMENT_TFD")
	private String versionComment;	
	
	@Column(name="LANGUAGE_TFD", nullable=false)
	private String language;
	
	@Column(name="TAG_TFD", nullable=false)
	private String tag;
	
	@Column(name="NEW_TAG_TFD")
	private String newTag;
	
	@Column(name="CUSTOM_ID_TFD")
	private String custId;
	
	@Column(name="COVERAGE_TFD")
	private String coverage;
	
	@Column(name="OBJECT_TFD")
	private String object;
	
	@Column(name="RECEIPIENT_TFD")
	private String receipient;
	
	@Column(name="SOURCE_TFD")
	private String source;
	
	@Column(name="AUTHOR_TFD")
	private String author;
	
	@Column(name="ORIGINAL_ID_TFD")
	private String originalId;
	
	@Column(name="TYPE_TFD")
	private String type;
	
	@Column(name="USERS_TFD", nullable=false)
	private String usersTFD;
	
	@Column(name="MESSAGES_TFD")
	private String messages;
	
	@Column(name="FILE_INDEX_TFD")
	private String fileIndex;
	
	@Column(name="FILE_LOCK_TFD", nullable=false)
	private String fileLock;
	
	@Column(name="FILE_BOOKMARK_TFD")
	private String fileBookmark;
	
	@Column(name="FOLDER_PATH_TFD", nullable=false)
	private String folderPath;
	
	@Column(name="RATING_TFD")
	private int rating;
	
	@Column(name="CREATED_ON_TFD", nullable=false)
	private String creationDate;	
	
	@Column(name="FILE_SIZE_TFD", nullable=false)
	private String fileSize;
	
	@Column(name="FILE_EXTENSION_TFD", nullable=false)
	private String fileExtension;	
	
	@Column(name="FILE_CURRENT_STATUS_TFD", nullable=false)
	private String fileCurrentStatus;

	@Column(name="USER_NAME_TFD", nullable=false)
	private String userName;
	
	@Column(name="DATE_UPLOADED_TFD", nullable=false)
	private String dateUploaded;	
	
	@Column(name="TREE_PATH_NAME_TFD", nullable=false)
	private String treePathName;
	
	@Column(name="PDF_FILE_PATH_TFD")
	private String pdfFilePath;
	
	@Column(name="ENCRYPTED_FILE_NAME_TFD", nullable=false)
	private String encryptedFileName;
	
	@Column(name="CHECK_IN_OUT_STATUS_TFD")
	private String checkInOut;
	
	@Column(name="CHECKOUT_DATE_TFD")
	private String checkOutDate;
	
	@Column(name="TEXT_FILE_NAME_TFD")
	private String textFile;
	
	@Column(name="FOLDER_ID_TFD", nullable=false)
	private Long folderId;
	
	@Column(name="USER_GROUP_TFD", nullable=false)
	private String userGroup;
	
	

	@Column(name="FILE_ID_TFD")
	private long origFileId;

	@Column(name="CURRENT_VERSION_TFD")
	private int currentVersion;

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

	public String getIndexing() {
		return indexing;
	}

	public void setIndexing(String indexing) {
		this.indexing = indexing;
	}

	public String getVersionComment() {
		return versionComment;
	}

	public void setVersionComment(String versionComment) {
		this.versionComment = versionComment;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getNewTag() {
		return newTag;
	}

	public void setNewTag(String newTag) {
		this.newTag = newTag;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getReceipient() {
		return receipient;
	}

	public void setReceipient(String receipient) {
		this.receipient = receipient;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsersTFD() {
		return usersTFD;
	}

	public void setUsersTFD(String usersTFD) {
		this.usersTFD = usersTFD;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getFileIndex() {
		return fileIndex;
	}

	public void setFileIndex(String fileIndex) {
		this.fileIndex = fileIndex;
	}

	public String getFileLock() {
		return fileLock;
	}

	public void setFileLock(String fileLock) {
		this.fileLock = fileLock;
	}

	public String getFileBookmark() {
		return fileBookmark;
	}

	public void setFileBookmark(String fileBookmark) {
		this.fileBookmark = fileBookmark;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getFileCurrentStatus() {
		return fileCurrentStatus;
	}

	public void setFileCurrentStatus(String fileCurrentStatus) {
		this.fileCurrentStatus = fileCurrentStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDateUploaded() {
		return dateUploaded;
	}

	public void setDateUploaded(String dateUploaded) {
		this.dateUploaded = dateUploaded;
	}

	public String getTreePathName() {
		return treePathName;
	}

	public void setTreePathName(String treePathName) {
		this.treePathName = treePathName;
	}

	public String getPdfFilePath() {
		return pdfFilePath;
	}

	public void setPdfFilePath(String pdfFilePath) {
		this.pdfFilePath = pdfFilePath;
	}

	public String getEncryptedFileName() {
		return encryptedFileName;
	}

	public void setEncryptedFileName(String encryptedFileName) {
		this.encryptedFileName = encryptedFileName;
	}

	public String getCheckInOut() {
		return checkInOut;
	}

	public void setCheckInOut(String checkInOut) {
		this.checkInOut = checkInOut;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getTextFile() {
		return textFile;
	}

	public void setTextFile(String textFile) {
		this.textFile = textFile;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public long getOrigFileId() {
		return origFileId;
	}

	public void setOrigFileId(long origFileId) {
		this.origFileId = origFileId;
	}

	public int getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}

	
	

	
	
	
	
}
