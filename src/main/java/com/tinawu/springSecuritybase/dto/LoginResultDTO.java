package com.tinawu.springSecuritybase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Timestamp;

public class LoginResultDTO implements Serializable {
    private static final long serialVersionUID = -3274766490215444968L;
    private String token;
    private String userName;
    private String accountType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp pwdExpiration;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Timestamp getPwdExpiration() {
        return pwdExpiration;
    }

    public void setPwdExpiration(Timestamp pwdExpiration) {
        this.pwdExpiration = pwdExpiration;
    }
}
