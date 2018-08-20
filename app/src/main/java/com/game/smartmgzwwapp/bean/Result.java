package com.game.smartmgzwwapp.bean;

import java.io.Serializable;

/**
 * Created by zhouh on 2017/9/8.
 */
public class Result<T> implements Serializable{
    private T data;
    private int code;
    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
