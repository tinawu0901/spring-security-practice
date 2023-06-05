package com.tinawu.springSecuritybase.config;

import com.tinawu.springSecuritybase.dto.LoginDTO;
import com.tinawu.springSecuritybase.dto.LoginResultDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyAuthentication implements Authentication {

    private String name;
    private Object credentials;
    private LoginResultDTO loginDTO;

    public MyAuthentication(String name, Object credentials, LoginResultDTO  loginDTO) {
        this.name = name;
        this.credentials = credentials;
        this.loginDTO = loginDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return name;
    }

    public LoginResultDTO getLoginDTO() {
        return loginDTO;
    }


}
