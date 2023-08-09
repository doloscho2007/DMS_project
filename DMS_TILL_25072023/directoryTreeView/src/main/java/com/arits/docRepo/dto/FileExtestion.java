package com.arits.docRepo.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="file_extension")
public class FileExtestion {


	/** The id. */
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id", nullable=false, unique=true)
	private Long id;
	
	@Column(name="file_extension", nullable=false)
	private String fileExt;
	
	@Column(name="content_type", nullable=false)
	private String contentType;	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFileExt() {
		return fileExt;
	}


	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	/**
	 * Instantiates a new apple devices.
	 */
	public FileExtestion() {
		super();
		
	}


	
	
}
