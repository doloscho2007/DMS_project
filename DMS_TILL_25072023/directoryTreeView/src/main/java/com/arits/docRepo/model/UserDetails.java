package com.arits.docRepo.model;

public class UserDetails {


	
	private Long loginId;
	
	private String passCode;
	
	private String userName;	
	private String userGroup;
	private String empcurrentStatus;
	private String systemIp;
	
	private String token;
	
	
	
	
	public Long getLoginId() {
		return loginId;
	}



	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}



	public String getPassCode() {
		return passCode;
	}



	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserGroup() {
		return userGroup;
	}



	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}



	public String getEmpcurrentStatus() {
		return empcurrentStatus;
	}



	public void setEmpcurrentStatus(String empcurrentStatus) {
		this.empcurrentStatus = empcurrentStatus;
	}



	public String getSystemIp() {
		return systemIp;
	}



	public void setSystemIp(String systemIp) {
		this.systemIp = systemIp;
	}



	public UserDetails() {
		super();
		
	}



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}


	
	
}
