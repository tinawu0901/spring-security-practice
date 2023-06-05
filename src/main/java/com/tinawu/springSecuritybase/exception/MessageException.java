package com.tinawu.springSecuritybase.exception;

public class MessageException extends Exception{
    private final String errorCode;
    private  final String errorMessage;
    private Object data;
    public MessageException(final String errorCode, final String errorMessage, final Object data){
        super(errorMessage);
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
        this.data=data;
    }
    public MessageException(final String errorCode, final String errorMessage){
        super(errorMessage);
        this.errorCode= errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getData() {
        return data;
    }
}
