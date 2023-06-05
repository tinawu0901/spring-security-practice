package com.tinawu.springSecuritybase.advice;


import com.tinawu.springSecuritybase.dto.ResponseObject;
import com.tinawu.springSecuritybase.enumeration.MessageCode;
import com.tinawu.springSecuritybase.exception.MessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

/*
*
*
* 攔截錯誤訊息
* */
@RestControllerAdvice
public class ApiExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
    //處理MessageException例外訊息
    @ExceptionHandler(value = MessageException.class)
    public ResponseEntity<ResponseObject>messageExceptionHandler(final MessageException messageException){
        logger.info("Occur  MessageException,errCode:{},message:{}",messageException.getErrorCode(),messageException.getErrorMessage());
        final ResponseObject responseObject = new ResponseObject(messageException.getErrorCode(),messageException.getErrorMessage(),false,messageException.getData());
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseObject> apiExceptionHandler(final Exception exception){
        logger.info("Occur internal Exception,message:{}",exception.getMessage(),exception);
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final ResponseObject responseObject = new ResponseObject( MessageCode.SystemError.getCode(), MessageCode.SystemError.getMessage(),false,null);
        return new ResponseEntity<>(responseObject,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> validateExceptionHandler(final MethodArgumentNotValidException e){
        logger.info("MethodArgumentNotValidException,message:{}",e.getMessage(),e);
        String validateMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        final ResponseObject responseObject = new ResponseObject( MessageCode.Faild.getCode(),validateMessage,false,null);
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

}
