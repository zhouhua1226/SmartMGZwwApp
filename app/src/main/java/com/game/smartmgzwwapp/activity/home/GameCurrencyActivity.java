package com.game.smartmgzwwapp.activity.home;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.adapter.GameCurrencyAdapter;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.UserPaymentBean;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hongxiu on 2017/9/26.
 */
public class GameCurrencyActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.game_tv)
    TextView gameTv;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.gc_none_tv)
    TextView gcNoneTv;

    private String TAG="GameCurrencyActivity--";
    private List<UserPaymentBean> list=new ArrayList<>();
    private GameCurrencyAdapter gameCurrencyAdapter;

    //我的游戏币
    @Override
    protected int getLayoutId() {
        return R.layout.activity_gamecurrency;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        gameTv.setText(UserUtils.UserBalance);
        initData();
        getPaymenList(UserUtils.USER_ID);
        //getExChangeList(UserUtils.USER_ID);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.image_back)
    public void onViewClicked() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void initData(){
        gameCurrencyAdapter = new GameCurrencyAdapter(this, list);
        recyclerview.setAdapter(gameCurrencyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void getPaymenList(String userId){
        HttpManager.getInstance().getPaymenList(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                Log.e(TAG,"金币流水列表="+loginInfoResult.getMsg());
                list=loginInfoResult.getData().getPaymentList();
                if(list.size()>0){
                    gameCurrencyAdapter.notify(list);
                }else {
                    recyclerview.setVisibility(View.GONE);
                    gcNoneTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

//    private void getExChangeList(String userId){
//        HttpManager.getInstance().getExChangeList(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
//            @Override
//            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
//                Log.e(TAG,"兑换列表="+loginInfoResult.getMsg());
//                list=loginInfoResult.getData().getConversionList();
//                if(list.size()>0){
//                    gameCurrencyAdapter.notify(list);
//                }else {
//                    recyclerview.setVisibility(View.GONE);
//                    gcNoneTv.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void _onError(Throwable e) {
//
//            }
//        });
//    }




}
