package com.game.smartmgzwwapp.model.crash;


import com.game.smartmgzwwapp.base.MyApplication;

/**
 * Created by zhouh on 2017/11/27.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{

    public CrashHandler(MyApplication application) {

    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

    }
}
