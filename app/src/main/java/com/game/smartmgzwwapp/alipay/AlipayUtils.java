package com.game.smartmgzwwapp.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.game.smartmgzwwapp.utils.EZUtils;
import com.game.smartmgzwwapp.view.MyToast;

import java.util.Map;

/**
 * Created by chenw on 2018/7/19.
 */

public class AlipayUtils {
    public static  Context ctx;
    public static  OnApliyPayResultListenter oOnApliyPayResultListenter;
    /**
     * 调取支付支付界面
     */
    public static void startApliyPay(Activity _ctx,String orderInfo,OnApliyPayResultListenter onApliyPayResultListenter) {
        ctx=_ctx;
        oOnApliyPayResultListenter=onApliyPayResultListenter;
        AlipayRequest.StartAlipay((Activity) ctx, orderInfo, new PayCallback() {
            @Override
            public void payResult(Map<String, String> result) {
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                Message msg = new Message();
                msg.what = EZUtils.PLAYER_APLIY_SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EZUtils.PLAYER_APLIY_SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        MyToast.getToast(ctx, "支付成功").show();
                        if(oOnApliyPayResultListenter!=null){
                            oOnApliyPayResultListenter.OnApliyPayResult(true);
                        }

                    } else if (TextUtils.equals(resultStatus, "8000") || TextUtils.equals(resultStatus, "6004")) {
                        // notifyPayResult("正在支付中，请与客服联系");
                        MyToast.getToast(ctx, "正在支付中，请与客服联系").show();
                        if(oOnApliyPayResultListenter!=null){
                            oOnApliyPayResultListenter.OnApliyPayResult(false);
                        }
                    } else {
                        //   notifyPayResult("支付失败");
                        MyToast.getToast(ctx, "支付失败").show();
                        if(oOnApliyPayResultListenter!=null){
                            oOnApliyPayResultListenter.OnApliyPayResult(false);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    public interface  OnApliyPayResultListenter{
        void  OnApliyPayResult(boolean isSuccess);
    }
}
