package com.tinawu.springSecuritybase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.tinawu.springSecuritybase.dto.LoginDTO;
import com.tinawu.springSecuritybase.dto.LoginResultDTO;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.service.AuthorizationService;
import com.tinawu.springSecuritybase.service.TokenService;
import com.tinawu.springSecuritybase.utils.CacheMap;
import com.tinawu.springSecuritybase.utils.UserAgentUtil;
//import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
public class AuthorizationController {
    private Logger logger = LoggerFactory.getLogger(AuthorizationController.class);


    //    @Autowired
    //    private OAuth2LoginAuthenticationProvider authenticationProvider;
//    @Value("${saml2.sso.enable:false}")
//    private boolean saml2SSOEnable;
//    @Value("${enable.google.recaptcha:false}")
//    private boolean enableGoogleRecaptcha;
//    @Value("${sso.redirect.web.uel}")
//    private String SSO_REDIRECT_WEB_UEL;//http://192.168.56.86/ssoCallback
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/auth/oauth2/authorization/google")
    public String google() {
        return "redirect:/oauth2/authorization/google";
    }

//    @GetMapping("/login/oauth2/code/google")
//    public String handleGoogleOAuth2Callback(HttpServletRequest request, Model model) throws Exception {
//        Authentication authentication = authenticationProvider.authenticate(new ServletOAuth2AuthorizedClientExchangeFilterFunction.OAuth2LoginAuthenticationToken(request));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return "redirect:/home"; // 替换成你的登录成功后的页面
//    }
//    @Autowired
//    private MessageUtil messageUtil;
//
//    CacheMap<Object, Object> CHECK_SSO_STATE_MAP = CacheMap.getDefault();
    //登入
//    @PostMapping(value = "login")//userAccount  // password
//    public LoginResultDTO login(@RequestBody final LoginDTO loginDTO, HttpServletRequest request) throws MessageException, JsonProcessingException {
//        logger.info("Login");
//        final String ip = UserAgentUtil.getIpAddr(request);//取的真實IP
//        final String device = UserAgentUtil.composeDeviceContent(request.getHeader("User-Agent"));//User-Agent
////        if (enableGoogleRecaptcha) {//是否開啟googleRecaptcha
////            final String recaptchaResponse = request.getHeader("recaptchaToken");
////            authorizationService.processGoogleRecaptcha(recaptchaResponse, ip);
////        }
//        final LoginResultDTO loginResultDTO = authorizationService.login(loginDTO);
//        return loginResultDTO;
//    }

//    @PostMapping(value = "logout")//登出	登出也要拿到tocken才能登出
//    public void logout(@RequestHeader(value = "TOKEN") final String token, HttpServletRequest request) {
//    	logger.info("Logout");
//        final String ip = UserAgentUtil.getIpAddr(request);
//        final Integer loginUserId = tokenService.getUserId(token);//先拿到token 只認token
//        authorizationService.logout(loginUserId);//刪除token
////        if(saml2SSOEnable) {
////        	SecurityContextHolder.clearContext();
////        }
//    }

    
//    @GetMapping(value = { "/", "SSOLogin" })
//    public RedirectView redirectView() {
//    	logger.info("SSOLogin");
//    	String randomState = UUID.randomUUID().toString().split("\\-")[0];
//
//    	Saml2Authentication authentication = (Saml2Authentication) SecurityContextHolder.getContext().getAuthentication();
//        Saml2AuthenticatedPrincipal principle = (Saml2AuthenticatedPrincipal) authentication.getPrincipal();
//        Map<String, List<Object>> ssoAttributes = principle.getAttributes();
//
//    	CHECK_SSO_STATE_MAP.put(randomState, ssoAttributes);
//
//    	//要到資料後馬上清除Security 狀態
//    	SecurityContextHolder.clearContext();
//
//		return new RedirectView(SSO_REDIRECT_WEB_UEL+"?state="+randomState);
//	}
//
//    @GetMapping(value = "SSOLogin/token")
//	public LoginResultDTO SSOLoginToken(@RequestParam(value = "state") final String state, HttpServletRequest request) throws AuthProException {
//    	LoginResultDTO loginResultDTO = new LoginResultDTO();
//    	logger.info("SSOLogin get token");
//    	Authentication authentication2 =  SecurityContextHolder.getContext().getAuthentication();
////    	String authenticationType = (String) authentication.getAuthorities().;
//    	//authenticationType == "ROLE_ANONYMOUS"
//
//    	logger.info("#########: "+CHECK_SSO_STATE_MAP.get(state));
//    	if(CHECK_SSO_STATE_MAP.get(state) == null) {
//    		logger.info(String.format("SSO Login 取得 token時效過期"));
//            throw new AuthProException("AUTH16", messageUtil.get(MessageCode.ERROR_CODE_MESSAGE_AUTH16));
//		}
//		final String ip = UserAgentUtil.getIpAddr(request);
//        final String device = UserAgentUtil.composeDeviceContent(request.getHeader("User-Agent"));
//
//        Map<String, List<Object>> ssoAttributes = (Map<String, List<Object>>) CHECK_SSO_STATE_MAP.get(state);
//		String uesrId = (String) ssoAttributes.get("guid").get(0);
//
//    	loginResultDTO = authorizationService.loginSSO(ssoAttributes, ip, device);
//    	return loginResultDTO;
//	}
    

}
