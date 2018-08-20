package com.game.smartmgzwwapp.activity.home;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.activity.mushroom.HomeActivity;
import com.game.smartmgzwwapp.activity.mushroom.Splash1Activity;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.base.MyApplication;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.PermissionsUtils;
import com.game.smartmgzwwapp.utils.SPUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.utils.YsdkUtils;
import com.game.smartmgzwwapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.game.smartmgzwwapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;


/**
 * Created by chenw on 2018/7/19.
 */
public class WelcomeActivity extends BaseActivity {

    private static final String TAG ="WelcomeActivity-----" ;
    private String uid;
    @BindView(R.id.rl_mlayout)
    RelativeLayout mlayout;

    private ImageView mImageView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        getAppVersion();
        initWelcome();
    }
    private void getAppVersion() {
        Utils.appVersion = Utils.getAppCodeOrName(this, 0);
        Utils.osVersion = Utils.getSystemVersion();
        Utils.deviceType = Utils.getDeviceBrand();
        Utils.IMEI = Utils.getIMEI(this);
    }
    @Override
    protected void initView() {
        ButterKnife.bind(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mImageView = new ImageView(getApplicationContext());
        mImageView.setLayoutParams(params);
        mlayout.addView(mImageView);
        mImageView.setBackgroundResource(R.mipmap.icon_app_numshroom_start);
    }

    private void initWelcome() {
       //  fresh();
        if (SPUtils.getBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, false)) {
            //用户已经注册
            uid =  SPUtils.getString(getApplicationContext(), UserUtils.SP_TAG_USERID, "");
            if (Utils.isEmpty(uid)) {
                toActivity(false);
                return;
            }
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                getAuthLogin(uid);
                return;
            } else {
                toActivity(false);
                MyToast.getToast(getApplicationContext(), "请查看你的网络！").show();
            }
        }
        toActivity(false);
    }
   private void  setPermissions(){
       PermissionsUtils.checkPermissions(this, new String[]{
               Manifest.permission.READ_PHONE_STATE},
               PERMISSIOM_EXTERNAL_STORAGE, new PermissionsUtils.PermissionsResultListener() {
                   @Override
                   public void onSuccessful() {
                       getAppVersion();
                   }
                   @Override
                   public void onFailure() {

                   }
               });
       }


    /**
     *
     * 自动登录
     * @param userId
     */
    private void getAuthLogin(String userId) {
        HttpManager.getInstance().getAuthLogin(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult == null || loginInfoResult.getData() == null
                        || loginInfoResult.getData().getAppUser() == null) {
                    toActivity(false);
                    return;
                }
                if (loginInfoResult.getMsg().equals("success")) {
                    YsdkUtils.loginResult = loginInfoResult;
                    SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, loginInfoResult.getData().getAppUser().getFIRST_CHARGE());
                    UserUtils.USER_ID = loginInfoResult.getData().getAppUser().getUSER_ID();
                    UserUtils.isUserChanger = true;
                    toActivity(true);
                }
            }
            @Override
            public void _onError(Throwable e) {
                toActivity(false);
            }
        });
    }

    private boolean isFirstInto() {
        // 判断是否第一次打开app
        boolean user_first = SPUtils.getBoolean(getApplicationContext(),"FIRST", true);
        if (user_first) {
            SPUtils.putBoolean(getApplicationContext(),"FIRST", false);
        }
        return user_first;
    }
    private void  toActivity(boolean isLogin){
        if (isLogin ) {
            Utils.toActivity(WelcomeActivity.this, HomeActivity.class);
            finish();
        } else {
            Utils.toActivity(WelcomeActivity.this, Splash1Activity.class);//蘑菇抓娃娃
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mImageView != null) {
            ((ViewGroup) mImageView.getParent()).removeView(mImageView);
            mImageView = null;
        }
        super.onDestroy();
    }

    //重写返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            MyApplication.getInstance().exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
