package com.game.smartmgzwwapp.fragment.mushroom;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.adapter.numshroom.RankCatchAdapter;
import com.game.smartmgzwwapp.base.BaseFragment;
import com.game.smartmgzwwapp.bean.ListRankBean;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.UserBean;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.GlideCircleTransform;
import com.game.smartmgzwwapp.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by mi on 2018/8/8.
 */

public class RankCatchFragment extends BaseFragment {
    private static final String TAG = "RankCatchFragment-";
    @BindView(R.id.rank_catch_recyclerbiew)
    RecyclerView mRecyclerbiew;

    @BindView(R.id.iv_rank_two_image)
    ImageView rank_two_image;
    @BindView(R.id.tv_rank_two_name)
    TextView rank_two_name;
    @BindView(R.id.tv_rank_two_catch)
    TextView rank_two_catch;

    @BindView(R.id.iv_rank_one_image)
    ImageView rank_one_image;
    @BindView(R.id.tv_rank_one_name)
    TextView rank_one_name;
    @BindView(R.id.tv_rank_one_catch)
    TextView rank_one_catch;

    @BindView(R.id.iv_rank_three_image)
    ImageView rank_three_image;
    @BindView(R.id.tv_rank_three_name)
    TextView rank_three_name;
    @BindView(R.id.tv_rank_three_catch)
    TextView rank_three_catch;


    private RankCatchAdapter listRankAdapter;
    private List<UserBean> list = new ArrayList<>();
    private List<UserBean> rankList = new ArrayList<>();
    private UserBean firstBean=new UserBean();
    private UserBean secondBean=new UserBean();
    private UserBean thirdBean=new UserBean();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank_catch;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initData();
        getRankDollList(UserUtils.USER_ID);
    }


    private void initData() {

        listRankAdapter = new RankCatchAdapter(getContext(), rankList );
        mRecyclerbiew.setAdapter(listRankAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerbiew.setLayoutManager(linearLayoutManager);
        mRecyclerbiew.addItemDecoration(new SpaceItemDecoration(0));
    }


    private void setViewDate() {
        if (firstBean.getNICKNAME().equals("")) {
            rank_one_name.setText(firstBean.getPHONE());
        } else {
            rank_one_name.setText(firstBean.getNICKNAME());
        }
        rank_one_catch.setText(firstBean.getDOLLTOTAL()+"次抓中");
        Glide.with(getContext())
                .load(UrlUtils.APPPICTERURL + firstBean.getIMAGE_URL())
                .error(R.mipmap.icon_app_numshroom)
                .dontAnimate()
                .centerCrop()
                .transform(new GlideCircleTransform(getContext()))
                .into(rank_one_image);

        if (secondBean.getNICKNAME().equals("")) {
            rank_two_name.setText(secondBean.getPHONE());
        } else {
            rank_two_name.setText(secondBean.getNICKNAME());
        }
        rank_two_catch.setText(secondBean.getDOLLTOTAL()+"次抓中");
        Glide.with(getContext())
                .load(UrlUtils.APPPICTERURL + secondBean.getIMAGE_URL())
                .error(R.mipmap.icon_app_numshroom)
                .dontAnimate()
                .centerCrop()
                .transform(new GlideCircleTransform(getContext()))
                .into(rank_two_image);

        if (thirdBean.getNICKNAME().equals("")) {
            rank_three_name.setText(thirdBean.getPHONE());
        } else {
            rank_three_name.setText(thirdBean.getNICKNAME());
        }
        rank_three_catch.setText(thirdBean.getDOLLTOTAL()+"次抓中");
        Glide.with(getContext())
                .load(UrlUtils.APPPICTERURL + thirdBean.getIMAGE_URL())
                .error(R.mipmap.icon_app_numshroom)
                .dontAnimate()
                .centerCrop()
                .transform(new GlideCircleTransform(getContext()))
                .into(rank_three_image);

    }


    private void getRankDollList(String userId){
        HttpManager.getInstance().getRankDollList(userId, new RequestSubscriber<Result<ListRankBean>>() {
            @Override
            public void _onSuccess(Result<ListRankBean> result) {
                if(result.getMsg().equals(Utils.HTTP_OK)){
                    dealData(result);
                }
            }

            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    /**
     * 接口数据处理
     * @param result
     */
    private void dealData(Result<ListRankBean> result){
        if(list.size()!=0){
            list.clear();
        }
        list = result.getData().getList();
        int length = list.size();
        if (length >= 1)
            firstBean = list.get(0);
        if (length >= 2)
            secondBean = list.get(1);
        if (length >= 3)
            thirdBean = list.get(2);
        rankList.clear();
        if (length > 20) {
            for (int i = 3; i < 20; i++) {
                rankList.add(list.get(i));
            }
        } else if (length > 3 && length <= 20) {
            for (int i = 3; i < length; i++) {
                rankList.add(list.get(i));
            }
        } else {
            rankList = list;
        }
        listRankAdapter.notify(rankList);
        setViewDate();

    }

}