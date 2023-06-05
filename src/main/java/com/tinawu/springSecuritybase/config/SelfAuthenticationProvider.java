package com.tinawu.springSecuritybase.config;

import com.tinawu.springSecuritybase.dto.LoginDTO;
import com.tinawu.springSecuritybase.dto.LoginResultDTO;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.service.AuthorizationService;
import com.tinawu.springSecuritybase.service.impl.AuthorizationServiceImpl;
import com.tinawu.springSecuritybase.utils.CodecUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AuthorizationServiceImpl authorizationServiceImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("你好");
        String account = authentication.getName();     //获取用户名
        String password = (String) authentication.getCredentials();  //获取密码
        UserDetails userDetails = authorizationServiceImpl.loadUserByUsername(account);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setAccount(account);
        loginDTO.setPassword(password);
        //login/密碼確認
        LoginResultDTO login;
        try {
            login = authorizationServiceImpl.login(loginDTO);
        } catch (MessageException e) {
            throw new RuntimeException(e);
        }


        return new MyAuthentication(account, userDetails.getAuthorities(), login);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}


