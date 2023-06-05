package com.tinawu.springSecuritybase.dto;

public class CreateUserInputDTO extends UserInfoDTO {
    private static final long serialVersionUID = -2617555299473680611L;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
