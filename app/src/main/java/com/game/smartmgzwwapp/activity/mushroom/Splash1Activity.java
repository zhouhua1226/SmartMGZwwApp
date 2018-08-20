package com.game.smartmgzwwapp.activity.mushroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.base.MyApplication;
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
import com.game.smartmgzwwapp.view.GifView;
import com.game.smartmgzwwapp.view.MyToast;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/8/8.
 */

public class Splash1Activity extends BaseActivity {
    private static final String TAG ="Splash1Activity" ;
    @BindView(R.id.rl_protocol)
    LinearLayout protocolView;
    @BindView(R.id.cb_protocol)
    CheckBox cb_protocol;
    @BindView(R.id.weiixn_loading_gv)
    GifView loginLoadingGv;
    private UMShareAPI mUMShareAPI;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash_one;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTranslucentStatus();
        initView();
        loginLoadingGv.setEnabled(false);
        loginLoadingGv.setMovieResource(R.raw.login_loadinggif);
        mUMShareAPI = UMShareAPI.get(this);
    }
    @OnClick({R.id.rl_weiixn_login,R.id.rl_phone_login})
    public void onViewClicked(View view) {
         if(!cb_protocol.isChecked()){
             MyToast.getToast(this, "您还没有同意用户协议与隐私条款").show();
             return;
         }
        switch (view.getId()) {
            case R.id.rl_weiixn_login:
                mUMShareAPI.doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);//微信授权
                setGifView(true);
                break;
            case R.id.rl_phone_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }
    }
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }
    @Override
    public void onBackPressed() {
        MyApplication.getInstance().exit();
    }

    /**
     * 应用授权
     * @param platform
     */
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e(TAG, platform.toString() + "--data==" + data.toString());
            getPlatformInfo(platform);
        }
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            setGifView(false);
        }
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            // Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
            setGifView(false);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUMShareAPI.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 获取用户信息
     * @param platform
     */
    private void getPlatformInfo(SHARE_MEDIA platform) {
        mUMShareAPI.getPlatformInfo(Splash1Activity.this, platform, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }
            @Override
            public void onComplete(SHARE_MEDIA platform, int i, Map<String, String> map) {
                if(map==null){
                    MyToast.getToast(getApplicationContext(),"登录失败！").show();
                    setGifView(false);
                    return;
                }
                String uid = null;
                String regChannel = null;
                if(platform.equals(SHARE_MEDIA.WEIXIN)){
                    uid=map.get("unionid");
                    regChannel="WEIXIN";
                }
                String name= map.get("screen_name");
                String gender= map.get("gender");
                String profile_image_url= map.get("profile_image_url");
                LogUtils.loge("个人信息刷新结果==uid=" + uid
                        + " regChannel=" + regChannel
                        + " name=" + name
                        + " profile_image_url=" + profile_image_url
                        + " gender=" + gender);
                wxLoginTask(uid,name,gender,profile_image_url,regChannel);
            }
            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                setGifView(false);
            }
            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                setGifView(false);
            }
        });
    }
    /**
     * 第三方登录
     */
    private void wxLoginTask( String uid, String name, String gender, String profile_image_url,String regChannel) {
        HttpManager.getInstance().wxRegister(uid,name,gender,profile_image_url,regChannel, ChannelUtils.getChannelNum(),
                new RequestSubscriber<Result<HttpDataInfo>>() {
                    @Override
                    public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                        setGifView(false);
                        if (httpDataInfoResult.getCode() == 0) {
                            YsdkUtils.loginResult = httpDataInfoResult;
                            UserUtils.USER_ID = httpDataInfoResult.getData().getAppUser().getUSER_ID();
                            SPUtils.putString(getApplicationContext(), UserUtils.SP_FIRET_CHARGE, httpDataInfoResult.getData().getAppUser().getFIRST_CHARGE());
                            SPUtils.putBoolean(getApplicationContext(), UserUtils.SP_TAG_LOGIN, true);
                            SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_USERID, UserUtils.USER_ID);
                            MyToast.getToast(getApplicationContext(), "登录成功！").show();
                            Utils.toActivity(Splash1Activity.this, HomeActivity.class);
                            finish();
                        } else {
                            MyToast.getToast(getApplicationContext(), httpDataInfoResult.getMsg()).show();
                        }
                    }

                    @Override
                    public void _onError(Throwable e) {
                        setGifView(false);
                        LogUtils.logi(e.getMessage());
                    }
                });

    }
    /**
     * 是否展示预加载
     *
     * @param isVisible
     */
    private void setGifView(boolean isVisible) {
        if (loginLoadingGv == null) {
            return;
        }
        if (isVisible) {
            loginLoadingGv.setVisibility(View.VISIBLE);
        } else {
            loginLoadingGv.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUMShareAPI.release();
    }
}
