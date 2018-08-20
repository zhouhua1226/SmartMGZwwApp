package com.game.smartmgzwwapp.activity.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yincong on 2018/3/28 15:00
 * 修改人：
 * 修改时间：
 * 类描述：找回密码
 */
public class FindBackPassActivity extends BaseActivity {

    @BindView(R.id.et_register_phone)
    EditText et_phone;
    @BindView(R.id.et_register_password)
    EditText et_password;
    @BindView(R.id.et_register_sure_password)
    EditText sure_password;
    @BindView(R.id.et_register_code)
    EditText register_code;
    @BindView(R.id.tv_get_code)
    TextView getCode;
    @BindView(R.id.btn_sure_register)
    Button btnSureRegister;

    private String TAG="FindBackPassActivity";
    private MyCountDownTimer myCountDownTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_findbackpass;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        myCountDownTimer =new MyCountDownTimer(60000,1000);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setLeftBtnDefaultOnClickListener();
        setTitle("找回密码");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_sure_register,R.id.tv_get_code})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.btn_sure_register:
                reSetTask();
                break;
            case R.id.tv_get_code:
                getCode();
                break;
        }
    }

    private void reSetTask() {
        String phone=et_phone.getText().toString();
        String password=et_password.getText().toString();
        String pass=sure_password.getText().toString();
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
        }if(!Utils.isTextPassword(pass)){
            MyToast.getToast(this,"密码6-20位数字和字母组合").show();
            return;
        }
        if(!password.equals(pass)){
            MyToast.getToast(this,"两次输入的密码不一致").show();
            return;
        }
        if(code.isEmpty()){
            MyToast.getToast(this,"请输入短信验证码").show();
            return;
        }
        reSetPassord(phone,code,password);
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
            getCode.setTextColor(Color.rgb(141,188,246));
            getCode.setText("重新获取");
            getCode.setEnabled(true);
        }
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
                MyToast.getToast(FindBackPassActivity.this,"验证码已发送").show();
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage());
            }
        });
    }

    private void reSetPassord(String phone,String code,String password){
        HttpManager.getInstance().getResetPass(phone, code, password, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if(httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)){
                    MyToast.getToast(getApplicationContext(),"修改成功！").show();
                    finish();
                }else {
                    MyToast.getToast(getApplicationContext(),"修改失败！").show();
                }
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getApplicationContext(),"网络异常！").show();
            }
        });
    }


}
