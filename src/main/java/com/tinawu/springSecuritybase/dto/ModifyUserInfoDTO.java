package com.tinawu.springSecuritybase.dto;

import java.io.Serializable;

public class ModifyUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 2497305362328994818L;
    private String userName;
    private String email;
    private String telephone;
    private String mobile;
    private String organization;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
