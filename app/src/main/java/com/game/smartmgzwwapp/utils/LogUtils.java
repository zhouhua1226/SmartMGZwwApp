package com.game.smartmgzwwapp.utils;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 如果用于android平台，将信息记录到“LogCat”。如果用于java平台，将信息记录到“Console”
 * 使用logger封装
 */
public class LogUtils {
    public static boolean DEBUG_ENABLE =false;// 是否调试模式
    public static String TAG ="com.game.smartmgzwwapp";// 是否调试模式
    /**
     * 在application调用初始化
     */
    public static void logInit(boolean debug) {
        DEBUG_ENABLE=debug;
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().tag(TAG)
                .showThreadInfo(false)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(3)         // （可选）要显示的方法行数。 默认2
                .methodOffset(1)        // （可选）隐藏内部方法调用到偏移量。 默认5
                //.logStrategy(customLog) //（可选）更改要打印的日志策略。 默认LogCat
                .tag("POWER")   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

    }
    public static void logd(String message) {
        if (DEBUG_ENABLE) {
            Logger.d(message);
        }
    }

    public static void loge(String message, Object... args) {
        if (DEBUG_ENABLE) {
            Logger.e(message, args);
        }
    }

    public static void logi(String message) {
        if (DEBUG_ENABLE) {
            Logger.i(message);
        }
    }
    public static void logv(String message, Object... args) {
        if (DEBUG_ENABLE) {
            Logger.v(message, args);
        }
    }

    public static void logwtf(String message, Object... args) {
        if (DEBUG_ENABLE) {
            Logger.wtf(message, args);
        }
    }

    public static void logjson(String message) {
        if (DEBUG_ENABLE) {
            if(isJson(message)){
                Logger.json(message);
            }else{
                Logger.e(message);
            }
        }
    }
    public static void logxml(String message) {
        if (DEBUG_ENABLE) {
            Logger.xml(message);
        }
    }


    public static boolean isJson(String content){
        try {
            JSONObject.parseObject(content);
            return  true;
        } catch (Exception e) {
            return false;
        }

    }

}
