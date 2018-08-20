package com.game.smartmgzwwapp.view;

import android.content.Context;
import android.os.Vibrator;

import com.game.smartmgzwwapp.utils.SPUtils;
import com.game.smartmgzwwapp.utils.Utils;


/**
 * Created by yincong on 2017/11/18 11:17
 * 修改人：
 * 修改时间：
 * 类描述：
 */
public class VibratorView {

    private static Vibrator vibrator;

    public static void init(Context context){
        Utils.isVibrator = SPUtils.getBoolean(context,"isVibrator", true);
    }

    public static synchronized Vibrator getVibrator(Context context)
    {
        init(context);
        if(!Utils.isVibrator)
        {
            return null;
        }
        if(null == vibrator){
            vibrator = (Vibrator) context
                    .getSystemService(Context.VIBRATOR_SERVICE);
        }
        return vibrator;
    }
}
