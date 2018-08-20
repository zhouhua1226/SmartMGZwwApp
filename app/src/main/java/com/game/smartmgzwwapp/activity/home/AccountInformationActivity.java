package com.game.smartmgzwwapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yincong on 2018/3/16 10:53
 * 修改人：
 * 修改时间：
 * 类描述：账户信息
 */
public class AccountInformationActivity extends BaseActivity {
    @BindView(R.id.image_back)
    ImageButton imageBack;
     @BindView(R.id.accountinfo_phone_tv)
     TextView accountinfoPhoneTv;
    @BindView(R.id.accountinfo_nickname_tv)
    TextView accountinfoNicknameTv;
    @BindView(R.id.accountinfo_username_et)
    EditText accountinfoUsernameEt;
    @BindView(R.id.accountinfo_idcard_et)
    EditText accountinfoIdcardEt;
    @BindView(R.id.accountinfo_bankname_et)
    EditText accountinfoBanknameEt;
    @BindView(R.id.accountinfo_bankplace_et)
    EditText accountinfoBankplaceEt;
    @BindView(R.id.accountinfo_branchbankname_et)
    EditText accountinfoBranchbanknameEt;
    @BindView(R.id.accountinfo_banknum_et)
    EditText accountinfoBanknumEt;
    @BindView(R.id.accountinfo_submit_btn)
    Button accountinfoSubmitBtn;
    @BindView(R.id.accountinfo_resetting_tv)
    TextView accountinfoResettingTv;
    @BindView(R.id.accountinfo_phonecode_et)
    EditText accountinfoPhonecodeEt;
    @BindView(R.id.accountinfo_smscode_tv)
    TextView accountinfoSmscodeTv;
    @BindView(R.id.accountinfo_intro_tv)
    TextView accountinfoIntroTv;

    private String TAG = "AccountInformation";
    private String smsType="3000";
    private MyCountDownTimer myCountDownTimer;
    private String realName, idCard, bankName, bankPlace, bankBranch, bankCardNum, smsCode,userPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_accountinformation;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        getIntroRules();
        initUserDate();
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    private void initUserDate() {

        accountinfoNicknameTv.setText(UserUtils.NickName);
        if (UserUtils.IsBankInf.equals("0")) {
            accountinfoSubmitBtn.setText("提 交");
        } else {
             accountinfoSubmitBtn.setText("修 改");
            if(UserUtils.BankBean.getUSER_REA_NAME()!=null){
                accountinfoUsernameEt.setText(UserUtils.BankBean.getUSER_REA_NAME());
                accountinfoUsernameEt.setCursorVisible(false);//隐藏光标
            }
            if(UserUtils.BankBean.getID_NUMBER()!=null){
                accountinfoIdcardEt.setText(UserUtils.BankBean.getID_NUMBER());
                accountinfoIdcardEt.setCursorVisible(false);
            }
            if(UserUtils.BankBean.getBANK_NAME()!=null){
                accountinfoBanknameEt.setText(UserUtils.BankBean.getBANK_NAME());
                accountinfoBanknameEt.setCursorVisible(false);

            }
            if(UserUtils.BankBean.getBANK_ADDRESS()!=null){
                accountinfoBankplaceEt.setText(UserUtils.BankBean.getBANK_ADDRESS());
                accountinfoBankplaceEt.setCursorVisible(false);
            }
            if(UserUtils.BankBean.getBANK_BRANCH()!=null){
                accountinfoBranchbanknameEt.setText(UserUtils.BankBean.getBANK_BRANCH());
                accountinfoBranchbanknameEt.setCursorVisible(false);
            }
            if(UserUtils.BankBean.getBANK_CARD_NO()!=null){
                accountinfoBanknumEt.setText(UserUtils.BankBean.getBANK_CARD_NO());
                accountinfoBanknumEt.setCursorVisible(false);
            }

        }
    }



