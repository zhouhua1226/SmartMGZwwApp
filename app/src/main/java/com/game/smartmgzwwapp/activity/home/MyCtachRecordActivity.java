package com.game.smartmgzwwapp.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.adapter.MyCenterAdapter;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.VideoBackBean;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.MyToast;
import com.game.smartmgzwwapp.view.QuizInstrictionDialog;
import com.game.smartmgzwwapp.view.SureCancelDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yincong on 2017/12/22 13:48
 * 修改人：
 * 修改时间：
 * 类描述：用户抓取记录
 */
public class MyCtachRecordActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageButton imageBack;
    @BindView(R.id.recode_title_tv)
    TextView recodeTitleTv;
    @BindView(R.id.mycatchrecod_recyclerview)
    RecyclerView mycatchrecodRecyclerview;
    @BindView(R.id.mycatchrecod_fail_tv)
    TextView mycatchrecodFailTv;
    @BindView(R.id.mycatchrecod_dialog_image)
    ImageView mycatchrecodDialogImage;
    @BindView(R.id.mycatchrecod_top_layout)
    RelativeLayout mycatchrecodTopLayout;
    @BindView(R.id.mycatchrecod_fx_image)
    ImageView mycatchrecodFxImage;
    @BindView(R.id.mycatchrecod_fx_layout)
    LinearLayout mycatchrecodFxLayout;
    @BindView(R.id.mycatchrecod_qx_image)
    ImageView mycatchrecodQxImage;
    @BindView(R.id.mycatchrecod_qx_layout)
    LinearLayout mycatchrecodQxLayout;
    @BindView(R.id.mycatchrecod_fhsure_image)
    Button mycatchrecodFhsureImage;
    @BindView(R.id.mycatchrecod_dhsure_image)
    Button mycatchrecodDhsureImage;
    @BindView(R.id.mycatchrecod_selectnum_tv)
    TextView mycatchrecodSelectnumTv;
    @BindView(R.id.mycatchrecod_bottom_layout)
    RelativeLayout mycatchrecodBottomLayout;
    @BindView(R.id.mycatchrecod_selectgold_tv)
    TextView mycatchrecodSelectgoldTv;

    private String TAG = "MyCtachRecordActivity";
    private Context context = MyCtachRecordActivity.this;
    private boolean isSelest = false;
    private QuizInstrictionDialog quizInstrictionDialog;
    private MyCenterAdapter myCenterAdapter;
    private List<Boolean> isList = new ArrayList<>();
    private List<Boolean> islist = new ArrayList<>();
    private List<Integer> isSelect=new ArrayList<>();  //-1为已发货或已兑换  0为未选中  1为已选中
    private List<Integer> num = new ArrayList<>();
    private List<VideoBackBean> videoList = new ArrayList<>();
    private List<VideoBackBean> selectList = new ArrayList<>();
    private StringBuffer stringId = new StringBuffer("");
    private StringBuffer stringDollId = new StringBuffer("");
    private int gold=0;
    public static String TYPE;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mycatchrecord;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initData();
        onClick();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"游戏记录查询userId="+ UserUtils.USER_ID);
        getVideoBackList(UserUtils.USER_ID);
    }

    private void initData() {
        myCenterAdapter = new MyCenterAdapter(context, videoList);
        mycatchrecodRecyclerview.setAdapter(myCenterAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mycatchrecodRecyclerview.setLayoutManager(linearLayoutManager);
        mycatchrecodRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        TYPE=getIntent().getStringExtra("type");   //1 邮寄   2兑换
        if(TYPE.equals("1")){
            recodeTitleTv.setText("娃娃邮寄");
            mycatchrecodDhsureImage.setVisibility(View.GONE);
            mycatchrecodFhsureImage.setVisibility(View.VISIBLE);
        }else {
            recodeTitleTv.setText("娃娃兑换");
            mycatchrecodDhsureImage.setVisibility(View.VISIBLE);
            mycatchrecodFhsureImage.setVisibility(View.GONE);
            mycatchrecodDialogImage.setVisibility(View.GONE);
        }

    }

    private void onClick() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myCenterAdapter.setOnItemClickListener(new MyCenterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (isSelect.get(position)== -1) {
                    Intent intent = new Intent(context, RecordGameActivty.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("record", videoList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
//                    view.setBackgroundResource(R.drawable.mycatchrecord_select);
//                    num.add(position);
//                    selectList.add(videoList.get(position));
//                    mycatchrecodSelectnumTv.setText("已选中"+selectList.size()+"个娃娃");
                    if (isSelect.get(position)==1) {
                        isSelect.set(position,0);
                        view.setBackgroundResource(R.drawable.mycatchrecord_unselect);
                        isList.set(position,false);
                        Log.e(TAG,"位置"+position);
                        Log.e(TAG,"移除的位置"+num.indexOf(position));
                        gold-=Integer.parseInt(selectList.get(num.indexOf(position)).getCONVERSIONGOLD());
                        selectList.remove(num.indexOf(position));
                        num.remove(num.indexOf(position));
                        mycatchrecodSelectnumTv.setText("已选中"+selectList.size()+"个娃娃");
                        mycatchrecodSelectgoldTv.setText("一共可兑换"+gold+"金币");
                    } else {
                        isSelect.set(position,1);
                        view.setBackgroundResource(R.drawable.mycatchrecord_select);
                        isList.set(position, true);
                        num.add(position);
                        selectList.add(videoList.get(position));
                        mycatchrecodSelectnumTv.setText("已选中" + selectList.size() + "个娃娃");
                        gold+=Integer.parseInt(selectList.get(num.indexOf(position)).getCONVERSIONGOLD());
                        mycatchrecodSelectgoldTv.setText("一共可兑换"+gold+"金币");
                    }


                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                startActivity(new Intent(MyCtachRecordActivity.this, RecordGameTwoActivity.class));
            }
        });
    }

    private void getVideoBackList(String userId) {

        HttpManager.getInstance().getVideoBackList(userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                videoList = result.getData().getPlayback();

                if (videoList.size() != 0) {
                    mycatchrecodFailTv.setVisibility(View.GONE);
                    //myCenterAdapter.notify(getCatchNum(removeDuplicate(videoList),videoReList));
                    myCenterAdapter.notify(videoList);
                    for (int i = 0; i < videoList.size(); i++) {
                        if (videoList.get(i).getPOST_STATE().equals("0")) {
                            islist.add(false);
                            isList.add(false);
                            isSelect.add(0);
                        } else {
                            islist.add(true);
                            isList.add(true);
                            isSelect.add(-1);

                        }
                    }
                } else {
                    LogUtils.logi("个人中心, 暂无数据");
                    mycatchrecodRecyclerview.setVisibility(View.GONE);
                    mycatchrecodFailTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void _onError(Throwable e) {
                mycatchrecodRecyclerview.setVisibility(View.GONE);
                mycatchrecodFailTv.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mycatchrecod_dialog_image, R.id.mycatchrecod_fx_layout,
            R.id.mycatchrecod_qx_layout, R.id.mycatchrecod_fhsure_image,
            R.id.mycatchrecod_dhsure_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mycatchrecod_dialog_image:
                //邮寄说明对话框
                quizInstrictionDialog = new QuizInstrictionDialog(this, R.style.easy_dialog_style);
                quizInstrictionDialog.show();
                quizInstrictionDialog.setTitle("娃娃邮寄说明");
                quizInstrictionDialog.setContent(Utils.readAssetsTxt(this, "consignintroduce"));
                break;
            case R.id.mycatchrecod_fx_layout:
                //反选
                break;
            case R.id.mycatchrecod_qx_layout:
                //全选
                break;
            case R.id.mycatchrecod_fhsure_image:
                //发货
                final int lengths = selectList.size();
                Log.e(TAG, "发货娃娃数量=" + lengths);
                if (lengths > 0) {
                    Intent intent = new Intent(this, ConsignmentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("record", (Serializable) selectList);//序列化,要注意转化(Serializable)
                    intent.putExtras(bundle);//发送数据
                    startActivityForResult(intent, 1);
                    finish();
                } else {
                    MyToast.getToast(getApplicationContext(), "请至少选择一个娃娃！").show();
                }
                break;
            case R.id.mycatchrecod_dhsure_image:
                //兑换
                final int length = selectList.size();
                Log.e(TAG, "兑换娃娃数量=" + length);
                if (length > 0) {
                    SureCancelDialog sureCancelDialog = new SureCancelDialog(this, R.style.easy_dialog_style);
                    sureCancelDialog.setCancelable(false);
                    sureCancelDialog.show();
                    sureCancelDialog.setDialogTitle("确定要将这些娃娃兑换成游戏币继续抓取吗？");
                    sureCancelDialog.setDialogResultListener(new SureCancelDialog.DialogResultListener() {
                        @Override
                        public void getResult(int resultCode) {
                            if (1 == resultCode) {// 确定
                                for (int i = 0; i < length; i++) {
                                    if (i == 0) {
                                        stringId.append(selectList.get(i).getID());
                                        stringDollId.append(selectList.get(i).getDOLLID());
                                    } else {
                                        stringId.append("," + selectList.get(i).getID());
                                        stringDollId.append(selectList.get(i).getDOLLID());
                                    }
                                }
                                Log.e(TAG, "兑换娃娃编号=" + stringId);
                                getExChangeWWB(String.valueOf(stringId), String.valueOf(stringDollId), length + "", UserUtils.USER_ID);
                            } else {
                                MyToast.getToast(getApplicationContext(), "兑换取消!").show();
                            }
                        }
                    });
                } else {
                    MyToast.getToast(getApplicationContext(), "请至少选择一个娃娃！").show();
                }

                break;


            default:
                break;
        }
    }

    private void getExChangeWWB(String id, String dollId, String number, String userId) {
        HttpManager.getInstance().getExChangeWWB(id, dollId, number, userId, new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                Log.e(TAG, "兑换结果=" + loginInfoResult.getMsg());
                if (loginInfoResult.getMsg().equals("success")) {
                    UserUtils.UserBalance = loginInfoResult.getData().getAppUser().getBALANCE();
//                    videoList=loginInfoResult.getData().getPlayback();
//                    myCenterAdapter.notify(videoList);
                    MyToast.getToast(getApplicationContext(), "兑换成功!").show();
                    finish();
                }
            }

            @Override
            public void _onError(Throwable e) {

            }
        });
    }


    /*****
     * 接受发货返回的数据时调用
     ****/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        videoList = (List<VideoBackBean>) data.getExtras().getSerializable("record");
        // 根据返回码的不同，设置参数
        if (requestCode == 1) {
            myCenterAdapter.notify(videoList);
        }
    }


//    //记录数据重组   11/28 17:55
//    private List<VideoBackBean> getCatchNum(List<VideoBackBean> list, List<VideoBackBean> reList) {
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).getDOLLNAME().equals(reList.get(i).getDOLLNAME())) {
//                list.get(i).setCOUNT(reList.get(i).getCOUNT());
//            } else {
//                for (int j = 0; j < reList.size(); j++) {
//                    if (reList.get(j).getDOLLNAME().equals(list.get(i).getDOLLNAME())) {
//                        list.get(i).setCOUNT(reList.get(j).getCOUNT());
//                    }
//                }
//            }
//        }
//        return list;
//    }
//
//    //记录重复赛选
//    public List<VideoBackBean> removeDuplicate(List<VideoBackBean> list) {
//        for (int i = 0; i < list.size() - 1; i++) {
//            for (int j = list.size() - 1; j > i; j--) {
//                if (list.get(j).getDOLLNAME().equals(list.get(i).getDOLLNAME())) {
//                    list.remove(j);
//                }
//            }
//        }
//
//        return list;
//    }

}
