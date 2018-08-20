package com.game.smartmgzwwapp.activity.mushroom;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.base.MyApplication;
import com.game.smartmgzwwapp.bean.AppInfo;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.RoomBean;
import com.game.smartmgzwwapp.bean.RoomListBean;
import com.game.smartmgzwwapp.fragment.mushroom.CenterFragment;
import com.game.smartmgzwwapp.fragment.mushroom.HomeFragment;
import com.game.smartmgzwwapp.fragment.mushroom.RankCatchFragment;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.model.http.download.DownloadManagerUtil;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.SPUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.utils.VersionUtils;
import com.game.smartmgzwwapp.utils.YsdkUtils;
import com.game.smartmgzwwapp.view.MyToast;
import com.game.smartmgzwwapp.view.SignInDialog;
import com.game.smartmgzwwapp.view.SignSuccessDialog;
import com.game.smartmgzwwapp.view.UpdateDialog;
import com.gatz.netty.AppClient;
import com.gatz.netty.utils.AppProperties;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mi on 2018/8/8.
 */

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "HomeActivity-";
    @BindView(R.id.tab_radiogroup)
    RadioGroup mMainTabRadioGroup;
    @BindView(R.id.tab_home_bg)
    RadioButton tab_home;
    @BindView(R.id.tab_rank)
    RadioButton tab_rank;
    @BindView(R.id.tab_mine)
    RadioButton tab_mine;

    private HomeFragment zwwjFragment;//抓娃娃
    private RankCatchFragment rankFragment;//排行榜
    private CenterFragment myCenterFragment;//我的
    private long mExitTime;
    private List<RoomBean> roomList = new ArrayList<>();
    private Result<HttpDataInfo> loginInfoResult;
    private int signNumber = 0;
    private int[] signDayNum = new int[7];
    private String isSign = "";
    private SignInDialog signInDialog;
    private DownloadManagerUtil downloadManagerUtil;
    private long downloadId = 0;
    public static HomeActivity mMainActivity;
    private int lastIndex = 0;
    private FragmentTransaction transaction;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    static {
        System.loadLibrary("SmartPlayer");
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTranslucentStatus();
        initView();
        mMainActivity = this;
        initFragment();
        getDollList();          //获取房间列表
        initNetty();
        RxBus.get().register(this);
        initData();

    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    private void initData() {
        SPUtils.putBoolean(getApplicationContext(), "isVibrator", true);
        SPUtils.putBoolean(getApplicationContext(), "isOpenMusic", true);
        UserUtils.isUserChanger = false;
        getUserSign(UserUtils.USER_ID, "0"); //签到请求 0 查询签到信息 1签到
    }

    private void initNetty() {
        doServcerConnect();
        NettyUtils.registerAppManager();
    }

    private void initDoConnect() {
        if ((YsdkUtils.loginResult != null) && (zwwjFragment != null)) {
            UserUtils.NickName = YsdkUtils.loginResult.getData().getAppUser().getNICKNAME();
            UserUtils.USER_ID = YsdkUtils.loginResult.getData().getAppUser().getUSER_ID();
            zwwjFragment.setSessionId(YsdkUtils.loginResult.getData().getSessionID(), false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoginBackDate();             //登录信息返回
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.isExit = true;
        RxBus.get().unregister(this);
    }

    private void getLoginBackDate() {
        loginInfoResult = YsdkUtils.loginResult;
        if (loginInfoResult != null && !loginInfoResult.equals("")) {
            if (loginInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                LogUtils.logi("logIn::::" + loginInfoResult.getMsg());
                Utils.token = loginInfoResult.getData().getAccessToken();
                UserUtils.sessionID = loginInfoResult.getData().getSessionID();
                UserUtils.SRSToken = loginInfoResult.getData().getSRStoken();
                //用户手机号
                UserUtils.UserPhone = loginInfoResult.getData().getAppUser().getBDPHONE();
                //用户名  11/22 13：25
                UserUtils.UserName = loginInfoResult.getData().getAppUser().getUSERNAME();
                UserUtils.NickName = loginInfoResult.getData().getAppUser().getNICKNAME();
                //用户余额
                UserUtils.UserBalance = loginInfoResult.getData().getAppUser().getBALANCE();
                //用户头像  11/22 13：25
                UserUtils.UserImage = UrlUtils.APPPICTERURL + loginInfoResult.getData().getAppUser().getIMAGE_URL();
                UserUtils.UserCatchNum = loginInfoResult.getData().getAppUser().getDOLLTOTAL();
                UserUtils.DOLL_ID = loginInfoResult.getData().getAppUser().getDOLL_ID();
                UserUtils.USER_ID = loginInfoResult.getData().getAppUser().getUSER_ID();
                UserUtils.UserAddress = loginInfoResult.getData().getAppUser().getCNEE_NAME() + " " +
                        loginInfoResult.getData().getAppUser().getCNEE_PHONE() + " " +
                        loginInfoResult.getData().getAppUser().getCNEE_ADDRESS();
            }
        } else {
            if (zwwjFragment != null) {
                zwwjFragment.showError();
            }
        }
    }
//重写返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(HomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            MyApplication.getInstance().exit();
        }
    }

    private void doServcerConnect() {
        AppClient.getInstance().setHost(UrlUtils.SOCKET_IP);
        AppClient.getInstance().setPort(8580);
        if (!AppProperties.initProperties(getResources())) {
            LogUtils.loge("netty初始化配置信息出错");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyUtils.socketConnect(getResources(), getApplicationContext());
            }
        }).start();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (UserUtils.isUserChanger) { //账号切换
            UserUtils.isUserChanger = false;
            if ((YsdkUtils.loginResult.getData() != null) && (zwwjFragment != null)) {
                UserUtils.NickName = YsdkUtils.loginResult.getData().getAppUser().getNICKNAME();
                UserUtils.USER_ID = YsdkUtils.loginResult.getData().getAppUser().getUSER_ID();
                if (YsdkUtils.loginResult.getData().getSessionID() != null)
                    zwwjFragment.setSessionId(YsdkUtils.loginResult.getData().getSessionID(), false);
            }
            getUserSign(UserUtils.USER_ID, "0"); //签到请求 0 查询签到信息 1签到
        } else {

            NettyUtils.pingRequest();
        }
    }

    //监控网关区
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_SESSION_INVALID),
            @Tag(Utils.TAG_CONNECT_ERR),
            @Tag(Utils.TAG_GATEWAT_USING),
            @Tag(Utils.TAG_CONNECT_SUCESS)})
    public void getConnectStates(String state) {
        if (state.equals(Utils.TAG_CONNECT_ERR)) {
            LogUtils.loge("TAG_CONNECT_ERR");
        } else if (state.equals(Utils.TAG_CONNECT_SUCESS)) {
            LogUtils.loge("TAG_CONNECT_SUCESS");
            //getDeviceStates();
        } else if (state.equals(Utils.TAG_SESSION_INVALID)) {
            LogUtils.loge("TAG_SESSION_INVALID");
            //TODO 重连后重新连接 QQ/WEIXIN 模式检测
            getAuthLogin(UserUtils.USER_ID, YsdkUtils.access_token, UrlUtils.LOGIN_CTYPE, UrlUtils.LOGIN_CHANNEL);
        } else if (state.equals(Utils.TAG_GATEWAT_USING)) {
            LogUtils.loge("TAG_GATEWAT_USING");
        }
    }

    /**
     * ####################### 网络请求区 #########################
     **/

    //自动登录
    private void getAuthLogin(String userId, String accessToken, String ctype, String channel) {
        HttpManager.getInstance().getAuthLogin(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                Log.e(TAG, "断开重连 重新获取相关参数" + loginInfoResult.getMsg());
                if (loginInfoResult.getMsg().equals("success")) {
                    if ((zwwjFragment != null) && (loginInfoResult.getData() != null)) {
                        zwwjFragment.setSessionId(loginInfoResult.getData().getSessionID(), true);
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    //房间列表
    private void getDollList() {
        HttpManager.getInstance().getDollList(new RequestSubscriber<Result<RoomListBean>>() {
            @Override
            public void _onSuccess(Result<RoomListBean> roomListBean) {
                if (zwwjFragment != null)
                    zwwjFragment.dismissEmptyLayout();
                if (roomListBean.getMsg().equals("success")) {
                    if (roomListBean.getData() == null) {
                        return;
                    }
                    roomList = roomListBean.getData().getDollList();
                    if (roomList.size() == 0) {
                        if (zwwjFragment != null)
                            zwwjFragment.showError();
                    } else {
                        if (zwwjFragment != null) {
                            for (int i = 0; i < roomList.size(); i++) {
                                RoomBean bean = roomList.get(i);
                                bean = UserUtils.dealWithRoomStatus(bean, bean.getDollState());
                                roomList.set(i, bean);
                            }
                            //TODO 按照规则重新排序
                            Collections.sort(roomList, new Comparator<RoomBean>() {
                                @Override
                                public int compare(RoomBean t1, RoomBean t2) {
                                    return t2.getDollState().compareTo(t1.getDollState());
                                }
                            });
                            zwwjFragment.notifyAdapter(roomList, roomListBean.getData().getPd().getTotalPage());
                        }
                    }
                    initDoConnect();
                }
            }

            @Override
            public void _onError(Throwable e) {
                if (zwwjFragment != null)
                    zwwjFragment.showError();
            }
        });
    }

    private void setSignInDialog(int[] num) {
        signInDialog = new SignInDialog(this, R.style.easy_dialog_style);
        signInDialog.setCancelable(true);
        signInDialog.show();
        signInDialog.setBackGroundColor(num);
        signInDialog.setDialogResultListener(new SignInDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                switch (resultCode) {
                    case 0:
                        getUserSign(UserUtils.USER_ID, "1");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getSignSuccessDialog(String gold) {
        SignSuccessDialog signSuccessDialog = new SignSuccessDialog(this, R.style.easy_dialog_style);
        signSuccessDialog.setCancelable(true);
        signSuccessDialog.show();
        signSuccessDialog.setTextView(gold);
        signSuccessDialog.setDialogResultListener(new SignSuccessDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if (resultCode == 0 && signInDialog != null) {
                    signInDialog.dismiss();
                }
            }
        });
    }

    //签到请求
    private void getUserSign(String userId, final String signType) {
        HttpManager.getInstance().getUserSign(userId, signType, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getMsg().equals("success")) {
                    if (signType.equals("0")) {
                        //查询处理
                        isSign = loginInfoResult.getData().getSign().getSIGN_TAG();
                        signNumber = Integer.parseInt(loginInfoResult.getData().getSign().getCSDATE());
                        LogUtils.logi("签到天数=" + signNumber);
                        for (int i = 0; i < 7; i++) {
                            if (i < signNumber) {
                                signDayNum[i] = 1;
                            } else {
                                signDayNum[i] = 0;
                            }
                        }
                        if (signNumber < 7) {
                            if (isSign.equals("0")) {
                                setSignInDialog(signDayNum);
                            }
                        }
                    } else {
                        //签到处理
                        String signgold = loginInfoResult.getData().getSign().getSIGNGOLD();
                        LogUtils.logi("签到赠送金币" + signgold);
                        getSignSuccessDialog(signgold);
                        signNumber += 1;
                        for (int i = 0; i < 7; i++) {
                            if (i < signNumber) {
                                signDayNum[i] = 1;
                            } else {
                                signDayNum[i] = 0;
                            }
                        }
                        signInDialog.setBackGroundColor(signDayNum);
                        signInDialog.isSignedView(true);
                    }
                } else {
                    MyToast.getToast(getApplicationContext(), loginInfoResult.getMsg()).show();
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    /**
     * 获取apk版本信息
     */
    private void checkVersion() {
        HttpManager.getInstance().checkVersion(new RequestSubscriber<Result<AppInfo>>() {
            @Override
            public void _onSuccess(Result<AppInfo> appInfoResult) {
                if (appInfoResult != null) {
                    String version = appInfoResult.getData().getVERSION();
                    if (VersionUtils.validateVersion(Utils.getAppCodeOrName(HomeActivity.this, 1), version)) {
                        updateApp(appInfoResult.getData().getDOWNLOAD_URL());
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {
            }
        });

    }

    private void updateApp(final String loadUri) {
        UpdateDialog updateDialog = new UpdateDialog(this, R.style.easy_dialog_style);
        updateDialog.setCancelable(false);
        updateDialog.show();
        updateDialog.setDialogResultListener(new UpdateDialog.DialogResultListener() {
            @Override
            public void getResult(boolean result) {
                if (result) {// 确定下载
                    downloadManagerUtil = new DownloadManagerUtil(getApplicationContext());
                    if (downloadId != 0) {
                        downloadManagerUtil.clearCurrentTask(downloadId);
                    }
                    if (downloadManagerUtil.isDownloadManagerAvailable()) {
                        downloadId = downloadManagerUtil.download(UrlUtils.APPPICTERURL + loadUri);
                    }
                }
            }
        });
    }

    private void initFragment() {
        Utils.setDrawableSize(this, tab_rank);
        Utils.setDrawableSize(this, tab_mine);
        Utils.setDrawableSize(this, tab_home);
        mMainTabRadioGroup.setOnCheckedChangeListener(this);
        selectFragment(lastIndex);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int current = 0;
        switch (checkedId) {
            case R.id.tab_home_bg:
                current = 0;
                break;
            case R.id.tab_rank:
                current = 1;
                break;
            case R.id.tab_mine:
                current = 2;
                break;
        }
        if (lastIndex != current) {
            selectFragment(current);
        }
    }

    private void selectFragment(int index) {
        transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (zwwjFragment == null) {
                    zwwjFragment = new HomeFragment();
                    transaction.add(R.id.show_fragment, zwwjFragment);
                } else {
                    transaction.show(zwwjFragment);
                }

                break;
            case 1:
                if (rankFragment == null) {
                    rankFragment = new RankCatchFragment();
                    transaction.add(R.id.show_fragment, rankFragment);
                } else {
                    transaction.show(rankFragment);
                }
                break;
            case 2:
                if (myCenterFragment == null) {
                    myCenterFragment = new CenterFragment();
                    transaction.add(R.id.show_fragment, myCenterFragment);
                } else {
                    transaction.show(myCenterFragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
        lastIndex = index;
    }

    //隐藏fragment
    public void hideFragment(FragmentTransaction fragmentTransaction) {
        if (zwwjFragment != null) {
            fragmentTransaction.hide(zwwjFragment);
        }
        if (rankFragment != null) {
            fragmentTransaction.hide(rankFragment);
        }
        if (myCenterFragment != null) {
            fragmentTransaction.hide(myCenterFragment);
        }
    }
}