    private void getAccInfoDate() {

        userPhone= accountinfoPhoneTv.getText().toString();
        realName = accountinfoUsernameEt.getText().toString();
        idCard = accountinfoIdcardEt.getText().toString();
        bankName = accountinfoBanknameEt.getText().toString();
        bankPlace = accountinfoBankplaceEt.getText().toString();
        bankBranch = accountinfoBranchbanknameEt.getText().toString();
        bankCardNum = accountinfoBanknumEt.getText().toString();
        smsCode = accountinfoPhonecodeEt.getText().toString();

        if (userPhone.isEmpty()||realName.isEmpty() || idCard.isEmpty() || bankName.isEmpty() ||
                bankPlace.isEmpty() || bankBranch.isEmpty() || bankCardNum.isEmpty() || smsCode.isEmpty()) {
               MyToast.getToast(getApplicationContext(), "请将信息填写完整！").show();
              return;
        }
         if(!Utils.checkIDcard(idCard)||Utils.isSpecialChar(idCard)){
             MyToast.getToast(getApplicationContext(), "您输入的身份证有误,不能包含字符！").show();
             return;
         }
         if(bankCardNum.length()<15||Utils.isSpecialChar(bankCardNum)){
          MyToast.getToast(getApplicationContext(), "您输入的银行卡号有误,不能包含字符！").show();
             return;
         }
        getRegBankInf(UserUtils.USER_ID, smsType, UserUtils.UserPhone, smsCode, bankPlace, bankName, bankBranch, bankCardNum, idCard, realName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        accountinfoPhoneTv.setText(UserUtils.UserPhone);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.image_back, R.id.accountinfo_submit_btn, R.id.accountinfo_phone_tv,
            R.id.accountinfo_smscode_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.accountinfo_resetting_tv:
                startActivity(new Intent(this, SettingMobileActivity.class));
                break;
            case R.id.accountinfo_phone_tv:
                startActivity(new Intent(this, SettingMobileActivity.class));
                break;
            case R.id.accountinfo_submit_btn:
                getAccInfoDate();
                break;
            case R.id.accountinfo_smscode_tv:
                userPhone= accountinfoPhoneTv.getText().toString();
                if(Utils.isEmpty(UserUtils.UserPhone)||userPhone.isEmpty()){
                    MyToast.getToast(getApplicationContext(),"请先绑定手机号！").show();
                }else {
                    myCountDownTimer.start();
                    getPhoneCode(UserUtils.USER_ID, userPhone, "3000");
                }
                break;
            default:
                break;
        }
    }

    //倒计时
    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            accountinfoSmscodeTv.setText("倒计时(" + millisUntilFinished / 1000 + ")");
            accountinfoSmscodeTv.setEnabled(false);
        }

        @Override
        public void onFinish() {
            accountinfoSmscodeTv.setText("重新获取");
            accountinfoSmscodeTv.setEnabled(true);
        }
    }

    private void getRegBankInf(String userId, String smsType, String phoneNumber,
                               String phoneCode, String bankAddress, String bankName,
                               String bankBranch, String bankCardNo, String idNumber, String userName) {
        HttpManager.getInstance().getRegBankInf(userId, smsType, phoneNumber, phoneCode, bankAddress,
                bankName, bankBranch, bankCardNo, idNumber, userName,
                new RequestSubscriber<Result<HttpDataInfo>>() {
                    @Override
                    public void _onSuccess(Result<HttpDataInfo> result) {
                        if (result.getMsg().equals(Utils.HTTP_OK)) {
                            UserUtils.BankBean = result.getData().getBankCard();
                            if (UserUtils.IsBankInf.equals("0")) {
                                MyToast.getToast(getApplicationContext(), "提交成功！").show();
                            } else {
                                MyToast.getToast(getApplicationContext(), "修改成功！").show();
                            }
                            finish();
                        } else {
                            MyToast.getToast(getApplicationContext(), result.getMsg()).show();
                        }
                    }

                    @Override
                    public void _onError(Throwable e) {
                        MyToast.getToast(getApplicationContext(), "网络异常！").show();
                    }
                });
    }

    private void getPhoneCode(String userId, String phoneNumber, String smsType) {
        if (Utils.isEmpty(userId) || Utils.isEmpty(phoneNumber)) {
            return;
        }
        HttpManager.getInstance().getPhoneCode(userId, phoneNumber, smsType, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    MyToast.getToast(getApplicationContext(), "已发送！").show();
                }else {
                    MyToast.getToast(getApplicationContext(), "发送失败,请重试！").show();
                }
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getApplicationContext(), "网络异常！").show();
            }
        });
    }

    private void getIntroRules(){
        accountinfoIntroTv.setText("温馨提示:\n" +
                "1、手机号请填写真实有效的号码，方便我们与您联系。\n" +
                "2、真实姓名须同银行卡户名一致，身份证号码须同银行卡户主身份一直。\n" +
                "3、请放心填写真实的个人资料，我们会对你的身份信息进行严格保密。\n" +
                "4、以上资料仅用于提款到银行卡，请真实填写否则无法完成提款。\n" +
                "5、如需修改账户信息，手机号与银行卡需重填，如遇其他问题请联系【汤姆抓娃娃】客服，客服QQ:2563582976。");
    }

}
