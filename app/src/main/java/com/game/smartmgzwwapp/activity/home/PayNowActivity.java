package com.game.smartmgzwwapp.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.alipay.AlipayUtils;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.AlipayBean;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mi on 2018/5/2.
 */

public class PayNowActivity extends BaseActivity {

    @BindView(R.id.ll_rightBtn)
    RelativeLayout ll_rightBtn;
    @BindView(R.id.iv_leftBtn)
    ImageView ivRightBtn;

    @BindView(R.id.et_pay_amount)
    EditText etAmount;
    @BindView(R.id.tv_user_blance)
    TextView userBlance;
    @BindView(R.id.tv_user_account)
    TextView userAccount;
    @BindView(R.id.rb_aliapy_pay)
    RadioButton rb_aliapy;
    private  int payType=0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_now;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void initView() {
        setLeftBtnDefaultOnClickListener();
        setTitle("充值");
        ivRightBtn.setVisibility(View.VISIBLE);
        ivRightBtn.setBackgroundResource(R.drawable.kf1);
        ll_rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Utils.toActivity(PayNowActivity.this,ServiceActivity.class);
            }
        });

        userAccount.setText(UserUtils.UserName);
        userBlance.setText(UserUtils.UserBalance);
        rb_aliapy.setChecked(true);
    }

    @OnClick({ R.id.rl_pay_alipay,R.id.bt_sure_pay })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_pay_alipay:
                rb_aliapy.setChecked(!rb_aliapy.isChecked());
                break;
            case R.id.bt_sure_pay:
                String amount= etAmount.getText().toString();
                if(amount.isEmpty()){
                    MyToast.getToast(PayNowActivity.this,"请输入充值金额").show();
                    return;
                }
                if(Long.parseLong(amount)<10){
                MyToast.getToast(PayNowActivity.this,"充值金额最少10元").show();
                return;
               }
                if(!rb_aliapy.isChecked()){
                    MyToast.getToast(PayNowActivity.this,"请选择充值方式").show();
                    return;
                }
                getOrderInfo(amount);
                break;
        }
    }

    /**
     * 获取支付宝支付信息
     * @param amount
     */
    private void getOrderInfo(String amount) {
        HttpManager.getInstance().getTradeOrderAlipay(UserUtils.USER_ID, amount, UserUtils.USER_ID, amount, new RequestSubscriber<Result<AlipayBean>>() {
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
                if (isSuccess) {
                    getAppUserInf();
                }
            }
        });
    }
    /**
     * 刷新游戏币
     */
    private void getAppUserInf() {
        HttpManager.getInstance().getAppUserInf(UserUtils.USER_ID, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if(result.getData().getAppUser()==null){
                    return;
                }
                UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                userBlance.setText(UserUtils.UserBalance);
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

}

