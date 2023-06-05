package com.tinawu.springSecuritybase.dto;

import java.io.Serializable;
import java.util.List;

public class GetUserNameListResultDTO implements Serializable {
    private static final long serialVersionUID = 6182827686828230853L;
    private List<UserNameDTO> userNameDTOList;

    public List<UserNameDTO> getUserNameDTOList() {
        return userNameDTOList;
    }

    public void setUserNameDTOList(List<UserNameDTO> userNameDTOList) {
        this.userNameDTOList = userNameDTOList;
    }
}
