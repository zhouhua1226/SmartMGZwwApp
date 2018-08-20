package com.game.smartmgzwwapp.activity.home;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.alipay.AlipayUtils;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.AlipayBean;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.NowPayBean;
import com.game.smartmgzwwapp.bean.OrderBean;
import com.game.smartmgzwwapp.bean.PayCardBean;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.view.MyLoading;
import com.game.smartmgzwwapp.view.MyToast;
import com.game.smartmgzwwapp.view.PayTypeDialog;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenw on 2018/7/19.
 */

public class CardDetailActivity extends BaseActivity implements ReceivePayResult {
    private static final String TAG ="CardDetailActivity==========" ;
    @BindView(R.id.card_detail_radiogroup )
    RadioGroup radiogroup;
    @BindView(R.id.radio_week_card )
    RadioButton radio_week;
    @BindView(R.id.radio_mouth_card )
    RadioButton radio_mouth;
    @BindView(R.id.iv_card_bg )
    ImageView card_bg;
    @BindView(R.id.tv_card_money )
    TextView card_money;
    @BindView(R.id.tv_card_time )
    TextView card_time;
    @BindView(R.id.iv_card_buy )
    Button card_buy;

    @BindView(R.id.tv_detail_one )
    TextView detail_one;
    @BindView(R.id.tv_detail_two )
    TextView detail_two;
    @BindView(R.id.tv_detail_three )
    TextView detail_three;
    private PayCardBean mWeek=null;
    private  PayCardBean  mMouth= null;
    private int type=1;
    private  String payOutType;
    private IpaynowPlugin mIpaynowplugin;
    private MyLoading mLoadingDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_card_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setStatusBarColor(R.color.white);
        initView();

