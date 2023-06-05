package com.tinawu.springSecuritybase.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tinawu.springSecuritybase.dto.CreateUserInputDTO;
import com.tinawu.springSecuritybase.dto.LoginDTO;
import com.tinawu.springSecuritybase.dto.LoginResultDTO;
import com.tinawu.springSecuritybase.enumeration.AccountType;
import com.tinawu.springSecuritybase.enumeration.UserStatus;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.po.UserInfoPO;
import com.tinawu.springSecuritybase.po.UserPO;
import com.tinawu.springSecuritybase.repository.UserRepository;
import com.tinawu.springSecuritybase.service.AuthorizationService;
import com.tinawu.springSecuritybase.service.TokenService;
import com.tinawu.springSecuritybase.service.UserService;
import com.tinawu.springSecuritybase.utils.CodecUtil;
import com.tinawu.springSecuritybase.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.naming.AuthenticationException;
import javax.naming.Name;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class AuthorizationServiceImpl implements AuthorizationService, UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);
    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    @Value("${token.pwd.expired.effective.minutes}")
    private int tokenPwdExpiredEffectiveMinutes;
    @Value("${token.general.effective.minutes}")
    private int tokenGeneralEffectiveMinutes;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;



    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserPO userPO = userRepository.findByUserAccountAndStatusNot(username, UserStatus.DELETED.getStatus());//找帳號
        if (ObjectUtils.isEmpty(userPO)) {//帳號不存在
            // AUTH01:帳號不存在
            logger.info(String.format("登入失敗，帳號(%s)不存在", username));
            throw new UsernameNotFoundException( "登入失敗，帳號(%s)不存在");
        }
        UserDetails userDetails = User.builder()
                .username(userPO.getAccount())
                .password("{noop}" + userPO.getPassword())
                .roles("users").build();
        return userDetails;
    }

    @Override //傳入帳號密碼
    public LoginResultDTO login(final LoginDTO loginDTO) throws MessageException {
        final LoginResultDTO loginResultDTO = new LoginResultDTO();//登入結果
        if (!ObjectUtils.isEmpty(loginDTO.getAccount()) && !ObjectUtils.isEmpty(loginDTO.getPassword())) {//如果不為空
            final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());//現在系統時間
            final String account = loginDTO.getAccount();
            final String password = loginDTO.getPassword();
            final UserPO userPO = userRepository.findByUserAccountAndStatusNot(loginDTO.getAccount(), UserStatus.DELETED.getStatus());//找帳號
            if (ObjectUtils.isEmpty(userPO)) {//帳號不存在
                // AUTH01:帳號不存在
                logger.info(String.format("登入失敗，帳號(%s)不存在", account));
                throw new MessageException("AUTH05", "登入失敗，帳號(%s)不存在");
            } else {//帳號存在
                final Integer userId = userPO.getUserId();//返回帳號ID
                final UserInfoPO userInfoPO = userPO.getUserInfoPO();
//                if (AccountType.GOOGLE_SSO.getType().equals(userInfoPO.getAccountType())) {
//                    logger.info(String.format("google登入成功{}", account));
//                }else{
                    // 密碼加密
                    String pwdEncode = CodecUtil.getSHA256Str(password);//使用者輸入的密碼
                    final boolean pwdCorrect = pwdEncode.equals(userPO.getPassword());//加密過的密碼 對想 資料庫以加密的密碼	//相等
                    if (!pwdCorrect) {
                        // 密碼錯誤處理
                        throw new MessageException("AUTH05", "密碼錯誤");
                    }
                    if ( !ObjectUtils.isEmpty(userPO.getPwdExpiration()) && systemCurrentTime.after(userPO.getPwdExpiration())) {
                        // AUTH04:密碼過期
                        logger.info(String.format("帳號(%s)密碼已過期", account));
                        // 將密碼錯誤次數歸零，刪除所有TOKEN並產生一個n分鐘過期的TOKEN
                        userPO.setWrongPwdCount(0);
                        userRepository.save(userPO);
                        tokenService.deleteToken(userId);
                        //產生token
                        final String token = tokenService.generateToken(userId, userInfoPO.getUserName(), systemCurrentTime, DateUtil.addMinute(systemCurrentTime, tokenPwdExpiredEffectiveMinutes));
                        loginResultDTO.setToken(token);
                        loginResultDTO.setUserName(userInfoPO.getUserName());
                        loginResultDTO.setAccountType(userInfoPO.getAccountType());
                        loginResultDTO.setPwdExpiration(userPO.getPwdExpiration());
                        throw new MessageException("AUTH05", "帳號(%s)密碼已過期");
                    }

//            }
                userPO.setLastLogin(systemCurrentTime);//上次登入時間
                userRepository.save(userPO);
                //產生token
                System.out.println("產生Token");
                final String token = tokenService.generateToken(userId, userInfoPO.getUserName(), systemCurrentTime, DateUtil.addMinute(systemCurrentTime, tokenGeneralEffectiveMinutes));
                loginResultDTO.setToken(token);
                loginResultDTO.setUserName(userInfoPO.getUserName());
                loginResultDTO.setAccountType(userInfoPO.getAccountType());
                loginResultDTO.setPwdExpiration(userPO.getPwdExpiration());

            }
        }
        return loginResultDTO;
    }

    @Override
    public LoginResultDTO loginSSO(LoginDTO loginDTO) {
        final LoginResultDTO loginResultDTO = new LoginResultDTO();//登入結果
            final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());//現在系統時間
            final String account = loginDTO.getAccount();
            final String password = loginDTO.getPassword();
            final UserPO userPO = userRepository.findByUserAccountAndStatusNot(loginDTO.getAccount(), UserStatus.DELETED.getStatus());//找帳號
            if (ObjectUtils.isEmpty(userPO)) {//帳號不存在
                // AUTH01:帳號不存在
                logger.info(String.format("登入失敗，帳號(%s)不存在", account));
            } else {//帳號存在
                final Integer userId = userPO.getUserId();//返回帳號ID
                final UserInfoPO userInfoPO = userPO.getUserInfoPO();
                if (AccountType.GOOGLE_SSO.getType().equals(userInfoPO.getAccountType())) {
                    logger.info(String.format("google登入成功{}", account));
                }
                userPO.setLastLogin(systemCurrentTime);//上次登入時間
                userRepository.save(userPO);
                //產生token
                System.out.println("產生Token");
                final String token = tokenService.generateToken(userId, userInfoPO.getUserName(), systemCurrentTime, DateUtil.addMinute(systemCurrentTime, tokenGeneralEffectiveMinutes));
                loginResultDTO.setToken(token);
                loginResultDTO.setUserName(userInfoPO.getUserName());
                loginResultDTO.setAccountType(userInfoPO.getAccountType());
                loginResultDTO.setPwdExpiration(userPO.getPwdExpiration());

            }

        return loginResultDTO;
    }


    @Override
    public void logout(final Integer userId) {
        tokenService.deleteToken(userId);
    }

    @Override
    public void processGoogleRecaptcha(final String recaptchaResponse, final String ip) throws MessageException, JsonProcessingException {
//        if (!responseSanityCheck(recaptchaResponse)) {
//            logger.error("Recaptcha Response ERROR");
//            throw new AuthProException("AUTH14", messageUtil.get(MessageCode.ERROR_CODE_MESSAGE_AUTH14));
//        }
//        URI verifyUri = URI.create(String.format(ProjectConstant.GOOGLE_RECAPTCHA_API_URL, googleRecaptchaKeySecret, recaptchaResponse, ip));
//        final RestTemplate restTemplate = new RestTemplate();
//        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
//        if (!googleResponse.isSuccess()) {
//            final ObjectMapper objectMapper = new ObjectMapper();
//            logger.error(String.format("Google Recaptcha 驗證失敗(%s)", objectMapper.writeValueAsString(googleResponse.getErrorCodes())));
//            throw new AuthProException("AUTH15", messageUtil.get(MessageCode.ERROR_CODE_MESSAGE_AUTH15));
//        }
    }

    private boolean responseSanityCheck(final String recaptchaResponse) {
        return StringUtils.hasLength(recaptchaResponse) && RESPONSE_PATTERN.matcher(recaptchaResponse).matches();
    }


}
