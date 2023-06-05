package com.tinawu.springSecuritybase.config;

import com.tinawu.springSecuritybase.service.AuthorizationService;
import com.tinawu.springSecuritybase.service.TokenService;
import com.tinawu.springSecuritybase.utils.UserAgentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    @Autowired
    TokenService tokenService;
    @Autowired
    private AuthorizationService authorizationService;
    private Logger logger = LoggerFactory.getLogger(LogoutSuccessHandler.class);
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.info("退出作業");
//        final String ip = UserAgentUtil.getIpAddr(httpServletRequest);
//        final Integer loginUserId = tokenService.getUserId(token);//先拿到token 只認token
//        authorizationService.logout(loginUserId);//刪除token

    }
}
