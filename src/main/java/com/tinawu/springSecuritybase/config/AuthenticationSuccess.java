package com.tinawu.springSecuritybase.config;

import com.google.gson.Gson;
import com.tinawu.springSecuritybase.controller.AuthorizationController;
import com.tinawu.springSecuritybase.dto.LoginResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler {

    @Autowired
    Gson gson;
    private Logger logger = LoggerFactory.getLogger(AuthenticationSuccess.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.info("登入成功後回傳token");

        httpServletResponse.setContentType("application/json;charset=utf-8");
        LoginResultDTO loginDTO = ((MyAuthentication) authentication).getLoginDTO();
        httpServletResponse.getWriter().write(gson.toJson(loginDTO));


        // 取得原始頁面的 URL
//        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(httpServletRequest, httpServletResponse);
//        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//        if(savedRequest != null) {
//            String redirectUrl = savedRequest.getRedirectUrl();
//            logger.info("獲取原本要導向的頁面" + redirectUrl);
//
//            // 重新導向至原始頁面
//            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);
//        }else{
//            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/index");
//        }
    }
}
