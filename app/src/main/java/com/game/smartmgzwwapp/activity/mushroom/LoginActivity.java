package com.game.smartmgzwwapp.activity.mushroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.activity.home.FindBackPassActivity;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
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

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginCodeActivity---";
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_password)
    EditText et_password;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTranslucentStatus();
        initView();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }



    @OnClick({R.id.image_back,R.id.bt_sure_login, R.id.tv_forget_password, R.id.tv_register})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.bt_sure_login:
                login();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, FindBackPassActivity.class));
                break;
            case R.id.tv_register:
                Utils.toActivity(this, RegisterOneActivity.class);
                break;

        }
    }

    private void login() {
        String phone = et_phone.getText().toString();
        String pass = et_password.getText().toString();
        if (phone.isEmpty()) {
            MyToast.getToast(this, "请输入手机号").show();
            return;
        }
        if (!Utils.checkPhoneREX(phone)) {
            MyToast.getToast(this, "手机号格式有误").show();
            return;
        }
        if (pass.isEmpty()) {
            MyToast.getToast(this, "请输入密码").show();
            return;
        }
        if (!Utils.isTextPassword(pass)) {
            MyToast.getToast(this, "密码6-20位数字和字母组合").show();
            return;
        }
        loginTask(phone, pass);
    }

    private void loginTask(final String phone, String pass) {
        HttpManager.getInstance().getLoginPassword(phone, pass, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getCode() == 0) {
                    YsdkUtils.loginResult = httpDataInfoResult;
                    UserUtils.USER_ID = httpDataInfoResult.getData().getAppUser().getUSER_ID();
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, httpDataInfoResult.getData().getAppUser().getFIRST_CHARGE());
                    SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, true);
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_USERID, UserUtils.USER_ID);
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_PHONE, phone);
                    MyToast.getToast(getApplicationContext(), "登录成功！").show();
                    Utils.toActivity(LoginActivity.this, HomeActivity.class);
                    finish();
                } else {
                    MyToast.getToast(getApplicationContext(), httpDataInfoResult.getMsg()).show();
                }
            }

            @Override
            public void _onError(Throwable e) {
                LogUtils.logi(e.getMessage());
            }
        });
    }

}
