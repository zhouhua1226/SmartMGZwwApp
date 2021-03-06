package com.game.smartmgzwwapp.activity.home;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.adapter.AccdetailAdapter;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.PagesBean;
import com.game.smartmgzwwapp.bean.PromoteEarnBean;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yincong on 2018/3/19 17:45
 * 修改人：
 * 修改时间：
 * 类描述：用户账户收支流水
 */
public class AccountDetailActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.accdetail_recyclerview)
    RecyclerView accdetailRecyclerview;
    @BindView(R.id.accdetail_fail_tv)
    TextView accdetailFailTv;

    private String TAG = "AccountDetail--";
    private String nextpage = "1";
    private PagesBean pagesBean;
    private AccdetailAdapter accdetailAdapter;
    private List<PromoteEarnBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_accountdetail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        setOnClick();
        getUserAccountDetailPage(UserUtils.USER_ID, nextpage);
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        accdetailAdapter = new AccdetailAdapter(this, list);
        accdetailRecyclerview.setAdapter(accdetailAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        accdetailRecyclerview.setLayoutManager(linearLayoutManager);
        accdetailRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setOnClick() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getUserAccountDetailPage(String userId, String nextPage) {
        HttpManager.getInstance().getUserAccountDetailPage(userId, nextPage, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals(Utils.HTTP_OK)) {
                    pagesBean = result.getData().getPd();
                    list = result.getData().getLogList();
                    if (list.size() > 0) {
                        accdetailRecyclerview.setVisibility(View.VISIBLE);
                        accdetailFailTv.setVisibility(View.GONE);
                        accdetailAdapter.notify(list);
                    }else {
                        accdetailRecyclerview.setVisibility(View.GONE);
                        accdetailFailTv.setVisibility(View.VISIBLE);
                    }
                }else {
                    accdetailRecyclerview.setVisibility(View.GONE);
                    accdetailFailTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void _onError(Throwable e) {
                accdetailRecyclerview.setVisibility(View.GONE);
                accdetailFailTv.setVisibility(View.VISIBLE);
                MyToast.getToast(getApplicationContext(), "网络异常！").show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
