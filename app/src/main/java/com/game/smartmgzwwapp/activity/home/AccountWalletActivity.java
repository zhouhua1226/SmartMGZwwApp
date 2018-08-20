package com.game.smartmgzwwapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.UserBean;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.CardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenw on 2018/7/18.
 */

public class AccountWalletActivity extends BaseActivity implements CardView.OnCardViewOnClickListener{
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
     @BindView(R.id.viewGroup)
     LinearLayout mIndicator;
    @BindView(R.id.tv_card_name)
    TextView card_name;
    @BindView(R.id.tv_card_days)
    TextView card_days;
    @BindView(R.id.btn_card_option)
    Button card_option;
    @BindView(R.id.tv_youxibi)
    TextView tv_youxibi;
    @BindView(R.id.tv_blance)
    TextView tv_blance;

    private List<View> viewList=new ArrayList<>() ;
    private int type=0;
    private int weekDay=0;
    private int mouthDay=0;

    /**
     * 图片轮播指示个图
     */
    private ImageView mImageView = null;
    /**
     * 滚动图片指示视图列表
     */
    private ImageView[] mImageViews = null;
    private String h1;
    private PagerAdapter pagerAdapter;
    private CardView mCardView1;
    private CardView mCardView2;
    private CardView mCardView3;
    @Override
    protected int getLayoutId() {
        return R.layout.layout_account_wallet;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setStatusBarColor(R.color.backcolor);
        initView();
            if (Utils.isNumeric( UserUtils.UserWeekDay)) {
                weekDay = Integer.parseInt(UserUtils.UserWeekDay);
            }
            if (Utils.isNumeric(UserUtils.UserMouthDay)) {
                mouthDay = Integer.parseInt(UserUtils.UserMouthDay);
            }
        initViewPager();
      //  getUserAccBalCount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAppUserInf();
    }

    @OnClick({R.id.image_back,R.id.tv_recharge_detail,
              R.id.btn_card_option,R.id.rl_youxibi,
              R.id.rl_blance })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                this.finish();
                break;
            case R.id.tv_recharge_detail:
                Utils.toActivity(this,GameCurrencyActivity.class);
                break;
            case R.id.btn_card_option:
                Intent intent=new Intent(this,CardDetailActivity.class);
                intent.putExtra(UrlUtils.CARD_TYPE,type);
                Utils.toActivity(this,intent);
                break;
            case R.id.rl_youxibi:
                Utils.toActivity(this,RechargeActivity.class);
                break;
            case R.id.rl_blance:
                Utils.toActivity(this,AccountDetailActivity.class);
                break;
        }
    }
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }


    public void  initViewPager() {

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mImageViews[position].setBackgroundResource(R.drawable.banner_show_check_bg);
                for (int i = 0; i < mImageViews.length; i++) {
                    if (position != i) {
                        mImageViews[i].setBackgroundResource(R.drawable.banner_show_defauit_bg);
                    }
                }
                notifyView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(pagerAdapter= new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }
            @Override
            public int getCount() {
                return viewList.size();
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return "title";
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        });
    }

    /**
     * 装填图片数据
     */
    public void setImageResources(List<View> mListView) {
        // 清除
        mIndicator.removeAllViews();
        // 图片广告数量
         int imageCount = mListView.size();
        if(imageCount>1) {
            mImageViews = new ImageView[imageCount];
            for (int i = 0; i < imageCount; i++) {
                mImageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 15;
                // FIT_XY不按比例缩放图片，目标是把图片塞满整个View
                mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                mImageView.setLayoutParams(params);
                mImageViews[i] = mImageView;
                if (i == 0) {
                    mImageViews[i].setBackgroundResource(R.drawable.banner_show_check_bg);
                } else {
                    mImageViews[i].setBackgroundResource(R.drawable.banner_show_defauit_bg);
                }
                mIndicator.addView(mImageViews[i]);
                mIndicator.setVisibility(View.VISIBLE);
            }
        }
    }


    private void notifyView(int position) {
        CardView mCardView= (CardView)viewList.get(position);
        type=mCardView.type;
        switch (type){
            case 0:
                card_name.setText("汤姆抓娃娃周卡");
                h1="周卡剩余"+"<font color='#f26d35'>" + 0 + "</font>"+"天";
                break;
            case 1:
                card_name.setText("汤姆抓娃娃周卡");
                h1="周卡剩余"+"<font color='#f26d35'>" + weekDay + "</font>"+"天";
                break;
            case 2:
                card_name.setText("汤姆抓娃娃月卡");
                h1="月卡剩余"+"<font color='#f26d35'>" + mouthDay + "</font>"+"天";
                break;
        }
         card_days.setText((Html.fromHtml(h1)));
    }

    /**
     * 刷新游戏币
     */
    private void getAppUserInf() {
        HttpManager.getInstance().getAppUserInf(UserUtils.USER_ID, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    if (result.getData().getAppUser() == null) {
                        return;
                    }
                    UserUtils.UserBalance = result.getData().getAppUser().getBALANCE();
                    tv_youxibi.setText(UserUtils.UserBalance);
                    initCardView(result.getData().getAppUser());
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }

    private void initCardView(UserBean mUserBean) {
              viewList.clear();
              if(Utils.isNumeric(mUserBean.getWEEKS_CARD())){
                     weekDay=Integer.parseInt(mUserBean.getWEEKS_CARD());
               }
            if(Utils.isNumeric(mUserBean.getMONTH_CARD())){
                  mouthDay=Integer.parseInt(mUserBean.getMONTH_CARD());
             }
             mCardView1=new CardView(this, 1, R.drawable.icon_week_card, weekDay, true,this);
             mCardView2=new CardView(this, 2, R.drawable.icon_mouth_card, mouthDay, true, this);
             mCardView3= new CardView(this,0,R.drawable.iocn_notbuy_card,0,false,this);
               if(weekDay>0){
                 viewList.add(mCardView1);
               }
               if(mouthDay>0) {
                  viewList.add(mCardView2);
               }

               if(viewList.size()<1){
                   viewList.add(mCardView3);
                   card_option.setText("购买");
                   card_option.setBackgroundResource(R.drawable.bg_radio_wallet_checked);
                }else{
                   card_option.setText("查看");
                   card_option.setBackgroundResource(R.drawable.bg_radio_wallet_normal);
                }
                 mViewPager.setAdapter(pagerAdapter);
                 pagerAdapter.notifyDataSetChanged();
                 setImageResources(viewList);
                 notifyView(0);
        }

    private void getUserAccBalCount() {
        HttpManager.getInstance().getUserAccBalCount(UserUtils.USER_ID, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if (httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)) {
                    String amount = httpDataInfoResult.getData().getAccBal();
                    UserUtils.UserAmount=amount;
                    tv_blance.setText(amount+"元");
                }
            }

            @Override
            public void _onError(Throwable e) {
                LogUtils.loge(e.getMessage());
            }
        });
    }


    @Override
    public void OnCardViewOnClick(int type) {
            Intent intent=new Intent(this,CardDetailActivity.class);
            intent.putExtra(UrlUtils.CARD_TYPE,type);
            Utils.toActivity(this,intent);
    }
}
