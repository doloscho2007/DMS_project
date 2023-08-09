package com.arits.docRepo.model;


public class AddFolderRequest {

    private String parentId; // node id
    private String folderName; // parent id
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
  
    
}
