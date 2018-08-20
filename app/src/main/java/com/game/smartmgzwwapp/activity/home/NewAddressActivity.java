package com.game.smartmgzwwapp.activity.home;

import android.os.Bundle;
import android.util.Log;
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
import com.game.smartmgzwwapp.utils.SPUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.MyToast;
import com.jianguo.timedialog.AddressDialog;
import com.jianguo.timedialog.listener.OnAddressSetListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hongxiu on 2017/10/23.
 */
public class NewAddressActivity extends BaseActivity {
    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.preserve_button)
    Button preserveButton;
    @BindView(R.id.newaddress_name_et)
    EditText newaddressNameEt;
    @BindView(R.id.newaddress_phone_et)
    EditText newaddressPhoneEt;
    @BindView(R.id.newaddress_dq_tv)
    public TextView newaddressDqTv;
    @BindView(R.id.newaddress_detail_et)
    EditText newaddressDetailEt;

    private String TAG="NewAddressActivity--";

    private String name="";
    private String phone="";
    private String detailaddress="";
    private String provinceCity="";
    private String totaladdress="";
    private String information="";
    public int province_index = -1;
    public int city_index = -1;
    private AddressDialog mAddressDialog;
    @Override
    protected int getLayoutId() {
        //新增地址页面
        return R.layout.activity_newaddress;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initAddress();
        initAddressDialog();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    private void initAddress(){
        provinceCity=  SPUtils.getString(getApplicationContext(), UserUtils.SP_TAG_PROVINCECITY, "");
        if(!UserUtils.UserAddress.equals("")){
           String[] sh=UserUtils.UserAddress.split(" ");
            int lenght=sh.length;
            if (lenght>0) {
                newaddressNameEt.setText(sh[0]);
            }
            if (lenght>1) {
                newaddressPhoneEt.setText(sh[1]);
            }
            if (lenght>2) {
                if(sh[2].contains(provinceCity)){
                    newaddressDetailEt.setText(sh[2].replace(provinceCity,""));
                }
            }

        }
        if(Utils.isEmpty(provinceCity)){
            newaddressDqTv.setText("");
        }else {
            newaddressDqTv.setText(provinceCity);
        }
    }

    private void initData(){
        name=newaddressNameEt.getText().toString();
        phone=newaddressPhoneEt.getText().toString();
        detailaddress=newaddressDetailEt.getText().toString();
        provinceCity=newaddressDqTv.getText().toString();
        totaladdress=provinceCity+detailaddress;
    }

    @OnClick({R.id.image_back, R.id.preserve_button,R.id.newaddress_dq_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.preserve_button:
                initData();
                if(Utils.isEmpty(name)||Utils.isEmpty(phone)||Utils.isEmpty(provinceCity)||Utils.isEmpty(detailaddress)){
                    MyToast.getToast(this, "请将信息填写完整！").show();
                }else {
                    if (!Utils.checkPhoneREX(phone)) {
                        MyToast.getToast(this, "手机号格式有误").show();
                        return;
                    }
                    if(detailaddress.contains(provinceCity)){
                        MyToast.getToast(this, "详细地址请勿重复填写省市地区！").show();
                        return;
                    }
                        information=name+"  "+phone+"  "+totaladdress;
                        getConsignee(name,phone,totaladdress,UserUtils.USER_ID);
                }
                break;
            case R.id.newaddress_dq_tv:
                if(!mAddressDialog.isAdded()){
                    mAddressDialog.show(getSupportFragmentManager(),"all");
                }
                break;
            default:
                break;
        }
    }

    private void getConsignee(String name,String phone,String address,String userID){
        HttpManager.getInstance().getConsignee(name, phone, address, userID, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                Log.e(TAG,"收货信息结果="+loginInfoResult.getMsg());
                String name=loginInfoResult.getData().getAppUser().getCNEE_NAME();
                String phone=loginInfoResult.getData().getAppUser().getCNEE_PHONE();
                String backaddress=loginInfoResult.getData().getAppUser().getCNEE_ADDRESS();
                UserUtils.UserAddress=name+" "+phone+" "+backaddress;
                SPUtils.putString(getApplicationContext(), UserUtils.SP_TAG_PROVINCECITY, provinceCity);
                MyToast.getToast(getApplicationContext(), "保存成功！").show();
                finish();
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    private void initAddressDialog() {
        mAddressDialog = new AddressDialog.Builder()
                .setCyclic(false)
                .setTitleStringId("地址")
                .setThemeColor(getResources().getColor(R.color.apptheme_bg))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.gray_drak))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.apptheme_bg))
                .setWheelItemTextSize(13)
                .setAddressBack(new OnAddressSetListener() {
                    @Override
                    public void onAddressDateSet(AddressDialog mAddressDialog, String provice, String city, String county) {
                        if(provice.equals(city)){
                            newaddressDqTv.setText(city+county);
                        }else{
                            newaddressDqTv.setText(provice+city+county);
                        }
                    }
                })
                .build();
    }
}
