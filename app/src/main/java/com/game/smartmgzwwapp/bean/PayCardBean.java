package com.game.smartmgzwwapp.bean;

import java.io.Serializable;

/**
 * Created by yincong on 2017/12/26 13:41
 * 修改人：
 * 修改时间：
 * 类描述：充值卡实体类
 */
public class PayCardBean implements Serializable {

    private String GOLD;        //共可以获得的金币数
    private String AMOUNT;      //充值金额
    private int ID;             //充值卡id
    private String IMAGEURL;    //充值卡url
    private String DISCOUNT;    //充值卡折扣
    private String AWARD;        //奖励金币数
    private String FIRSTAWARD_GOLD; //首充用户可以获得的总金币数
    private String RECHARE;    //本金对应的金币数
    private String FIRSTAWARD;    //首充本金对应的金币数

    public String getAWARD() {
        return AWARD;
    }

    public void setAWARD(String AWARD) {
        this.AWARD = AWARD;
    }

    public String getFIRSTAWARD_GOLD() {
        return FIRSTAWARD_GOLD;
    }

    public void setFIRSTAWARD_GOLD(String FIRSTAWARD_GOLD) {
        this.FIRSTAWARD_GOLD = FIRSTAWARD_GOLD;
    }

    public String getRECHARE() {
        return RECHARE;
    }

    public void setRECHARE(String RECHARE) {
        this.RECHARE = RECHARE;
    }

    public String getFIRSTAWARD() {
        return FIRSTAWARD;
    }

    public void setFIRSTAWARD(String FIRSTAWARD) {
        this.FIRSTAWARD = FIRSTAWARD;
    }

    public String getGOLD() {
        return GOLD;
    }

    public void setGOLD(String GOLD) {
        this.GOLD = GOLD;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getIMAGEURL() {
        return IMAGEURL;
    }

    public void setIMAGEURL(String IMAGEURL) {
        this.IMAGEURL = IMAGEURL;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(String DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }
}
