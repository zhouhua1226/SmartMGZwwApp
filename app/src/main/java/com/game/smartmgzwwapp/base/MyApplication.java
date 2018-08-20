package com.game.smartmgzwwapp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.game.smartmgzwwapp.service.SmartRemoteService;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhouh on 2017/9/7.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication myApplication;
    public static List<Activity> activities = new LinkedList<Activity>();
    private String TAG="MyApplication--";

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMShareAPI.get(this);
        startCoreService();
        getPushAgent();
        setCrashHandler();
        LogUtils.logInit(false);//初始化logger

    }
    private void setCrashHandler() {

    }
    private void getPushAgent() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e(TAG,"友盟deviceToken="+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.setDisplayNotificationNumber(2);
        mPushAgent.setDebugMode(false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void startCoreService() {
        Intent it = new Intent();
        it.setClass(getApplicationContext(), SmartRemoteService.class);
        startService(it);
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin(UrlUtils.APP_WEIXIN_ID, UrlUtils.APP_WEIXIN_KEY);
        PlatformConfig.setQQZone(UrlUtils.APP_QQ_ID, UrlUtils.APP_QQ_KEY);

    }
    public void exit() {
        if (activities != null) {
            Activity activity;
            for (int i = 0; i < activities.size(); i++) {
                activity = activities.get(i);
                if (activity != null) {
                    if (!activity.isFinishing()) {
                        activity.finish();
                    }
                    activity = null;
                }
                activities.remove(i);
                i--;
            }

        }
    }
}
