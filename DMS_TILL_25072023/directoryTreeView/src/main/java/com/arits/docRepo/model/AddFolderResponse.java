package com.arits.docRepo.model;


public class AddFolderResponse {

    private String message; // node id

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param message
	 */
	public AddFolderResponse(String message) {
		super();
		this.message = message;
	}
  
  
    
}
