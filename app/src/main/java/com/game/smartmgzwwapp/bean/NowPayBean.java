package com.game.smartmgzwwapp.bean;

import java.io.Serializable;

/**
 * Created by mi on 2018/8/15.
 */

public class NowPayBean<T> implements Serializable {

    private String nowpayData;
    private int code;
    private String msg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getNowpayData() {
        return nowpayData;
    }

    public void setNowpayData(String nowpayData) {
        this.nowpayData = nowpayData;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
