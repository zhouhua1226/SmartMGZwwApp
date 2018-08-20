package com.game.smartmgzwwapp.activity.mushroom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.ChannelUtils;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.SPUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.utils.YsdkUtils;
import com.game.smartmgzwwapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/8/8.
 */

public class RegisterOneActivity extends BaseActivity {
    @BindView(R.id.et_register_phone)
    EditText et_phone;
    @BindView(R.id.et_register_password)
    EditText et_password;
    @BindView(R.id.et_register_code)
    EditText register_code;
    @BindView(R.id.tv_get_code)
    TextView getCode;

    private  MyCountDownTimer myCountDownTimer;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_one;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTranslucentStatus();
        initView();
        myCountDownTimer =new  MyCountDownTimer(60000,1000);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_sure_register, R.id.image_back,R.id.tv_get_code,})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.image_back:
                 finish();
                break;
            case R.id.btn_sure_register:
                registerTask();
                break;
            case R.id.tv_get_code:
                getCode();
                break;
        }
    }

    private void registerTask() {
        String phone=et_phone.getText().toString();
        String password=et_password.getText().toString();
        String code=register_code.getText().toString();
        if(phone.isEmpty()){
            MyToast.getToast(this,"请输入手机号").show();
            return;
        }if(!Utils.checkPhoneREX(phone)){
            MyToast.getToast(this,"手机号格式有误").show();
            return;
        }
        if(password.isEmpty()){
            MyToast.getToast(this,"请输入密码").show();
            return;
        }if(!Utils.isTextPassword(password)){
            MyToast.getToast(this,"密码6-20位数字和字母组合").show();
            return;
        }
        if(code.isEmpty()){
            MyToast.getToast(this,"请输入短信验证码").show();
            return;
        }
        regiterTask(phone,password,code,
                ChannelUtils.getChannelNum());//渠道
    }

    private void regiterTask(  String phone, String pass, String code,String channelNum) {
        HttpManager.getInstance().getRegiter(phone,pass,code,channelNum, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if(httpDataInfoResult.getCode()==0){
                    YsdkUtils.loginResult = httpDataInfoResult;
                    UserUtils.USER_ID = httpDataInfoResult.getData().getAppUser().getUSER_ID();
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, "0");
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, true);
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_USERID, UserUtils.USER_ID);
                    MyToast.getToast(getApplicationContext(), "注册成功！").show();
                    Intent intent = new Intent(RegisterOneActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    MyToast.getToast(getApplicationContext(), httpDataInfoResult.getMsg()).show();
                }
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage());
            }
        });
    }

    private void getCode() {
        String phone=et_phone.getText().toString();
        if(phone.isEmpty()){
            MyToast.getToast(this,"请输入手机号").show();
            return;
        }if(!Utils.checkPhoneREX(phone)){
            MyToast.getToast(this,"手机号格式有误").show();
            return;
        }
        myCountDownTimer.start();
        getCodeTask(phone);
    }

    private void getCodeTask(String phone) {
        String str = Base64.encodeToString(phone.getBytes(), Base64.DEFAULT);
        LogUtils.logi(str);
        HttpManager.getInstance().getCode(str, new RequestSubscriber<Result<Void>>() {
            @Override
            public void _onSuccess(Result<Void> token) {
                Log.e( "短信验证码------",token.toString());
                MyToast.getToast(RegisterOneActivity.this,"验证码已发送").show();
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage());
            }
        });
    }

    //倒计时
    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setTextColor(Color.RED);
            getCode.setText("倒计时("+millisUntilFinished/1000+")");
            getCode.setEnabled(false);
        }
        @Override
        public void onFinish() {
            getCode.setTextColor(Color.parseColor("#544d1e"));
            getCode.setText("重新获取");
            getCode.setEnabled(true);
        }
    }
}
