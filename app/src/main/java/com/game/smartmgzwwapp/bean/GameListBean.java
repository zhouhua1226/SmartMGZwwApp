package com.game.smartmgzwwapp.bean;

import java.io.Serializable;

/**
 * Created by yincong on 2018/4/3 14:37
 * 修改人：
 * 修改时间：
 * 类描述：房间游戏记录实体类
 */
public class GameListBean implements Serializable{

    private String CREATE_DATE;
    private String NICKNAME;
    private String STATE;
    private String IMAGE_URL;

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }
}