        mIpaynowplugin = IpaynowPlugin.getInstance().init(this);// 1.插件初始化
        mLoadingDialog = new MyLoading(mIpaynowplugin.getDefaultLoading());
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        type=getIntent().getIntExtra(UrlUtils.CARD_TYPE,0);
        getPayCardList();
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case  R.id.radio_mouth_card:
                        initMouthView();
                        break;
                    case  R.id.radio_week_card:
                        initWeekView();
                        break;
                }
            }
        });

        if(type>1){
            radio_mouth.setChecked(true);
            initMouthView();
        }else{
            radio_week.setChecked(true);
            initWeekView();
        }
    }

    private void initWeekView() {
        card_bg.setBackgroundResource(R.drawable.icon_week_detail);
        card_time.setText(getResources().getString(R.string.week_take_effect));
        String s1 = "购买周卡后一共可获得" + "<font color='#FF0000'>" + "420" + "</font>"+"游戏币,可立即获得"+ "<font color='#FF0000'>" + "280" + "</font>"+"游戏币";
        String s2 = "有效期内每天赠送" + "<font color='#FF0000'>" + "20" + "</font>"+"游戏币，7天内共赠送"+ "<font color='#FF0000'>" + "140" + "</font>"+"游戏币";
        String s3 = "重复购买周卡，奖励不会叠加，而是会延顺到下一个周期";
        detail_one.setText(Html.fromHtml(s1));
        detail_two.setText(Html.fromHtml(s2));
        detail_three.setText(Html.fromHtml(s3));
        if(mWeek!=null) {
            card_money.setText(mWeek.getAMOUNT());
        }
        payOutType="wc";
        type=0;
    }

    private void initMouthView() {
        card_bg.setBackgroundResource(R.drawable.icon_mouth_detail);
        card_time.setText(getResources().getString(R.string.mouth_take_effect));
        String s1 = "购买月卡后一共可获得" + "<font color='#FF0000'>" + "1980" + "</font>"+"游戏币,可立即获得"+ "<font color='#FF0000'>" + "980" + "</font>"+"游戏币";
        String s2 = "有效期内每天赠送" + "<font color='#FF0000'>" + "33" + "</font>"+"游戏币"
                + "\n30天内共赠送"+ "<font color='#FF0000'>" + "990" + "</font>"+"游戏币";
        String s3 = "重复购买月卡，奖励不会叠加，而是会延顺到下一个周期";
        detail_one.setText(Html.fromHtml(s1));
        detail_two.setText(Html.fromHtml(s2));
        detail_three.setText(Html.fromHtml(s3));
        if(mMouth!=null) {
            card_money.setText(mMouth.getAMOUNT());
        }
        payOutType="mc";
        type=2;
    }

    @OnClick({R.id.image_back,R.id.iv_card_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.iv_card_buy:
                if(mWeek!=null&&mMouth!=null) {
                    if (type > 1) {
                        getPayTypeDialog(mMouth.getID());
                    } else {
                        getPayTypeDialog(mWeek.getID() );
                    }
                }
                break;
        }
    }

    /**
     *选择支付方式
     */
    private void getPayTypeDialog(final int pid ) {
        PayTypeDialog mPayTypeDialog = new PayTypeDialog(this, R.style.activitystyle);
        mPayTypeDialog.show();
        mPayTypeDialog.setDialogResultListener(new PayTypeDialog.DialogResultListener() {
            @Override
            public void getResult(boolean payChannelType) {
                if(payChannelType){ //支付宝支付
                    getNowPayOrder(pid,"13");
                }else{
                    getOrderInfo(pid);
                }
            }
        });
    }

    /**
     *获取微信订单信息
     */
    private void getNowPayOrder( int pid,  String payChannelType) {
        HttpManager.getInstance().getNowWXPayOrder(UserUtils.USER_ID, pid+"",payChannelType,
                payOutType, new RequestSubscriber<NowPayBean<OrderBean>>() {
                    @Override
                    public void _onSuccess(NowPayBean<OrderBean> result) {
                        if (result.getCode() == 0) {
                            mIpaynowplugin.setCustomLoading(mLoadingDialog).setCallResultReceiver(CardDetailActivity.this).pay(result.getNowpayData());
                        }
                    }
                    @Override
                    public void _onError(Throwable e) {
                        LogUtils.logi(e.getMessage());
                    }
                });
    }


    /**
     *获取支付宝订单信息
     */
    private void getOrderInfo(int pid) {
        HttpManager.getInstance().getOrderAlipay(UserUtils.USER_ID, pid+"",
                payOutType, new RequestSubscriber<Result<AlipayBean>>() {
                    @Override
                    public void _onSuccess(Result<AlipayBean> result) {
                        if (result.getCode() == 0) {
                            startPay(result.getData().getAlipay()); //调用支付宝支付接口
                        }
                    }

                    @Override
                    public void _onError(Throwable e) {
                        LogUtils.logi(e.getMessage());
                    }
                });
    }


    /**
     * 调取支付支付界面
     */
    private void startPay(String orderInfo) {
        AlipayUtils.startApliyPay(this, orderInfo, new AlipayUtils.OnApliyPayResultListenter() {
            @Override
            public void OnApliyPayResult(boolean isSuccess) {
                if(isSuccess){

                }
            }
        });
    }

    //获取充值卡列表
    public void getPayCardList() {
        HttpManager.getInstance().getPayCardList(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getCode()==0) {
                    List<PayCardBean> mylist = httpDataInfoResult.getData().getPaycard();
                    for (PayCardBean mPayCardBean:mylist) {
                        if(mPayCardBean.getID()==8){//周卡
                            mWeek=mPayCardBean;
                        }else if(mPayCardBean.getID()==9){//月卡
                            mMouth=mPayCardBean;
                        }
                    }
                    initData();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    private void initData() {
        if(type>1){
            if(mMouth!=null){
                card_money.setText(mMouth.getAMOUNT());
            }
        }else{
            if(mWeek!=null){
                card_money.setText(mWeek.getAMOUNT());
            }
        }

    }

    @Override
    public void onIpaynowTransResult(ResponseParams responseParams) {
        String respCode = responseParams.respCode;
        String errorCode = responseParams.errorCode;
        String errorMsg = responseParams.respMsg;
        StringBuilder temp = new StringBuilder();
        if (respCode.equals("00")) {
            MyToast.getToast(getApplicationContext(),"支付成功!").show();
        } else if (respCode.equals("02")) {
            MyToast.getToast(getApplicationContext(),"支付取消!").show();
        } else if (respCode.equals("01")) {
            MyToast.getToast(getApplicationContext(),"支付失败!").show();
            Log.e( TAG,"respCode:" + respCode+"respMsg:"+errorMsg);
        }  else {
            Log.e( TAG,"respCode:" + respCode+"respMsg:"+errorMsg);
        }
    }
}
