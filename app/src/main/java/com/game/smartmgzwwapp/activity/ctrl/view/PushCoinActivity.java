package com.game.smartmgzwwapp.activity.ctrl.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.activity.ctrl.presenter.CtrlCompl;
import com.game.smartmgzwwapp.activity.home.CoinRecordActivity;
import com.game.smartmgzwwapp.activity.home.RechargeActivity;
import com.game.smartmgzwwapp.bean.CoinPusher;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.UserBean;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.CatchDollResultDialog;
import com.game.smartmgzwwapp.view.GifView;
import com.game.smartmgzwwapp.view.GlideCircleTransform;
import com.game.smartmgzwwapp.view.MyToast;
import com.game.smartmgzwwapp.view.PushCoinView;
import com.gatz.netty.global.AppGlobal;
import com.gatz.netty.global.ConnectResultEvent;
import com.gatz.netty.utils.NettyUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.iot.game.pooh.server.entity.json.CoinControlResponse;
import com.iot.game.pooh.server.entity.json.app.AppInRoomResponse;
import com.iot.game.pooh.server.entity.json.app.AppOutRoomResponse;
import com.iot.game.pooh.server.entity.json.enums.CoinStatusType;
import com.iot.game.pooh.server.entity.json.enums.ReturnCode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PushCoinActivity extends Activity implements IctrlView {
    private static final String TAG = "PushCoinActivity";
    @BindView(R.id.player_counter_tv)
    TextView player_counter;//在线人数
    @BindView(R.id.player2_iv)
    ImageView player2_iv;//在线人头像
    @BindView(R.id.player_name_tv)
    TextView player_name;//玩家
    @BindView(R.id.main_player_iv)
    ImageView player_iv;//玩家头像
    @BindView(R.id.ctrl_status_iv)
    ImageView ctrl_status;//链接状态
    @BindView(R.id.coin_recharge)
    TextView coin_recharge;//充值
    @BindView(R.id.ctrl_comerecord_tv)
    TextView comerecord;//投币记录
    @BindView(R.id.ctrl_gif_view)
    GifView ctrlGifView;
    @BindView(R.id.ctrl_fail_iv)
    ImageView ctrlFailIv;
    @BindView(R.id.coin_button1)
    ImageView coinBtn1;
    @BindView(R.id.coin_button5)
    ImageView coinBtn5;
    @BindView(R.id.coin_button10)
    ImageView coinBtn10;
    @BindView(R.id.coin_button20)
    ImageView coinBtn20;
    @BindView(R.id.coin_button50)
    ImageView coinBtn50;
    @BindView(R.id.coin_push_btn)
    TextView coinPushBtn;
    @BindView(R.id.coin_response_text)
    TextView coinResponseText;
    @BindView(R.id.coin_play_video_sv)
    SurfaceView mPlaySv;
    @BindView(R.id.coin_gif_view)
    GifView coinGif;
    @BindView(R.id.push_day_coin)
    PushCoinView push_day_coin;
    private boolean isStartSend = false;
    private boolean isCurrentConnect = true;
    private String currentUrl;
    private String playUrlMain;
    private String playUrlSecond;
    private CtrlCompl ctrlCompl;
    private int coinNumber = 0;

    private String showUserId = "";
    private List<String> userInfos = new ArrayList<>();  //房屋内用户电话信息
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_coin);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initView() {
        RxBus.get().register(this);
        NettyUtils.sendRoomInCmd();

        ctrlGifView.setVisibility(View.VISIBLE);
        ctrlGifView.setEnabled(false);
        ctrlGifView.setMovieResource(R.raw.ctrl_video_loading);
        coinGif.setVisibility(View.GONE);
        coinGif.setEnabled(false);


        ctrlFailIv.setVisibility(View.GONE);
        if (Utils.connectStatus.equals(ConnectResultEvent.CONNECT_FAILURE)) {
            ctrl_status.setImageResource(R.drawable.point_red);
            isCurrentConnect = false;
        } else {
            ctrl_status.setImageResource(R.drawable.point_green);
        }
        Glide.with(this).load(UserUtils.UserImage).asBitmap().
                error(R.mipmap.icon_app_numshroom).
                transform(new GlideCircleTransform(this)).into(player_iv);

        player_name.setText(UserUtils.NickName + "···");
        NettyUtils.pingRequest(); //判断连接
        getUserDate(UserUtils.USER_ID);
        getUserSumCoin(UserUtils.USER_ID);
    }

    private void initData() {
        // dollName = getIntent().getStringExtra(Utils.TAG_ROOM_NAME);
        // dollId = getIntent().getStringExtra(Utils.TAG_DOLL_Id);

        playUrlMain = getIntent().getStringExtra(Utils.TAG_URL_MASTER);
        playUrlSecond = getIntent().getStringExtra(Utils.TAG_URL_SECOND);
        ctrlCompl = new CtrlCompl(this, this);
        currentUrl = playUrlMain;
        ctrlCompl.startPlayVideo(mPlaySv, currentUrl);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Utils.showLogE(TAG, "onRestart");
        if (ctrlFailIv.getVisibility() == View.VISIBLE) {
            ctrlFailIv.setVisibility(View.GONE);
        }
        ctrlGifView.setVisibility(View.VISIBLE);
        ctrlCompl.startPlayVideo(mPlaySv, currentUrl);
        NettyUtils.sendRoomInCmd();
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getUserDate(UserUtils.USER_ID);    //2秒后获取用户余额并更新UI
                }
            }, 2000);

        }

    }

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ctrlGifView.setVisibility(View.GONE);
                    ctrlFailIv.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    ctrlGifView.setVisibility(View.GONE);
                    if (ctrlFailIv.getVisibility() == View.VISIBLE) {
                        ctrlFailIv.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        ctrlCompl.stopRecordView();
        ctrlCompl.stopPlayVideo();
        ctrlCompl.stopRecordView();
        ctrlCompl.stopTimeCounter();
        ctrlCompl.sendCmdOutRoom();
        ctrlCompl = null;

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ctrlCompl.stopRecordView();
        ctrlCompl.stopPlayVideo();
        ctrlCompl.stopRecordView();
        ctrlCompl.stopTimeCounter();
        ctrlCompl.sendCmdOutRoom();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void playBGMusic() {
          if (mediaPlayer != null && mediaPlayer.isPlaying()) {
               return;
           }
            mediaPlayer = MediaPlayer.create(this, R.raw.coin_down);
            // 设置音频流的类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

    }

    @OnClick({R.id.coin_button1, R.id.coin_button10, R.id.coin_button5,
            R.id.coin_button20, R.id.coin_button50})
    public void onViewCoinClicked(View view) {
        if (isStartSend) {
            MyToast.getToast(getApplicationContext(), "正在进行游戏,请稍后").show();
            return;
        }
        setCoinNormal();
        int mcoinNumber = 0;
        ImageView imageView = null;
        int drawable = 0;
        switch (view.getId()) {
            case R.id.coin_button1:
                imageView = coinBtn1;
                drawable = R.drawable.coin_1_s;
                mcoinNumber = 1;
                break;
            case R.id.coin_button5:
                imageView = coinBtn5;
                drawable = R.drawable.coin_5_s;
                mcoinNumber = 5;
                break;
            case R.id.coin_button10:
                imageView = coinBtn10;
                drawable = R.drawable.coin_10_s;
                mcoinNumber = 10;
                break;
            case R.id.coin_button20:
                imageView = coinBtn20;
                drawable = R.drawable.coin_20_s;
                mcoinNumber = 20;
                break;
            case R.id.coin_button50:
                imageView = coinBtn50;
                drawable = R.drawable.coin_50_s;
                mcoinNumber = 50;
                break;
        }
        if (judgeMoney(mcoinNumber)) {
            coinPushBtn.setEnabled(true);
            coinPushBtn.setText("投 币");
            coinPushBtn.setTextColor(Color.parseColor("#57515d"));
            imageView.setImageResource(drawable);
        } else {
            setCatchResultDialog(0);
        }
    }

    private boolean judgeMoney(int mCoinNumber) {
        int totalMoney = mCoinNumber * 10;
        if (Integer.parseInt(UserUtils.UserBalance) >= totalMoney) {
            coinNumber = mCoinNumber;
            return true;
        }
        coinNumber = 0;
        return false;
    }

    @OnClick({R.id.ctrl_back_imag, R.id.coin_recharge, R.id.ctrl_comerecord_tv,
            R.id.ctrl_change_camera_iv, R.id.ctrl_fail_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctrl_back_imag:
                finish();
                break;
            case R.id.ctrl_comerecord_tv://投币记录
                startActivity(new Intent(this, CoinRecordActivity.class));
                break;
            case R.id.coin_recharge://充值
                startActivity(new Intent(this, RechargeActivity.class));
                break;
            case R.id.ctrl_change_camera_iv:
                currentUrl = currentUrl.equals(playUrlMain) ? playUrlSecond : playUrlMain;
                ctrlCompl.startPlaySwitchUrlVideo(currentUrl);
                break;
            case R.id.ctrl_fail_iv:
                ctrlFailIv.setVisibility(View.GONE);
                ctrlGifView.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void setCoinNormal() {
        coinBtn1.setImageResource(R.drawable.coin_1_n);
        coinBtn5.setImageResource(R.drawable.coin_5_n);
        coinBtn10.setImageResource(R.drawable.coin_10_n);
        coinBtn20.setImageResource(R.drawable.coin_20_n);
        coinBtn50.setImageResource(R.drawable.coin_50_n);
    }

    private void setBtnEnabled(boolean isEnabled) {
        coinBtn1.setEnabled(isEnabled);
        coinBtn5.setEnabled(isEnabled);
        coinBtn10.setEnabled(isEnabled);
        coinBtn20.setEnabled(isEnabled);
        coinBtn50.setEnabled(isEnabled);
        coinPushBtn.setEnabled(false);
        coinPushBtn.setTextColor(Color.parseColor("#FFFFFF"));
        if (!isEnabled) {
            playBGMusic();
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
    }
   private void  pushCoinAnimat(){
       Movie  mMovie = Movie.decodeStream(getResources().openRawResource(
               R.raw.coin_out_gif));
       coinGif.setMovie(mMovie);
       int dur=mMovie.duration();
       coinGif.setVisibility(View.VISIBLE);
       //gif动画播放一次
       coinGif.setPaused(false);
       coinGif.postDelayed(new Runnable() {
           @Override
           public void run() {
               coinGif.setPaused(true);
               coinGif.setVisibility(View.GONE);
           }
       },dur);

   }
    @OnClick({R.id.coin_push_btn})
    public void onPushClick(View v) {
        switch (v.getId()) {
            case R.id.coin_push_btn:
                if (coinNumber != 0 && isCurrentConnect) {
                    NettyUtils.sendPushCoinCmd(NettyUtils.USER_PUSH_COIN_START, 0);
                    coinResponseText.setText(String.valueOf(0));
                    isStartSend = true;
                    NettyUtils.sendPushCoinCmd(NettyUtils.USER_PUSH_COIN_PLAY, coinNumber);
                    coinPushBtn.setText("投币中");
                    setBtnEnabled(false);
                    //刷新用户游戏币
                    int totalMoney = coinNumber * 10;
                    UserUtils.UserBalance=(Integer.parseInt(UserUtils.UserBalance)-totalMoney)+"";
                    coin_recharge.setText("  " + UserUtils.UserBalance + " 充值");
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_ROOM_IN),
            @Tag(Utils.TAG_ROOM_OUT),
            @Tag(Utils.TAG_COIN_RESPONSE)})
    public void getCoinDeviceResponse(Object response) {
        if (response instanceof CoinControlResponse) {
            CoinControlResponse coinControlResponse = (CoinControlResponse) response;
            ReturnCode code = coinControlResponse.getReturnCode();
            if (code.toString().equals(ReturnCode.SUCCESS.name())) {
                //TODO 结算
                Log.e(TAG, "结算中........");
                if (coinControlResponse.getCoinStatusType().name().equals(CoinStatusType.END.name())) {
                    if ((coinControlResponse.getBet() != null) && (coinControlResponse.getBingo() != null)) {
                        int bingo = coinControlResponse.getBingo();
                        coinResponseText.setText(String.valueOf(bingo));
                        String userId = coinControlResponse.getUserId();
                        isStartSend = false;
                        if (userId!=null&&userId.equals(UserUtils.USER_ID)) {
                            if (bingo > 0) {
                                pushCoinAnimat();
                                getUserDate(UserUtils.USER_ID);
                                getUserSumCoin(UserUtils.USER_ID);
                            }
                            Log.e(TAG, "结算完毕,玩家立刻变亮........");
                            //玩家变亮
                            setCoinNormal();
                            coinPushBtn.setText("投 币");
                            setBtnEnabled(true);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG, "结算完毕,观看者6s后立刻变亮........");
                                    if (!isStartSend) {
                                        setCoinNormal();
                                        coinPushBtn.setText("投 币");
                                        isStartSend = false;
                                        setBtnEnabled(true);
                                    } else {
                                        Log.e(TAG, "结算完毕,但是玩家再次使用了......");
                                    }

                                }
                            }, Utils.OTHER_PLAYER_DELAY_TIME);
                        }
                    }
                } else if (coinControlResponse.getCoinStatusType().name().equals(CoinStatusType.PLAY.name())) {
                    Log.e(TAG, "游戏开始中........"+coinControlResponse.toString());

                    String userId = coinControlResponse.getUserId();
                    if (!userId.equals(UserUtils.USER_ID)) {
                        //TODO 观察到点击play
                        //  Log.e(TAG, "观察到其他用户点击play");
                        coinPushBtn.setText("投币中");
                        isStartSend = true;
                        setBtnEnabled(false);

                    } else {
                        //TODO 本人点击play
                        Log.e(TAG, "本人点击play");
                    }
                }
            }
        } else if (response instanceof AppInRoomResponse) {
            AppInRoomResponse appInRoomResponse = (AppInRoomResponse) response;
            Log.e(TAG, "=====进房间" + appInRoomResponse.toString());
            String allUsers = appInRoomResponse.getAllUserInRoom(); //返回的UserId
            Boolean free = appInRoomResponse.getFree();
            long seq = appInRoomResponse.getSeq();

            if ((seq != -2) && (!Utils.isEmpty(allUsers))) {
                //TODO  我本人进来了
                ctrlCompl.sendGetUserInfos(allUsers, true);
                //是否玩家正在游戏中
                if (free) {
                    setCoinNormal();
                    coinPushBtn.setText("投 币");
                    isStartSend = false;
                    setBtnEnabled(true);
                } else {
                    coinPushBtn.setText("投币中");
                    isStartSend = true;
                    setBtnEnabled(false);
                }

            } else {
                boolean is = false;
                if (userInfos.size() == 1) {
                    is = true;
                }
                userInfos.add(appInRoomResponse.getUserId());
                getUserInfos(userInfos, is);
            }
        } else if (response instanceof AppOutRoomResponse)//退出房间
        {
            AppOutRoomResponse appOutRoomResponse = (AppOutRoomResponse) response;
            Utils.showLogE(TAG, appOutRoomResponse.toString());
            long seq = appOutRoomResponse.getSeq();
            if (seq == -2) {
                userInfos.remove(appOutRoomResponse.getUserId());
                if (appOutRoomResponse.getUserId().equals(showUserId)) {
                    getUserInfos(userInfos, true);
                } else {
                    getUserInfos(userInfos, false);
                }
            }

        }
    }


    //设备状态
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_COIN_DEVICE_STATE)})
    public void getCoinDeviceState(String state) {
        Utils.showLogE(TAG, "当前游戏机状态::::" + state);
        if (state.equals("cbusy")) { //游戏中

        } else if (state.equals("cfree")) {//休闲中
            setCoinNormal();
            coinPushBtn.setText("投 币");
            isStartSend = false;
            setBtnEnabled(true);
        }
    }

    @Override
    public void getTime(int time) {
    }

    @Override
    public void getTimeFinish() {
        ctrlCompl.stopTimeCounter();
    }

    @Override
    public void getRecordErrCode(int code) {
        LogUtils.loge("录制视频失败::::::" + code, TAG);
    }

    @Override
    public void getRecordSuecss() {
        LogUtils.loge("录制视频完毕......", TAG);
    }

    @Override
    public void getRecordAttributetoNet(String time, String fileName) {
        LogUtils.loge("视频上传的时间::::" + time + "=====" + fileName, TAG);
    }

    @Override
    public void getPlayerErcErrCode(int code) {
        LogUtils.loge("直播失败,错误码:::::" + code, TAG);
        uiHandler.sendEmptyMessage(0);
    }

    @Override
    public void getPlayerSucess() {
        LogUtils.loge("直播Sucess:::::", TAG);
        uiHandler.sendEmptyMessage(1);
    }

    @Override
    public void getVideoPlayConnect() {

    }

    @Override
    public void getVideoPlayStart() {

    }

    @Override
    public void getVideoStop() {

    }

    //监控网关区
    //TODO 网关重连过后 需要前端主动去获取一次网关的状态来最终判断网关是否存在!!!
    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag(Utils.TAG_CONNECT_ERR),
            @Tag(Utils.TAG_CONNECT_SUCESS)})
    public void getConnectStates(String state) {
        if (state.equals(Utils.TAG_CONNECT_ERR)) {
            Utils.showLogE(TAG, "TAG_CONNECT_ERR");
            ctrl_status.setImageResource(R.drawable.point_red);
            isCurrentConnect = false;
        } else if (state.equals(Utils.TAG_CONNECT_SUCESS)) {
            Utils.showLogE(TAG, "TAG_CONNECT_SUCESS");
            ctrl_status.setImageResource(R.drawable.point_green);
            NettyUtils.sendRoomInCmd();
            //TODO 后续修改获取网关状态接口
            NettyUtils.sendGetDeviceStatesCmd();
            isCurrentConnect = true;
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_GATEWAT_USING)})
    public void getGatwayStates(String tag) {
        //TODO 发送命令 网关在使用中
        Log.e(TAG, "网关在使用中........" + tag);
    }

    //监控单个网关连接区
    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_GATEWAY_SINGLE_DISCONNECT)})
    public void getSingleGatwayDisConnect(String id) {
        Utils.showLogE(TAG, "getSingleGatwayDisConnect id" + id);
        if (id.equals(AppGlobal.getInstance().getUserInfo().getRoomid())) {
            ctrl_status.setImageResource(R.drawable.point_red);
            isCurrentConnect = false;
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD,
            tags = {@Tag(Utils.TAG_GATEWAY_SINGLE_CONNECT)})
    public void getSingleGatwayConnect(String id) {
        Utils.showLogE(TAG, "getSingleGatwayConnect id" + id);
        if (id.equals(AppGlobal.getInstance().getUserInfo().getRoomid())) {
            ctrl_status.setImageResource(R.drawable.point_green);
            isCurrentConnect = true;
        }
    }

    @Override
    public void getUserInfos(List<String> list, boolean is) {
        //当前房屋的人数
        userInfos = list;
        int counter = userInfos.size();
        Utils.showLogE(TAG, "当前房屋的人数::::" + counter);
        if (counter > 0) {
            String s = (counter + 20) + "人在线";//线人数 默认20个
            player_counter.setText(s);
            if (counter == 1) {
                //显示自己
                Glide.with(this).load(UserUtils.UserImage).asBitmap().
                        transform(new GlideCircleTransform(this)).into(player2_iv);
            } else {
                if (is) {
                    //显示另外一个人
                    for (int i = 0; i < counter; i++) {
                        if (!userInfos.get(i).equals(UserUtils.USER_ID)) {
                            showUserId = userInfos.get(i);
                            Utils.showLogE(TAG, "显示观察者的userId::::" + showUserId);
                            getCtrlUserImage(showUserId);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void getCtrlUserImage(String userId) {
        HttpManager.getInstance().getUserDate(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getCode() == 0) {
                    UserBean bean = httpDataInfoResult.getData().getAppUser();
                    if ((bean != null) && (!Utils.isEmpty(bean.getIMAGE_URL()))) {
                        String showImage = UrlUtils.USERFACEIMAGEURL + bean.getIMAGE_URL();
                        Glide.with(getApplicationContext()).load(showImage)
                                .asBitmap().transform(new GlideCircleTransform(PushCoinActivity.this)).into(player2_iv);
                    } else {
                        Glide.with(getApplicationContext()).load(R.mipmap.icon_app_numshroom)
                                .asBitmap().
                                transform(new GlideCircleTransform(PushCoinActivity.this)).into(player2_iv);
                    }
                } else {
                    Glide.with(getApplicationContext()).load(R.mipmap.icon_app_numshroom)
                            .asBitmap().
                            transform(new GlideCircleTransform(PushCoinActivity.this)).into(player2_iv);

                }
            }

            @Override
            public void _onError(Throwable e) {
                Glide.with(getApplicationContext()).load(R.mipmap.icon_app_numshroom)
                        .asBitmap().transform(new GlideCircleTransform(PushCoinActivity.this)).into(player2_iv);
            }
        });
    }

    //获取用户信息接口
    private void getUserDate(String userId) {
        HttpManager.getInstance().getUserDate(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                Log.e(TAG, "获取结果=" + loginInfoResult.getMsg());
                if (loginInfoResult.getMsg().equals("success")) {
                    UserBean bean = loginInfoResult.getData().getAppUser();
                    if (bean != null) {
                        String balance = bean.getBALANCE();
                        //UserUtils.UserBetNum = bean.getBET_NUM();
                        if (!TextUtils.isEmpty(balance)) {
                            UserUtils.UserBalance = balance;
                            coin_recharge.setText("  " + UserUtils.UserBalance + " 充值");
                        }
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    //获取用户信息接口
    private void getUserSumCoin(String userId) {
        HttpManager.getInstance().getUserSumCoin(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getMsg().equals("success")) {
                    CoinPusher mCoinPusher = loginInfoResult.getData().getCoinPusher();
                    if (mCoinPusher != null) {
                       push_day_coin.setProgress(mCoinPusher.getSum());
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void setCatchResultDialog(final int index  ) {
        final CatchDollResultDialog catchDollResultDialog = new CatchDollResultDialog(this, R.style.activitystyle);
        catchDollResultDialog.setCancelable(false);
        catchDollResultDialog.show();
        switch (index) {
            case 0:
                catchDollResultDialog.setTitle("余额不足！");
                catchDollResultDialog.setContent("请充值。");
                catchDollResultDialog.setFail("取消充值");
                catchDollResultDialog.setSuccess("前往充值");
                catchDollResultDialog.setBackground(R.drawable.catchdialog_success_bg);
                break;
        }
        catchDollResultDialog.setDialogResultListener(new CatchDollResultDialog.DialogResultListener() {
            @Override
            public void getResult(int resultCode) {
                if(resultCode>0) {
                    switch (index) {
                        case 0:
                            Utils.toActivity(PushCoinActivity.this, RechargeActivity.class);
                            break;

                    }
                }
                catchDollResultDialog.dismiss();
            }
        });
    }
}
