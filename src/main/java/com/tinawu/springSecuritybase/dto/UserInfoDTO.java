package com.tinawu.springSecuritybase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class UserInfoDTO implements Serializable {
	private static final long serialVersionUID = -6096435937780040226L;
	private String userId;
	private String account;
	private String userName;
	private String status;
	private String accountType;
	private String email;
	private String telephone;
	private String mobile;
	private String organization;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp lastModified;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp pwdExpiration;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp lastLogin;
	private List<String> roleIdList;
	private List<String> groupIdList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public Timestamp getPwdExpiration() {
		return pwdExpiration;
	}

	public void setPwdExpiration(Timestamp pwdExpiration) {
		this.pwdExpiration = pwdExpiration;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public List<String> getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List<String> groupIdList) {
		this.groupIdList = groupIdList;
	}
}
