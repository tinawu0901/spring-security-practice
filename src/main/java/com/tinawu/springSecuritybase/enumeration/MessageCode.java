package com.tinawu.springSecuritybase.enumeration;

public enum MessageCode {
    Success("success","操作成功"),
    Faild("faild","操作失敗"),
    SystemError("systemError","系統發生錯誤"),

    NotFind("NotFind","此id不存在，請確認");

    private String code;
    private String message;
    MessageCode(final String code, final  String message){
        this.code = code;
        this.message=message;
    }

    public String getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
}
