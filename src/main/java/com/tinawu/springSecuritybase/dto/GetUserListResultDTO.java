package com.tinawu.springSecuritybase.dto;

import java.io.Serializable;
import java.util.List;

public class GetUserListResultDTO implements Serializable {
    private static final long serialVersionUID = -2521502000358858727L;
    private List<UserInfoDTO> userInfoDTOList;

    public List<UserInfoDTO> getUserInfoDTOList() {
        return userInfoDTOList;
    }

    public void setUserInfoDTOList(List<UserInfoDTO> userInfoDTOList) {
        this.userInfoDTOList = userInfoDTOList;
    }
}
