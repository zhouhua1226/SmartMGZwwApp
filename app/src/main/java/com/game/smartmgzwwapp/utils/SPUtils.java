package com.game.smartmgzwwapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by chen on 2018/6/18.
 */
public class SPUtils {

    public static UUID uuid;
    public static final String PREFS_DEVICE_ID = "device_id";//设备号id


    public static void init(Context context) {
        synchronized (SPUtils.class) {
            if (uuid == null) {
                final SharedPreferences prefs = SPUtils.getSharedPreferences(context);
                final String id = prefs.getString(PREFS_DEVICE_ID, null);
                if (id != null) {

                    uuid = UUID.fromString(id);
                } else {
                    final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    // Use the Android ID unless it's broken, in which case
                    // fallback on deviceId,
                    // unless it's not available, then fallback on a random
                    // number which we store
                    // to a prefs file
                    try {
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                        } else {
                            final String deviceId = ((TelephonyManager) context
                                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                            uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID
                                    .randomUUID();
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                }
            }
        }
    }

    /**
     * 获取设备号
     * @param context
     * @return
     */
    public static UUID getDeviceUuid(Context context) {
        if (uuid == null) {
            init(context);
        }
        return uuid;
    }


    private static SharedPreferences getSharedPreferences( Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 存获取SharedPreferences Int 类型
     * @param context
     * @return
     */
    public static int getInt( Context context,  String key,  int defaultValue) {
        return SPUtils.getSharedPreferences(context).getInt(key, defaultValue);
    }
    public static boolean putInt( Context context,  String key,  int pValue) {
        final SharedPreferences.Editor editor = SPUtils.getSharedPreferences(context).edit();
        editor.putInt(key, pValue);
        return editor.commit();
    }
    /**
     * 存获取SharedPreferences Long 类型
     * @param context
     * @return
     */

    public static Long getLong( Context context,  String key,  Long defaultValue) {
        if (SPUtils.getSharedPreferences(context).contains(key)) {
            return SPUtils.getSharedPreferences(context).getLong(key, defaultValue);
        } else {
            return null;
        }
    }
    public static boolean putLong( Context context,  String key,  long pValue) {
        final SharedPreferences.Editor editor = SPUtils.getSharedPreferences(context).edit();
        editor.putLong(key, pValue);
        return editor.commit();
    }
    /**
     * 存获取SharedPreferences Boolean 类型
     * @param context
     * @return
     */
    public static boolean getBoolean( Context context,  String key,  boolean defaultValue) {
        return SPUtils.getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static boolean putBoolean( Context context,  String key,  boolean pValue) {
        final SharedPreferences.Editor editor = SPUtils.getSharedPreferences(context).edit();
        editor.putBoolean(key, pValue);
        return editor.commit();
    }
    /**
     * 存获取SharedPreferences String 类型
     * @param context
     * @return
     */
    public static String getString( Context context,  String key,  String defaultValue) {
        return SPUtils.getSharedPreferences(context).getString(key, defaultValue);
    }
    public static boolean putString( Context context,  String key,  String pValue) {
        final SharedPreferences.Editor editor = SPUtils.getSharedPreferences(context).edit();
        editor.putString(key, pValue);
        return editor.commit();
    }

    /**
     * 存获取SharedPreferences String 类型
     * @param context
     * @return
     */
    public static float getFloat(  Context context,   String key,  float defaultValue) {
        return SPUtils.getSharedPreferences(context).getFloat(key, defaultValue);
    }
    public static boolean putFloat(  Context context,   String key,   float pValue) {
        final SharedPreferences.Editor editor = SPUtils.getSharedPreferences(context).edit();
        editor.putFloat(key, pValue);
        return editor.commit();
    }

    /**
     *  SharedPreferences remove 数据
     * @param context
     * @return
     */

    public static boolean remove(  Context context, final String key) {
        SharedPreferences.Editor editor = SPUtils.getSharedPreferences(context).edit();
        editor.remove(key);
        return editor.commit();
    }


    /**
     * 返回所有数据
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        return   SPUtils.getSharedPreferences(context).getAll();
    }


    /**
     * 清除所有内容
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences.Editor editor = SPUtils.getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

}
