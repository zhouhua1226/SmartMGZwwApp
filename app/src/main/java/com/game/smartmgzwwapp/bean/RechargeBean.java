package com.game.smartmgzwwapp.bean;

/**
 * Created by chenw on 2018/7/17.
 */

public class RechargeBean {
    public int id;
    public int icon;
    public String rechargeMoney;
    public String sendMoney;
    public String sumMoney;
    public boolean  isCheck;

    public RechargeBean(int id,int icon, String rechargeMoney, String sendMoney, String sumMoney, boolean isCheck) {
        this.id = id;
        this.icon = icon;
        this.rechargeMoney = rechargeMoney;
        this.sendMoney = sendMoney;
        this.sumMoney = sumMoney;
        this.isCheck = isCheck;
    }
}
