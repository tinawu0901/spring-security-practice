package com.tinawu.springSecuritybase.service.impl;


import com.tinawu.springSecuritybase.dto.*;
import com.tinawu.springSecuritybase.enumeration.AccountType;
import com.tinawu.springSecuritybase.enumeration.UserStatus;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.po.UserInfoPO;
import com.tinawu.springSecuritybase.po.UserPO;
import com.tinawu.springSecuritybase.repository.UserInfoRepository;
import com.tinawu.springSecuritybase.repository.UserRepository;
import com.tinawu.springSecuritybase.service.AuthorizationService;
import com.tinawu.springSecuritybase.service.UserService;
import com.tinawu.springSecuritybase.utils.CodecUtil;
import com.tinawu.springSecuritybase.utils.DateUtil;

//import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserInfoRepository userInfoRepository;
//    @Autowired
//    private MessageUtil messageUtil;
//
//    @Override
//    public String convertUserInfoDTOToString(final UserInfoDTO userInfoDTO) {
////        final String userStatus = ObjectUtils.isEmpty(userInfoDTO.getStatus()) ? UserStatus.ACTIVE.getStatus() : userInfoDTO.getStatus();
////        final String userInfo = messageUtil.get( new String[] { userInfoDTO.getUserAccount(), userInfoDTO.getUserName(), userStatus, userInfoDTO.getAccountType(), userInfoDTO.getEmail(), userInfoDTO.getTelephone(), userInfoDTO.getMobile(), userInfoDTO.getOrganization(), String.valueOf(userInfoDTO.getRoleIdList()), String.valueOf(userInfoDTO.getGroupIdList()) });
//        return null;
//    }

    @Override
    public void createUser(final CreateUserInputDTO createUserInputDTO) throws MessageException {
        final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());
        final String account = createUserInputDTO.getAccount();
        final boolean isUserAccountExisted = userRepository.countByUserAccountAndStatusNot(account, UserStatus.DELETED.getStatus()) >= 1 ? true : false;
        if (isUserAccountExisted) {
            // AUTH05:帳號已存在
            throw new MessageException("AUTH05", "帳號已存在");
        }
        final UserPO userPO = new UserPO();
        userPO.setAccount(account);
        String pwdEncode = null;


            final String pwd = createUserInputDTO.getPassword();
            // 密碼加密
            pwdEncode = CodecUtil.getSHA256Str(pwd);
            userPO.setPassword(pwdEncode);

        userRepository.save(userPO);

        final UserInfoPO userInfoPO = new UserInfoPO();
        userInfoPO.setUserId(userPO.getUserId());
        userInfoPO.setUserName(createUserInputDTO.getUserName());
        userInfoPO.setAccountType(createUserInputDTO.getAccountType());
        userInfoPO.setStatus(UserStatus.ACTIVE.getStatus());
        userInfoPO.setEmail(createUserInputDTO.getEmail());
        userInfoPO.setTelephone(createUserInputDTO.getTelephone());
        userInfoPO.setMobile(createUserInputDTO.getMobile());
        userInfoPO.setOrganization(createUserInputDTO.getOrganization());
        userInfoPO.setLastModified(systemCurrentTime);
        userInfoRepository.save(userInfoPO);

    }

    @Override
    public void createUserBySSO(CreateUserInputDTO createUserInputDTO) {
        final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());
        final String account = createUserInputDTO.getAccount();
        final boolean isUserAccountExisted = userRepository.countByUserAccountAndStatusNot(account, UserStatus.DELETED.getStatus()) >= 1 ? true : false;
        if (!isUserAccountExisted) {
            final UserPO userPO = new UserPO();
            userPO.setAccount(account);
            String pwdEncode = null;

            if (AccountType.GOOGLE_SSO.getType().equals(createUserInputDTO.getAccountType())) {
                logger.info(String.format("google登入成功", account));
            } else {
                final String pwd = createUserInputDTO.getPassword();
                // 密碼加密
                pwdEncode = CodecUtil.getSHA256Str(pwd);
                userPO.setPassword(pwdEncode);
            }
            userRepository.save(userPO);

            final UserInfoPO userInfoPO = new UserInfoPO();
            userInfoPO.setUserId(userPO.getUserId());
            userInfoPO.setUserName(createUserInputDTO.getUserName());
            userInfoPO.setAccountType(createUserInputDTO.getAccountType());
            userInfoPO.setStatus(UserStatus.ACTIVE.getStatus());
            userInfoPO.setEmail(createUserInputDTO.getEmail());
            userInfoPO.setTelephone(createUserInputDTO.getTelephone());
            userInfoPO.setMobile(createUserInputDTO.getMobile());
            userInfoPO.setOrganization(createUserInputDTO.getOrganization());
            userInfoPO.setLastModified(systemCurrentTime);
            userInfoRepository.save(userInfoPO);
        }
    }

    @Override
    public GetUserNameListResultDTO getUserNameList() {
        final List<UserPO> userPOList = userRepository.findByStatusNot(UserStatus.DELETED.getStatus());
        final List<UserNameDTO> userNameDTOList = new ArrayList<>();
        for (UserPO userPO : userPOList) {
            final UserNameDTO userNameDTO = new UserNameDTO();
            userNameDTO.setUserId(String.valueOf(userPO.getUserId()));
            userNameDTO.setAccount(userPO.getAccount());
            userNameDTO.setUserName(userPO.getUserInfoPO().getUserName());
            userNameDTOList.add(userNameDTO);
        }
        final GetUserNameListResultDTO getUserNameListResultDTO = new GetUserNameListResultDTO();
        getUserNameListResultDTO.setUserNameDTOList(userNameDTOList);
        return getUserNameListResultDTO;
    }

    @Override
    public GetUserListResultDTO getUserList() {
        final List<UserPO> userPOList = userRepository.findByStatusNot(UserStatus.DELETED.getStatus());
        final List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        for (UserPO userPO : userPOList) {
            final UserInfoDTO userInfoDTO = this.composeUserInfoDTO(userPO);
            userInfoDTOList.add(userInfoDTO);
        }
        final GetUserListResultDTO getUserListResultDTO = new GetUserListResultDTO();
        getUserListResultDTO.setUserInfoDTOList(userInfoDTOList);
        return getUserListResultDTO;
    }

    @Override
    public UserInfoDTO getOneselfUserInfo(final Integer userId) throws MessageException {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        final UserPO userPO = userRepository.findByUserIdAndAndStatusNot(userId, UserStatus.DELETED.getStatus());
        if (ObjectUtils.isEmpty(userPO)) {
            // AUTH01:帳號不存在
            logger.info(String.format("取得個人資訊失敗，使用者編號(%s)不存在", userId));
            throw new MessageException("AUTH05", "取得個人資訊失敗，使用者編號不存在");
        } else {
            userInfoDTO = this.composeUserInfoDTO(userPO);
        }
        return userInfoDTO;
    }

    private UserInfoDTO composeUserInfoDTO(final UserPO userPO) {
        final UserInfoPO userInfo = userPO.getUserInfoPO();
        final UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(String.valueOf(userPO.getUserId()));
        userInfoDTO.setAccount(userPO.getAccount());
        userInfoDTO.setUserName(userInfo.getUserName());
        userInfoDTO.setAccountType(userInfo.getAccountType());
        userInfoDTO.setStatus(userInfo.getStatus());
        userInfoDTO.setEmail(userInfo.getEmail());
        userInfoDTO.setTelephone(userInfo.getTelephone());
        userInfoDTO.setMobile(userInfo.getMobile());
        userInfoDTO.setOrganization(userInfo.getOrganization());
        userInfoDTO.setLastModified(userInfo.getLastModified());
        userInfoDTO.setPwdExpiration(userPO.getPwdExpiration());
        userInfoDTO.setLastLogin(userPO.getLastLogin());
        return userInfoDTO;
    }

    @Override
    public void updateOneselfUserInfo(final Integer userId, final ModifyUserInfoDTO modifyUserInfoDTO) throws MessageException {
        final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());
        final UserPO userPO = userRepository.findByUserIdAndAndStatusNot(userId, UserStatus.DELETED.getStatus());
        if (ObjectUtils.isEmpty(userPO)) {
            // AUTH01:帳號不存在
            logger.info(String.format("編輯個人資訊失敗，使用者編號(%s)不存在", userId));
            throw new MessageException("AUTH05", "取得個人資訊失敗，使用者編號不存在");
        } else {
            final UserInfoPO userInfo = userPO.getUserInfoPO();
            userInfo.setUserName(modifyUserInfoDTO.getUserName());
            userInfo.setOrganization(modifyUserInfoDTO.getOrganization());
            userInfo.setTelephone(modifyUserInfoDTO.getTelephone());
            userInfo.setMobile(modifyUserInfoDTO.getMobile());
            userInfo.setEmail(modifyUserInfoDTO.getEmail());
            userInfo.setLastModified(systemCurrentTime);
            userRepository.save(userPO);
        }
    }

    @Override
    public void updateUserInfo(final UserInfoDTO userInfoDTO) throws MessageException {
        final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());
        final Integer userId = Integer.valueOf(userInfoDTO.getUserId());
        final UserPO userPO = userRepository.findByUserIdAndAndStatusNot(userId, UserStatus.DELETED.getStatus());
        if (ObjectUtils.isEmpty(userPO)) {
            // AUTH01:帳號不存在
            logger.info(String.format("修改使用者資訊失敗，使用者編號(%s)不存在", userId));
            throw new MessageException("AUTH05", "取得個人資訊失敗，使用者編號不存在");
        } else {
            final List<String> roleIdStringList = userInfoDTO.getRoleIdList();
            final List<Integer> roleIdList = roleIdStringList.stream().map(Integer::valueOf).collect(Collectors.toList());
            final UserInfoPO userInfoPO = userPO.getUserInfoPO();
            userInfoPO.setUserName(userInfoDTO.getUserName());
            userInfoPO.setStatus(userInfoDTO.getStatus());
            userInfoPO.setEmail(userInfoDTO.getEmail());
            userInfoPO.setTelephone(userInfoDTO.getTelephone());
            userInfoPO.setMobile(userInfoDTO.getMobile());
            userInfoPO.setOrganization(userInfoDTO.getOrganization());
            userInfoPO.setLastModified(systemCurrentTime);
            userRepository.save(userPO);
            if (UserStatus.DISABLED.getStatus().equals(userInfoDTO.getStatus())) {
                // 停用帳號，將該帳號登出
                authorizationService.logout(userId);
            }
        }
    }

    @Override
    public UserInfoPO deleteUser(final Integer userId) throws MessageException {
        final Timestamp systemCurrentTime = new Timestamp(System.currentTimeMillis());
        UserInfoPO userInfoPO = null;
        final UserPO userPO = userRepository.findByUserIdAndAndStatusNot(userId, UserStatus.DELETED.getStatus());
        if (ObjectUtils.isEmpty(userPO)) {
            // AUTH01:帳號不存在
            logger.info(String.format("刪除使用者失敗，使用者編號(%s)不存在", userId));
            throw new MessageException("AUTH05", "取得個人資訊失敗，使用者編號不存在");
        } else {
            userInfoPO = userPO.getUserInfoPO();
            userInfoPO.setStatus(UserStatus.DELETED.getStatus());
            userInfoPO.setLastModified(systemCurrentTime);
            userRepository.save(userPO);
            // 將該帳號登出
            authorizationService.logout(userId);
        }
        return userInfoPO;
    }
}
