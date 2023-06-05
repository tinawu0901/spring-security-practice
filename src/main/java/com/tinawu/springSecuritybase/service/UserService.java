package com.tinawu.springSecuritybase.service;



import com.tinawu.springSecuritybase.dto.*;
import com.tinawu.springSecuritybase.exception.MessageException;
import com.tinawu.springSecuritybase.po.UserInfoPO;

import java.io.IOException;

public interface UserService {

    void createUser(final CreateUserInputDTO createUserInputDTO) throws MessageException;

    void createUserBySSO(final CreateUserInputDTO createUserInputDTO);

    GetUserNameListResultDTO getUserNameList();

    GetUserListResultDTO getUserList();

    UserInfoDTO getOneselfUserInfo(final Integer userId) throws MessageException;

    void updateOneselfUserInfo(final Integer userId, final ModifyUserInfoDTO modifyUserInfoDTO) throws MessageException;

    void updateUserInfo(final UserInfoDTO userInfoDTO) throws MessageException;

    UserInfoPO deleteUser(final Integer userId) throws MessageException;

}
