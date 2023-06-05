package com.tinawu.springSecuritybase.dto;

import lombok.Data;

@Data
public class ResponseObject<T> {
    private String code;
    private String message;
    private Boolean sucess;
    private T data;
    public ResponseObject(String code, String message, Boolean sucess, T data){

        this.code = code;
        this.message = message;
        this.sucess = sucess;
        this.data = data;
    }
}
