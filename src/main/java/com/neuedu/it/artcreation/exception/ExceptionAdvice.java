package com.neuedu.it.artcreation.exception;

import com.neuedu.it.artcreation.entity.RespEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.neuedu.it.artcreation.controller")
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RespEntity handleException(Exception e){
        e.printStackTrace();
        return new RespEntity("5000","服务器异常"+e.getMessage(),null);
    }
}
