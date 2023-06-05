package com.tinawu.springSecuritybase.advice;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tinawu.springSecuritybase.dto.ResponseObject;
import com.tinawu.springSecuritybase.enumeration.MessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        final String methodName = returnType.getMethod().getName();
        System.out.println(methodName);
        return !"apiExceptionHandler".equals(methodName)&& !"messageExceptionHandler".equals(methodName)&& !"validateExceptionHandler".equals(methodName);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ResponseObject responseObject = new ResponseObject(MessageCode.Success.getCode(), MessageCode.Success.getMessage(),true,o);
        return responseObject;
    }
}
