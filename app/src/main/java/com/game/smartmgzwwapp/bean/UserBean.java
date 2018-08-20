package com.game.smartmgzwwapp.bean;

import java.io.Serializable;

/**
 * Created by hongxiu on 2017/10/11.
 */
public class UserBean implements Serializable {
    private String NUMBER;
    private String START_TIME;
    private String RIGHTS;
    private String IP;
     private String PHONE;
    private String SFID;
    private String USER_ID;
    private String LAST_LOGIN;
    private String EMAIL;
    private String NAME;
    private String YEARS;
    private String STATUS;
    private String END_TIME;
    private String PASSWORD;
    private String BZ;
    private String ROLE_ID;
    private String USERNAME;
    private String CREATETIME;
    private String IMAGE_URL;
    private String BALANCE;
    private String NICKNAME;
    private String CNEE_NAME;
    private String CNEE_PHONE;
    private String CNEE_ADDRESS;
    private String GUESSID;
    private String RANK;
    private String DOLL_ID;
    private String DOLLTOTAL;
    private int BET_NUM;
    private String FIRST_CHARGE ;
    private String WEEKS_CARD ;
    private String MONTH_CARD ;
    private String MONTH_CARD_TAG ;
    private String WEEKS_CARD_TAG ;
    private String GENDER ;
    private String OPEN_TYPE ;
    private String  BDPHONE;
    private int TODAY_GUESS;//今日抓娃娃总数
    private int   TODAY_POOH ;;//今日竞猜总数

    public int getTODAY_GUESS() {
        return TODAY_GUESS;
    }

    public void setTODAY_GUESS(int TODAY_GUESS) {
        this.TODAY_GUESS = TODAY_GUESS;
    }

    public int getTODAY_POOH() {
        return TODAY_POOH;
    }

    public void setTODAY_POOH(int TODAY_POOH) {
        this.TODAY_POOH = TODAY_POOH;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getBDPHONE() {
        return BDPHONE;
    }
    public void setBDPHONE(String BDPHONE) {
        this.BDPHONE = BDPHONE;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getOPEN_TYPE() {
        return OPEN_TYPE;
    }

    public void setOPEN_TYPE(String OPEN_TYPE) {
        this.OPEN_TYPE = OPEN_TYPE;
    }

    public String getFIRST_CHARGE() {
        return FIRST_CHARGE;
    }

    public void setFIRST_CHARGE(String FIRST_CHARGE) {
        this.FIRST_CHARGE = FIRST_CHARGE;
    }

    public String getWEEKS_CARD() {
        return WEEKS_CARD;
    }

    public void setWEEKS_CARD(String WEEKS_CARD) {
        this.WEEKS_CARD = WEEKS_CARD;
    }

    public String getMONTH_CARD() {
        return MONTH_CARD;
    }

    public void setMONTH_CARD(String MONTH_CARD) {
        this.MONTH_CARD = MONTH_CARD;
    }

    public String getMONTH_CARD_TAG() {
        return MONTH_CARD_TAG;
    }

    public void setMONTH_CARD_TAG(String MONTH_CARD_TAG) {
        this.MONTH_CARD_TAG = MONTH_CARD_TAG;
    }

    public String getWEEKS_CARD_TAG() {
        return WEEKS_CARD_TAG;
    }

    public void setWEEKS_CARD_TAG(String WEEKS_CARD_TAG) {
        this.WEEKS_CARD_TAG = WEEKS_CARD_TAG;
    }

    public int getBET_NUM() {
        return BET_NUM;
    }

    public void setBET_NUM(int BET_NUM) {
        this.BET_NUM = BET_NUM;
    }

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getGUESSID() {
        return GUESSID;
    }

    public void setGUESSID(String GUESSID) {
        this.GUESSID = GUESSID;
    }

    public String getCNEE_NAME() {
        return CNEE_NAME;
    }

    public void setCNEE_NAME(String CNEE_NAME) {
        this.CNEE_NAME = CNEE_NAME;
    }

    public String getCNEE_PHONE() {
        return CNEE_PHONE;
    }

    public void setCNEE_PHONE(String CNEE_PHONE) {
        this.CNEE_PHONE = CNEE_PHONE;
    }

    public String getCNEE_ADDRESS() {
        return CNEE_ADDRESS;
    }

    public void setCNEE_ADDRESS(String CNEE_ADDRESS) {
        this.CNEE_ADDRESS = CNEE_ADDRESS;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }


    public String getDOLL_ID() {
        return DOLL_ID;
    }

    public void setDOLL_ID(String DOLL_ID) {
        this.DOLL_ID = DOLL_ID;
    }



    public String getDOLLTOTAL() {
        return DOLLTOTAL;
    }

    public void setDOLLTOTAL(String DOLLTOTAL) {
        this.DOLLTOTAL = DOLLTOTAL;
    }


    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String BALANCE) {
        this.BALANCE = BALANCE;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setLAST_LOGIN(String LAST_LOGIN) {
        this.LAST_LOGIN = LAST_LOGIN;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }



    public void setRIGHTS(String RIGHTS) {
        this.RIGHTS = RIGHTS;
    }

    public void setROLE_ID(String ROLE_ID) {
        this.ROLE_ID = ROLE_ID;
    }

    public void setSFID(String SFID) {
        this.SFID = SFID;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public void setYEARS(String YEARS) {
        this.YEARS = YEARS;
    }

    public String getBZ() {
        return BZ;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public String getIP() {
        return IP;
    }

    public String getLAST_LOGIN() {
        return LAST_LOGIN;
    }

    public String getNAME() {
        return NAME;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }


    public String getRIGHTS() {
        return RIGHTS;
    }

    public String getROLE_ID() {
        return ROLE_ID;
    }

    public String getSFID() {
        return SFID;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getYEARS() {
        return YEARS;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }
}
