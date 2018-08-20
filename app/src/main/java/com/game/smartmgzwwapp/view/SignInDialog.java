package com.game.smartmgzwwapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.game.smartmgzwwapp.R;


/**
 * Created by yincong on 2018/1/5 15:45
 * 修改人：
 * 修改时间：
 * 类描述：签到弹窗
 */

public class SignInDialog extends Dialog implements View.OnClickListener{

    private ImageView imageView1,imageView2,imageView3,imageView4,
            imageView5,imageView6,imageView7,imageView_sure,
            signin_day1_dot_iv,signin_day2_dot_iv,signin_day3_dot_iv,
            signin_day4_dot_iv, signin_day5_dot_iv,signin_day6_dot_iv,
            signin_day7_dot_iv;
    private ImageButton signin_day1_ibtn,signin_day2_ibtn,signin_day3_ibtn,
            signin_day4_ibtn,signin_day5_ibtn,signin_day6_ibtn,
            signin_day7_ibtn,cancle_ibtn,sure_ibtn;


    public SignInDialog(Context context) {
        super(context);
    }

    public SignInDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public SignInDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_dialog);
        findView();
        setListner();
    }

    /**
     * 设置天数背景
     *
     */
    public void setBackGroundColor(int[] num) {
        if(num[0]==0){
            signin_day1_dot_iv.setImageResource(R.drawable.tm_sign_green_dot);
            signin_day1_ibtn.setBackgroundResource(R.drawable.tm_sign_coin5);
        }else {
            signin_day1_dot_iv.setImageResource(R.drawable.tm_sign_yellow_dot);
            signin_day1_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned1);
        }
        if(num[1]==0){
            signin_day2_dot_iv.setImageResource(R.drawable.tm_sign_green_dot);
            signin_day2_ibtn.setBackgroundResource(R.drawable.tm_sign_coin5);
        }else {
            signin_day2_dot_iv.setImageResource(R.drawable.tm_sign_yellow_dot);
            signin_day2_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned1);
        }
        if(num[2]==0){
            signin_day3_dot_iv.setImageResource(R.drawable.tm_sign_green_dot);
            signin_day3_ibtn.setBackgroundResource(R.drawable.tm_sign_coin15);
        }else {
            signin_day3_dot_iv.setImageResource(R.drawable.tm_sign_yellow_dot);
            signin_day3_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned1);
        }
        if(num[3]==0){
            signin_day4_dot_iv.setImageResource(R.drawable.tm_sign_green_dot);
            signin_day4_ibtn.setBackgroundResource(R.drawable.tm_sign_coin10);
        }else {
            signin_day4_dot_iv.setImageResource(R.drawable.tm_sign_yellow_dot);
            signin_day4_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned1);
        }
        if(num[4]==0){
            signin_day5_dot_iv.setImageResource(R.drawable.tm_sign_green_dot);
            signin_day5_ibtn.setBackgroundResource(R.drawable.tm_sign_coin10);
        }else {
            signin_day5_dot_iv.setImageResource(R.drawable.tm_sign_yellow_dot);
            signin_day5_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned1);
        }
        if(num[5]==0){
            signin_day6_dot_iv.setImageResource(R.drawable.tm_sign_green_dot);
            signin_day6_ibtn.setBackgroundResource(R.drawable.tm_sign_coin10);
        }else {
            signin_day6_dot_iv.setImageResource(R.drawable.tm_sign_yellow_dot);
            signin_day6_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned1);
        }
        if(num[6]==0){
            signin_day7_dot_iv.setImageResource(R.drawable.tm_sign_green_dot);
            signin_day7_ibtn.setBackgroundResource(R.drawable.tm_sign_coin30);
        }else {
            signin_day7_dot_iv.setImageResource(R.drawable.tm_sign_yellow_dot);
            signin_day7_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned2);
        }
    }


    public void findView() {
        sure_ibtn= (ImageButton) findViewById(R.id.signin_sure_ibtn);
        cancle_ibtn= (ImageButton) findViewById(R.id.signin_cancle_ibtn);
        signin_day1_dot_iv= (ImageView) findViewById(R.id.signin_day1_dot_iv);
        signin_day1_ibtn= (ImageButton) findViewById(R.id.signin_day1_ibtn);
        signin_day2_dot_iv= (ImageView) findViewById(R.id.signin_day2_dot_iv);
        signin_day2_ibtn= (ImageButton) findViewById(R.id.signin_day2_ibtn);
        signin_day3_dot_iv= (ImageView) findViewById(R.id.signin_day3_dot_iv);
        signin_day3_ibtn= (ImageButton) findViewById(R.id.signin_day3_ibtn);
        signin_day4_dot_iv= (ImageView) findViewById(R.id.signin_day4_dot_iv);
        signin_day4_ibtn= (ImageButton) findViewById(R.id.signin_day4_ibtn);
        signin_day5_dot_iv= (ImageView) findViewById(R.id.signin_day5_dot_iv);
        signin_day5_ibtn= (ImageButton) findViewById(R.id.signin_day5_ibtn);
        signin_day6_dot_iv= (ImageView) findViewById(R.id.signin_day6_dot_iv);
        signin_day6_ibtn= (ImageButton) findViewById(R.id.signin_day6_ibtn);
        signin_day7_dot_iv= (ImageView) findViewById(R.id.signin_day7_dot_iv);
        signin_day7_ibtn= (ImageButton) findViewById(R.id.signin_day7_ibtn);
    }

    /**
     * 当前是否签到，从而更改签到按钮UI
     * @param isSign
     */
    public void isSignedView(boolean isSign){
        sure_ibtn.setBackgroundResource(R.drawable.tm_sign_sure_bg);
//        if(isSign){
//            sure_ibtn.setBackgroundResource(R.drawable.tm_sign_hassigned3);
//        }else {
//            sure_ibtn.setBackgroundResource(R.drawable.tm_sign_sure_bg);
//        }
    }

    /**
     * 绑定监听
     **/
    private void setListner() {
        cancle_ibtn.setOnClickListener(this);
        sure_ibtn.setOnClickListener(this);
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin_sure_ibtn:
                if (null != this.listener) {
                    listener.getResult(0);
                }
                //SignInDialog.this.dismiss();
                break;
            case R.id.signin_cancle_ibtn:
                SignInDialog.this.dismiss();
                break;
            default:
                break;
        }
    }

    private DialogResultListener listener;

    public void setDialogResultListener(DialogResultListener listener) {
        this.listener = listener;
    }

    public interface DialogResultListener {
        /**
         * 获取结果的方法
         *
         * @param resultCode 0.确认  1.第一天  2.第二天  3.第三天 .....
         */
        void getResult(int resultCode);
    }

}
