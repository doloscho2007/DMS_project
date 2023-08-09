package com.arits.docRepo.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="login_registration")
public class LoginRegistration {


	/** The id. */
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="ID_LOGIN_REG", nullable=false, unique=true)
	private Long loginId;
	
	@Column(name="PASSCODE_LOGIN_REG", nullable=false)
	private String passCode;
	
	@Column(name="RETYPE_PWD_LOGIN_REG", nullable=false)
	private String confPassCode;
	
	public String getConfPassCode() {
		return confPassCode;
	}



	public void setConfPassCode(String confPassCode) {
		this.confPassCode = confPassCode;
	}



	@Column(name="USER_NAME_LOGIN_REG", nullable=false)
	private String userName;	
	
	@Column(name="USER_GROUP_LOGIN_REG", nullable=false)
	private String userGroup;
	
	@Column(name="EMP_CURRENT_STATUS_REG", nullable=false)
	private String empcurrentStatus;
	
	@Column(name="SYSTEMIP_LOGIN_REG", nullable=true)
	private String systemIp;
	
	
	
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



	public LoginRegistration() {
		super();
		
	}


	
	
}
