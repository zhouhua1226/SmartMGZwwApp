package com.game.smartmgzwwapp.bean;

/**
 * Created by mi on 2018/4/16.
 */

public class AppInfo {
    private String APPVERSION_ID;
    private String DOWNLOAD_URL;
    private String VERSION;
    private String STATE;
    private String CONTENT;
    private String CREATE_TIME;

    public String getAPPVERSION_ID() {
        return APPVERSION_ID;
    }

    public void setAPPVERSION_ID(String APPVERSION_ID) {
        this.APPVERSION_ID = APPVERSION_ID;
    }

    public String getDOWNLOAD_URL() {
        return DOWNLOAD_URL;
    }

    public void setDOWNLOAD_URL(String DOWNLOAD_URL) {
        this.DOWNLOAD_URL = DOWNLOAD_URL;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }
}
