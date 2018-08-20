package com.game.smartmgzwwapp.fragment.mushroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.activity.home.AccountWalletActivity;
import com.game.smartmgzwwapp.activity.home.BetRecordActivity;
import com.game.smartmgzwwapp.activity.home.GameCurrencyActivity;
import com.game.smartmgzwwapp.activity.home.InformationActivity;
import com.game.smartmgzwwapp.activity.home.LnvitationCodeActivity;
import com.game.smartmgzwwapp.activity.home.MyCtachRecordActivity;
import com.game.smartmgzwwapp.activity.home.MyLogisticsOrderActivity;
import com.game.smartmgzwwapp.activity.home.ServiceActivity;
import com.game.smartmgzwwapp.activity.home.SettingActivity;
import com.game.smartmgzwwapp.activity.mushroom.HomeActivity;
import com.game.smartmgzwwapp.activity.mushroom.Splash1Activity;
import com.game.smartmgzwwapp.base.BaseFragment;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.VideoBackBean;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.GlideCircleTransform;
import com.game.smartmgzwwapp.view.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by mi on 2018/8/8.
 */

public class CenterFragment extends BaseFragment {

    @BindView(R.id.user_center_image)
    ImageView userImage;
    @BindView(R.id.user_center_name)
    TextView userName;
    @BindView(R.id.tv_center_golds)
    TextView userGolds;

    private String TAG = "MyCenterActivity";
    private List<VideoBackBean> videoList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_munshroom_center;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Glide.get(getContext()).clearMemory();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "个人中心userId=" + UserUtils.USER_ID);
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            getAppUserInf(UserUtils.USER_ID);
        }else{
            Utils.toActivity(getActivity(), Splash1Activity.class);
            HomeActivity.mMainActivity.finish();
      }
    }


    private void getUserImageAndName() {
        if (!Utils.isEmpty(UserUtils.USER_ID)) {
            if (!UserUtils.NickName.equals("")) {
                userName.setText(UserUtils.NickName);
            } else {
                userName.setText("暂无昵称");
            }
            userGolds.setText("游戏币:" + UserUtils.UserBalance );
            Glide.with(getContext())
                    .load(UserUtils.UserImage)
                    .error(R.mipmap.icon_app_numshroom)
                    .dontAnimate()
                    .transform(new GlideCircleTransform(getContext()))
                    .into(userImage);

        } else {
            userName.setText("请登录");
            videoList.clear();
            userImage.setImageResource(R.drawable.round);
        }
    }


    @OnClick({R.id.rl_center_order, R.id.rl_center_gold_record,
            R.id.rl_center_bet_record, R.id.rl_center_wawa_mail,
            R.id.rl_center_wawa_exchange, R.id.rl_center_invite_code,
            R.id.rl_center_customer_service, R.id.rl_center_set,
            R.id.user_center_image, R.id.tv_center_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_center_customer_service:
                startActivity(new Intent(getContext(), ServiceActivity.class));
                break;
            case R.id.rl_center_set:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.user_center_image:
                startActivity(new Intent(getContext(), InformationActivity.class));
                break;
            case R.id.tv_center_recharge:
                startActivity(new Intent(getContext(), AccountWalletActivity.class));
                break;
            case R.id.rl_center_wawa_mail:
                Intent intent=new Intent(getContext(),MyCtachRecordActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
                break;
            case R.id.rl_center_invite_code:// 邀请码
                startActivity(new Intent(getContext(), LnvitationCodeActivity.class));
                break;

            case R.id.rl_center_gold_record:  //金币记录
                startActivity(new Intent(getContext(), GameCurrencyActivity.class));
                break;
            case R.id.rl_center_bet_record://竞猜记录
                startActivity(new Intent(getContext(), BetRecordActivity.class));
                break;
            case R.id.rl_center_order://物流订单查询类
                startActivity(new Intent(getContext(), MyLogisticsOrderActivity.class));
                break;

            case R.id.rl_center_wawa_exchange:
                Intent intent2=new Intent(getContext(),MyCtachRecordActivity.class);
                intent2.putExtra("type","2");
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    private void getAppUserInf(String userId) {
        if (Utils.isEmpty(userId)) {
            return;
        }
        HttpManager.getInstance().getAppUserInf(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if(result.getData().getAppUser()==null){
                    return;
                }
                UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                UserUtils.UserCatchNum = result.getData().getAppUser().getDOLLTOTAL();
                UserUtils.NickName = result.getData().getAppUser().getNICKNAME();
                UserUtils.UserWeekDay=result.getData().getAppUser().getWEEKS_CARD();
                UserUtils.UserMouthDay=result.getData().getAppUser().getMONTH_CARD();

                UserUtils.UserImage = UrlUtils.APPPICTERURL + result.getData().getAppUser().getIMAGE_URL();
                String name = result.getData().getAppUser().getCNEE_NAME();
                String phone = result.getData().getAppUser().getCNEE_PHONE();
                String address = result.getData().getAppUser().getCNEE_ADDRESS();
                if(name!=null && phone!=null && address!=null) {
                    UserUtils.UserAddress = name + " " + phone + " " + address;
                }
                UserUtils.IsBankInf = result.getData().getIsBankInf();
                UserUtils.BankBean = result.getData().getBankCard();
                if(!UserUtils.IsBankInf.equals("0")){
                    UserUtils.UserPhone=result.getData().getBankCard().getBANK_PHONE();
                }else{
                    if(result.getData().getAppUser().getBDPHONE().equals("")&&result.getData().getAppUser().getBDPHONE()!=null){
                        UserUtils.UserPhone=result.getData().getAppUser().getPHONE();
                    }else{
                        UserUtils.UserPhone=result.getData().getAppUser().getBDPHONE();
                    }
                }
                LogUtils.loge( "个人信息刷新结果=" + result.getMsg() + "余额=" + result.getData().getAppUser().getBALANCE()
                        + " 抓取次数=" + result.getData().getAppUser().getDOLLTOTAL()
                        + " 昵称=" + result.getData().getAppUser().getNICKNAME()
                        + " 头像=" + UserUtils.UserImage
                        + " 发货地址=" + UserUtils.UserAddress);
                getUserImageAndName();
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getContext(), "网络异常！").show();
            }
        });
    }


}