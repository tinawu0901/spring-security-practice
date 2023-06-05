package com.tinawu.springSecuritybase.config;

import com.tinawu.springSecuritybase.service.AuthorizationService;
import com.tinawu.springSecuritybase.service.TokenService;
import com.tinawu.springSecuritybase.utils.UserAgentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    @Autowired
    TokenService tokenService;
    @Autowired
    private AuthorizationService authorizationService;

    private Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        logger.info("退出作業");
        final String ip = UserAgentUtil.getIpAddr(httpServletRequest);
        String token = httpServletRequest.getHeader("token");
        //驗證token? filter幫你了
        System.out.println("1"+token);
        if (token == null || token.isEmpty()) {
            logger.info("token為空");
            return;
        }
        final Integer loginUserId = tokenService.getUserId(token);//先拿到token 只認token
        authorizationService.logout(loginUserId);//刪除token
    }
}
