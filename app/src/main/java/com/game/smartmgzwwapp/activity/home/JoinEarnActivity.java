package com.game.smartmgzwwapp.activity.home;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.adapter.JoinEarnAdapter;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.PromomoteBean;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.CatchDollResultDialog;
import com.game.smartmgzwwapp.view.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yincong on 2018/3/7 16:15
 * 修改人：
 * 修改时间：
 * 类描述：加盟推广类
 */
public class JoinEarnActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.joinearn_ll)
    LinearLayout joinearnLl;
    @BindView(R.id.joinearn_recyclerview)
    RecyclerView joinearnRecyclerview;

    private String TAG = "JoinEarnActivity--";
    private JoinEarnAdapter joinEarnAdapter;
    private List<PromomoteBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_joinearn;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initDate();
        getPromomoteManage();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    private void initDate() {
        joinEarnAdapter = new JoinEarnAdapter(this, list);
        joinearnRecyclerview.setAdapter(joinEarnAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        joinearnRecyclerview.setLayoutManager(linearLayoutManager);
        joinearnRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        joinEarnAdapter.setmOnItemClickListener(new JoinEarnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PromomoteBean promomoteBean=list.get(position);
                String proManageId=promomoteBean.getPRO_MANAGE_ID()+"";
                if(Utils.isNumeric(UserUtils.UserBalance)) {
                    int userBalance = Integer.parseInt(UserUtils.UserBalance);
                    int payGold = Integer.parseInt(promomoteBean.getPAY_GOLD());
                    if (userBalance >= payGold) {
                        getPromomoteOrder(UserUtils.USER_ID, proManageId, "P");
                    } else {
                        setCatchResultDialog(0);
                    }
                }
            }
        });
    }

    private void setCatchResultDialog(final int index ) {
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
                            Utils.toActivity(JoinEarnActivity.this, RechargeActivity.class);
                            break;

                    }
                }
                catchDollResultDialog.dismiss();
            }
        });
    }

    private void showJoinDialog(final String code) {
        View view = getLayoutInflater().inflate(R.layout.dialog_joinearn, null);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        final PopupWindow mPop = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPop.setOutsideTouchable(true);
        mPop.setFocusable(true);
        mPop.showAtLocation(joinearnLl, Gravity.CENTER, 0, 0);//在屏幕居中，无偏移
        BackgroudAlpha((float) 0.5);
        mPop.setOnDismissListener(new popupwindowdismisslistener());
        TextView cancleTv = (TextView) view.findViewById(R.id.dialog_joinearn_cancle_tv);
        TextView copyTv = (TextView) view.findViewById(R.id.dialog_joinearn_copy_tv);
        TextView codeTv = (TextView) view.findViewById(R.id.dialog_joinearn_code_tv);
        codeTv.setText(code);
        copyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!code.equals("")) {
                    // 将文本内容放到系统剪贴板里。
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(code);
                    mPop.dismiss();
                    MyToast.getToast(JoinEarnActivity.this, "复制成功！").show();
                }
            }
        });
        cancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        // 重写onKeyListener
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mPop.dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    //设置屏幕背景透明度
    private void BackgroudAlpha(float alpha) {
        // TODO Auto-generated method stub
        WindowManager.LayoutParams l = this.getWindow().getAttributes();
        l.alpha = alpha;
        getWindow().setAttributes(l);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    //点击其他部分popwindow消失时，屏幕恢复透明度
    class popupwindowdismisslistener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            BackgroudAlpha((float) 1);
        }

    }

    private void getPromomoteManage() {
        HttpManager.getInstance().getPromomoteManage(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if(httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)){
                    list=httpDataInfoResult.getData().getVarList();
                    if(list.size()>0){
                        joinEarnAdapter.notify(list);
                    }
                }
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getApplicationContext(),"网络异常！").show();
            }
        });
    }

    private void getPromomoteOrder(String userId,String proManageId,String payType){
        HttpManager.getInstance().getPromomoteOrder(userId, proManageId, payType, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> httpDataInfoResult) {
                if(httpDataInfoResult.getMsg().equals(Utils.HTTP_OK)){
                    PromomoteBean promomoteBean=httpDataInfoResult.getData().getPromoteInf();
                    if(promomoteBean!=null){
                        String joinCode=promomoteBean.getPRO_ID()+"";
                        if(!Utils.isEmpty(joinCode)) {
                            showJoinDialog(joinCode);
                        }

                    }

                }else {
                    MyToast.getToast(getApplicationContext(),httpDataInfoResult.getMsg()).show();
                }
            }

            @Override
            public void _onError(Throwable e) {
                MyToast.getToast(getApplicationContext(),"网络异常！").show();
            }
        });
    }

}
