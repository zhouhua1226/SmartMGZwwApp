package com.game.smartmgzwwapp.utils;

/**
 * Created by mi on 2018/7/26.
 */

public class VersionUtils {

    /**
     * 校验当前版本号是否小于指定版本号，如果小于则返回true
     *
     * @param curVer  当前版本号
     * @param  speVer 指定版本号
     * @return curVer < speVer 返回true
     * curVer >= speVer 返回false
     */
    public static boolean validateVersion(String curVer, String speVer) {
        boolean flag = true;
        String[] curVers = curVer.split("\\.");
        String[] speVers = speVer.split("\\.");

        int[] iCur = new int[curVers.length];
        int[] iSper = new int[speVers.length];

        for (int i = 0; i < curVers.length; i++) {
            iCur[i] = Integer.parseInt(curVers[i]);
        }
        for (int i = 0; i < speVers.length; i++) {
            iSper[i] = Integer.parseInt(speVers[i]);
        }

        int iFlag = 0;
        if (curVers.length < speVers.length) {
            iFlag = validata(iSper, iCur);
            if (iFlag > 0) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            iFlag =validata(iCur, iSper);
            if (iFlag >= 0) {
                flag = false;
            } else {
                flag = true;
            }
        }
        return flag;

    }

    /**
     * @param bigOne
     * @param smallOne
     * @return 1 说明 bigOne > smallOne
     * -1 说明bigOne < smallOne
     * 0 说明bigOne == smallOne
     */
    public static int validata(int[] bigOne, int[] smallOne) {
        int flag = 0;
        int temp = 0;
        for (int i = 0; i < bigOne.length; i++) {
            if (i > smallOne.length - 1) {
                temp = 0;
            } else {
                temp = smallOne[i];
            }
            if (temp < bigOne[i]) {
                flag = 1;
                return flag;
            } else if (temp > bigOne[i]) {
                flag = -1;
                return flag;
            } else {
                if (i == bigOne.length - 1) {
                    flag = 0;
                } else {
                    continue;
                }
            }
        }
        return flag;
    }
}
