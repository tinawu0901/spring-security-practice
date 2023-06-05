package com.tinawu.springSecuritybase.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.tinawu.springSecuritybase.dto.LoginDTO;
import com.tinawu.springSecuritybase.dto.LoginResultDTO;
import com.tinawu.springSecuritybase.exception.MessageException;

import java.util.List;
import java.util.Map;

public interface AuthorizationService {
    LoginResultDTO login(final LoginDTO loginDTO) throws MessageException;

    LoginResultDTO loginSSO(final LoginDTO loginDTO);


//    LoginResultDTO loginSSO(final Map<String, List<Object>> ssoAttributes, final String ip, final String device) throws MessageException;

    void logout(final Integer userId);
    
    void processGoogleRecaptcha(final String recaptchaResponse, final String ip) throws MessageException, JsonProcessingException;
}
