package com.game.smartmgzwwapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.game.smartmgzwwapp.R;


/**
 * Created by mi on 2018/8/13.
 */

public class PayTypeDialog extends Dialog implements View.OnClickListener{

    private ImageButton closeBtn;
    private LinearLayout rl_pay_alipay,rl_pay_weixin;

    public PayTypeDialog(Context context) {
        super(context);
    }

    public PayTypeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PayTypeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_type_dialog);
        findView();
        setListner();
    }

    public void findView() {
        rl_pay_alipay= (LinearLayout) findViewById(R.id.rl_pay_alipay);
        rl_pay_weixin= (LinearLayout) findViewById(R.id.rl_pay_weixin);
        closeBtn= (ImageButton) findViewById(R.id.iv_pay_type_close);
    }

    /**
     * 绑定监听
     **/
    private void setListner() {
        rl_pay_weixin.setOnClickListener(this);
        rl_pay_alipay.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pay_type_close:
                PayTypeDialog.this.dismiss();
                break;
            case R.id.rl_pay_weixin:
                if (null != this.listener) {
                    listener.getResult(true);
                }
                PayTypeDialog.this.dismiss();
                break;
            case R.id.rl_pay_alipay:
                if (null != this.listener) {
                    listener.getResult(false);
                }
                PayTypeDialog.this.dismiss();
                break;
        }
    }

    private  DialogResultListener listener;

    public void setDialogResultListener( DialogResultListener listener) {
        this.listener = listener;
    }

    public interface DialogResultListener {
        /**
         * 获取结果的方法
         *
         * @param payChannelType
         */
        void getResult(boolean payChannelType);
    }


}
