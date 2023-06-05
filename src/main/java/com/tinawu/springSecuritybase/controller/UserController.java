package com.tinawu.springSecuritybase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tinawu.springSecuritybase.dto.*;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.po.UserInfoPO;
import com.tinawu.springSecuritybase.service.TokenService;
import com.tinawu.springSecuritybase.service.UserService;
import com.tinawu.springSecuritybase.utils.UserAgentUtil;
//import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

//    @Autowired
//    private MessageUtil messageUtil;

    @PostMapping
    public void createUser(@RequestHeader(value = "TOKEN") final String token, @RequestBody final CreateUserInputDTO createUserInputDTO, HttpServletRequest request) throws MessageException, JsonProcessingException {
        logger.info("新增使用者");
        userService.createUser(createUserInputDTO);

    }

    @GetMapping(value = "name/list")
    public GetUserNameListResultDTO getUserNameList() {
        logger.info("取得使用者名稱列表");
        final GetUserNameListResultDTO getUserNameListResultDTO = userService.getUserNameList();
        return getUserNameListResultDTO;
    }

    @GetMapping(value = "list")
    public GetUserListResultDTO getUserList() {
        logger.info("取得使用者列表");
        final GetUserListResultDTO getUserListResultDTO = userService.getUserList();
        return getUserListResultDTO;
    }

    @GetMapping(value = "oneself")
    public UserInfoDTO getOneselfUserInfo(@RequestHeader(value = "TOKEN") final String token) throws MessageException {
        logger.info("取得個人資訊");
        final Integer loginUserId = tokenService.getUserId(token);//透過token取得userID
        final UserInfoDTO userInfoDTO = userService.getOneselfUserInfo(loginUserId);//透過userID取得UserInfo
        return userInfoDTO;
    }

    @PutMapping(value = "oneself")
    public void updateOneselfUserInfo(@RequestHeader(value = "TOKEN") final String token, @RequestBody final ModifyUserInfoDTO modifyUserInfoDTO, HttpServletRequest request) throws MessageException {
        logger.info("編輯個人資訊");
        final Integer loginUserId = tokenService.getUserId(token);
        userService.updateOneselfUserInfo(loginUserId, modifyUserInfoDTO);
    }

    @PutMapping
    public void updateUserInfo(@RequestHeader(value = "TOKEN") final String token, @RequestBody final UserInfoDTO userInfoDTO, HttpServletRequest request) throws MessageException, JsonProcessingException {
        logger.info("編輯使用者資訊");
        logger.info(new ObjectMapper().writeValueAsString(userInfoDTO));
        final Integer loginUserId = tokenService.getUserId(token);
        userService.updateUserInfo(userInfoDTO);

    }

    @DeleteMapping
    public void deleteUser(@RequestHeader(value = "TOKEN") final String token, @RequestParam(value = "userId") final String userId, HttpServletRequest request) throws MessageException {
        logger.info("刪除使用者");
        final Integer loginUserId = tokenService.getUserId(token);
        if (loginUserId.equals(Integer.valueOf(userId))) {
            throw new MessageException("AUTH17", "刪除使用者不成功");
        }
        final UserInfoPO userInfoPO = userService.deleteUser(Integer.valueOf(userId));

    }



}


