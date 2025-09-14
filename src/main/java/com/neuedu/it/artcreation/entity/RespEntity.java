package com.neuedu.it.artcreation.entity;

import java.util.Map;

public class RespEntity<T>{
    private String code;
    private String msg;
    private T data;
    public RespEntity(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> RespEntity<T> error(String msg, T data) {
        return new RespEntity<>("400",msg,data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    static public <T> RespEntity<T> success(String msg,T data){
        return new RespEntity<>("200",msg,data);
    }
}
