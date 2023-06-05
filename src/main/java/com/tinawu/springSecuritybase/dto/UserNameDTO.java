package com.tinawu.springSecuritybase.dto;

import java.io.Serializable;

public class UserNameDTO implements Serializable {
	private static final long serialVersionUID = -7418124051655590898L;
	private String userId;
	private String account;
	private String userName;

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
}
