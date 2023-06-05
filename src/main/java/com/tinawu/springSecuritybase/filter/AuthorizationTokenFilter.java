package com.tinawu.springSecuritybase.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Value("${permitAll.url}")
    private String[] permitAllUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(httpServletRequest.getRequestURI());
        System.out.println(ArrayUtils.contains(permitAllUrl, httpServletRequest.getRequestURI()));
        if (!ArrayUtils.contains(permitAllUrl, httpServletRequest.getRequestURI())) {
            System.out.println("開始驗證token");
            String token = httpServletRequest.getHeader("token");
                //validateToken
                try {
                    tokenService.validateToken(token);
                    // 添加至上下文中
//                   SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                } catch (MessageException e) {
//                    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation failed");

                    commence(httpServletResponse, e);
                    return;
                }

//            final Integer userId = tokenService.getUserId(token);
//            final String apiPath = request.getRequestURI();
//            final String httpMethod = request.getMethod();
//            funcService.checkFuncPermission(userId, apiPath, httpMethod);//驗證失敗權限不足
                tokenService.extendTokenExpirationTime(token);//延長token過期時間
                logger.info("TOKEN IS VALID");


        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }


    public void commence(HttpServletResponse response, MessageException messageException) throws IOException {
        logger.error("异常：{}", messageException);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out;
        out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(messageException.getErrorMessage()));
        out.flush();
        out.close();
    }

}
