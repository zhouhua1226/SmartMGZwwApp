package com.game.smartmgzwwapp.fragment.mushroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.activity.ctrl.view.CtrlActivity;
import com.game.smartmgzwwapp.activity.ctrl.view.PushCoinActivity;
import com.game.smartmgzwwapp.activity.home.NewsWebActivity;
import com.game.smartmgzwwapp.adapter.numshroom.ZWWHomeAdapter;
import com.game.smartmgzwwapp.base.BaseFragment;
import com.game.smartmgzwwapp.bean.BannerBean;
import com.game.smartmgzwwapp.bean.HttpDataInfo;
import com.game.smartmgzwwapp.bean.Marquee;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.bean.RoomBean;
import com.game.smartmgzwwapp.bean.RoomListBean;
import com.game.smartmgzwwapp.bean.ToyTypeBean;
import com.game.smartmgzwwapp.bean.VideoBackBean;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.EmptyLayout;
import com.game.smartmgzwwapp.view.GlideImageLoader;
import com.game.smartmgzwwapp.view.MyToast;
import com.game.smartmgzwwapp.view.PullToRefreshView;
import com.game.smartmgzwwapp.view.SpaceItemDecoration;
import com.gatz.netty.utils.NettyUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by mi on 2018/8/8.
 */

public class HomeFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener,
        PullToRefreshView.OnFooterRefreshListener  {
    private static final String TAG = "HomeFragment";
    @BindView(R.id.home_recyclerview)
    RecyclerView zwwRecyclerview;
    @BindView(R.id.home_emptylayout)
    EmptyLayout zwwEmptylayout;

    @BindView(R.id.type_tly)
    TabLayout typeTabLayout;
    @BindView(R.id.mPullToRefreshView)
    PullToRefreshView mPullToRefreshView;

    @BindView(R.id.home_banner)
    Banner zwwBanner;
    private List<RoomBean> currentRoomBeens = new ArrayList<>();
    private ZWWHomeAdapter zwwAdapter;
    private String sessionId;
    private EmptyLayout.OnClickReTryListener onClickReTryListener;
    private List<VideoBackBean> playBackBeanList = new ArrayList<>();
    private List<Marquee> marquees = new ArrayList<>();
    private List<BannerBean> bannerList = new ArrayList<>();//轮播图接口返回BannerBean
    private List<BannerBean> nBannerList = new ArrayList<>();//轮播图BannerBean
    private List<String> list = new ArrayList<>();//轮播图url
    //分类参数
    private int currentSumPage = 1;
    private int currentPage = 1;
    private List<ToyTypeBean> toyTypeBeanList;
    private String currentType = "";  //首页


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_munshroom_home;
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initData();
        onClick();
        getBannerList();
        getToyType();
    }


    private void initData() {
        dismissEmptyLayout();
        zwwAdapter = new ZWWHomeAdapter(getActivity(), currentRoomBeens);
        zwwAdapter.setmOnItemClickListener(onItemClickListener);
        zwwRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        if (Utils.getWidthSize(getContext()) < 720) {
            zwwRecyclerview.addItemDecoration(new SpaceItemDecoration(16));
        } else {
            zwwRecyclerview.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.PX_10)));
        }
        zwwRecyclerview.setHasFixedSize(true);
        zwwRecyclerview.setNestedScrollingEnabled(false);
        zwwRecyclerview.setAdapter(zwwAdapter);
        if (onClickReTryListener != null) {
            zwwEmptylayout.setOnClickReTryListener(onClickReTryListener);
        }
        typeTabLayout.addOnTabSelectedListener(tabSelectedListener);


    }
    private void onClick() {
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
    }
    public void notifyAdapter(List<RoomBean> rooms, int page) {
        currentRoomBeens = rooms;
        currentSumPage = page;
        zwwAdapter.notify(currentRoomBeens);
    }

    public void showError() {
        if(zwwEmptylayout!=null) {
            zwwEmptylayout.showEmpty();
        }
    }

    public void dismissEmptyLayout() {
        if(zwwEmptylayout!=null) {
            zwwEmptylayout.dismiss();
        }
    }

    public void setSessionId(String id, boolean isReconnect) {
        this.sessionId = id;
        UserUtils.setNettyInfo(sessionId, UserUtils.USER_ID, "", isReconnect);
        UserUtils.doNettyConnect(NettyUtils.LOGIN_TYPE_TENCENT);
    }

    public  ZWWHomeAdapter.OnItemClickListener onItemClickListener =
            new ZWWHomeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    jumpRoom(position);
                }
            };

    private void enterNext(String name, String camera1, String camera2, boolean status, String gold, String id, String prob, String reward, String dollUrl) {
        Intent intent = new Intent(getActivity(), CtrlActivity.class);
        intent.putExtra(Utils.TAG_ROOM_NAME, name);
        intent.putExtra(Utils.TAG_URL_MASTER, camera1);
        intent.putExtra(Utils.TAG_URL_SECOND, camera2);
        intent.putExtra(Utils.TAG_ROOM_STATUS, status);
        intent.putExtra(Utils.TAG_DOLL_GOLD, gold);
        intent.putExtra(Utils.TAG_DOLL_Id, id);
        intent.putExtra(Utils.TAG_ROOM_PROB, prob);
        intent.putExtra(Utils.TAG_ROOM_REWARD, reward);
        intent.putExtra(Utils.TAG_ROOM_DOLLURL, dollUrl);
        startActivity(intent);

    }

    /**
     * 房间跳转方法
     *
     * @param po
     */
    private void jumpRoom(int po) {
        if ((currentRoomBeens.size() > 0) && (!Utils.isEmpty(sessionId))) {
            String room_id = currentRoomBeens.get(po).getDollId();
            boolean room_status = false;
            UserUtils.setNettyInfo(sessionId, UserUtils.USER_ID, room_id, false);
            if (currentRoomBeens.get(po).getDollState().equals("0")) {
                room_status = true;
            } else if (currentRoomBeens.get(po).getDollState().equals("1")) {
                room_status = false;
            }
            String rtmpUrl1 = currentRoomBeens.get(po).getCameras().get(0).getRtmpUrl();
            String rtmpUrl2 = currentRoomBeens.get(po).getCameras().get(1).getRtmpUrl();
            String serviceName1 = currentRoomBeens.get(po).getCameras().get(0).getServerName();
            String serviceName2 = currentRoomBeens.get(po).getCameras().get(1).getServerName();
            String liveStream1 = currentRoomBeens.get(po).getCameras().get(0).getLivestream();
            String liveStream2 = currentRoomBeens.get(po).getCameras().get(1).getLivestream();
            String idToken = "?token=" + UserUtils.SRSToken.getToken()
                    + "&expire=" + UserUtils.SRSToken.getExpire()
                    + "&tid=" + UserUtils.SRSToken.getTid()
                    + "&time=" + UserUtils.SRSToken.getTime()
                    + "&type=" + UserUtils.SRSToken.getType()
                    + "/";
            String url1 = rtmpUrl1 + serviceName1 + idToken + liveStream1;
            String url2 = rtmpUrl2 + serviceName2 + idToken + liveStream2;
            LogUtils.loge("房间推流地址1=" + url1);
            LogUtils.loge("房间推流地址2=" + url2);
            if (!TextUtils.isEmpty(url2) && !TextUtils.isEmpty(url1)) {
                String type = currentRoomBeens.get(po).getDeviceType();
                if (!TextUtils.isEmpty(type) && type.equals("2")) {
                    //TODO 推币机处理
                    enterCoinNext(url1, url2);
                    return;
                }
                enterNext(currentRoomBeens.get(po).getDollName(),
                        url1, url2,
                        room_status,
                        String.valueOf(currentRoomBeens.get(po).getDollGold()),
                        currentRoomBeens.get(po).getDollId(),
                        currentRoomBeens.get(po).getProb(),
                        currentRoomBeens.get(po).getReward(),
                        currentRoomBeens.get(po).getDollUrl());
            } else {
                LogUtils.loge("当前设备没有配置摄像头!");
            }
        }
    }


    //TODO 正式环境统一处理
    private void enterCoinNext(String camera1, String camera2) {
        Intent intent = new Intent(getActivity(), PushCoinActivity.class);
        intent.putExtra(Utils.TAG_URL_MASTER, camera1);
        intent.putExtra(Utils.TAG_URL_SECOND, camera2);
        startActivity(intent);
    }

    //banner轮播
    private void initBanner(List<String> lists, final List<BannerBean> nBannerList) {

        //设置Banner样式
        zwwBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        zwwBanner.setImageLoader(new GlideImageLoader());
        zwwBanner.setImages(lists);
        //设置Banner动画效果
        zwwBanner.setBannerAnimation(Transformer.DepthPage);
        //设置轮播时间
        zwwBanner.setDelayTime(3000);
        //设置指示器位置(当banner模式中有指示器时)
        zwwBanner.setIndicatorGravity(BannerConfig.CENTER);

        //Banner设置方法全部调用完毕时最后调用
        zwwBanner.start();
        zwwBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerBean  mBannerBean=  nBannerList.get(position);
                if (!mBannerBean.getHREF_ST().equals("")) {
                    Intent intent = new Intent(getContext(), NewsWebActivity.class);
                    intent.putExtra("newsurl",  mBannerBean.getHREF_ST());
                    intent.putExtra("newstitle", mBannerBean.getRUN_NAME());
                    startActivity(intent);
                }
            }
        });
    }

    private void getBannerList() {

        HttpManager.getInstance().getBannerList(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> loginInfoResult) {
                if (loginInfoResult.getMsg().equals("success")) {
                    bannerList = loginInfoResult.getData().getRunImage();
                    if (bannerList.size() > 0) {
                        for (int i = 0; i < bannerList.size(); i++) {
                            int state = bannerList.get(i).getSTATE();
                            switch (state){
                                case 0:
                                    if(bannerList.get(i).getIMAGE_URL()!=null&&!bannerList.get(i).getIMAGE_URL().equals("")) {
                                        list.add(UrlUtils.APPPICTERURL+bannerList.get(i).getIMAGE_URL());
                                        nBannerList.add(bannerList.get(i));
                                    }
                                    break;
                                case 1:
                                    break;
                            }
                        }
                        initBanner(list,nBannerList);
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.loge(e.getMessage());
            }
        });
    }

    private List<RoomBean> dealWithRoomStats(List<RoomBean> beens) {
        if (beens.size() == 0) {
            return beens;
        }
        for (int i = 0; i < beens.size(); i++) {
            RoomBean bean = beens.get(i);
            bean = UserUtils.dealWithRoomStatus(bean, bean.getDollState());
            beens.set(i, bean);
        }
        return beens;
    }
    private void getToyType() {
        HttpManager.getInstance().getToyType(new RequestSubscriber<Result<HttpDataInfo>>() {
            @Override
            public void _onSuccess(Result<HttpDataInfo> result) {
                if (result.getMsg().equals("success")) {
                    if (result.getData() != null) {
                        toyTypeBeanList = result.getData().getToyTypeList();
                        typeTabLayout.addTab(typeTabLayout.newTab().
                                setText("全部"), 0);  //保证一定会有全部按钮
                        if (toyTypeBeanList != null) {
                            for (int i = 0; i < toyTypeBeanList.size(); i++) {
                                typeTabLayout.addTab(typeTabLayout.newTab().
                                        setText(toyTypeBeanList.get(i).getTOY_TYPE()), i + 1);
                            }
                            if (toyTypeBeanList.size() > 5) {
                                typeTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                            } else {
                                typeTabLayout.setTabMode(TabLayout.MODE_FIXED);
                            }
                        }
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
    private void getToysByType(String type, int page) {
        HttpManager.getInstance().getToyListByType(type, page, new RequestSubscriber<Result<RoomListBean>>() {
            @Override
            public void _onSuccess(Result<RoomListBean> loginInfoResult) {
                if (loginInfoResult.getMsg().equals("success")) {
                    if (loginInfoResult.getData() != null) {
                        List<RoomBean> roomBeens = dealWithRoomStats(loginInfoResult.getData().getDollList());
                        if (currentRoomBeens.size() == 0) {
                            currentRoomBeens = roomBeens;
                        } else {
                            //TODO 增加的
                            currentRoomBeens.addAll(roomBeens);
                        }
                        Collections.sort(currentRoomBeens, new Comparator<RoomBean>() {
                            @Override
                            public int compare(RoomBean t1, RoomBean t2) {
                                return t2.getDollState().compareTo(t1.getDollState());
                            }
                        });
                        zwwAdapter.notify(currentRoomBeens);
                        currentSumPage = loginInfoResult.getData().getPd().getTotalPage();
                        if (currentRoomBeens.size() > 2) {
                            mPullToRefreshView.setIsFooterView(true);
                        } else {
                            mPullToRefreshView.setIsFooterView(false);
                        }
                    }
                }
            }
            @Override
            public void _onError(Throwable e) {

            }
        });
    }
    private TabLayout.OnTabSelectedListener tabSelectedListener
            = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int pos = tab.getPosition();
            currentPage = 1;
            if (pos == 0) {
                currentType = "";
            } else {
                currentType = String.valueOf(toyTypeBeanList.get(pos - 1).getID());
            }
            currentRoomBeens.clear();
            getToysByType(currentType, currentPage);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkAvailable(getContext())) {
                    currentPage++;
                    if (currentPage > currentSumPage) {
                        //TODO 无更多了
                        MyToast.getToast(getContext(), "没有更多啦！").show();
                        mPullToRefreshView.onFooterRefreshComplete();
                        return;
                    }
                    getToysByType(currentType, currentPage);
                } else {
                    MyToast.getToast(getContext(), "网络连接异常，请检查网络").show();
                }
                mPullToRefreshView.onFooterRefreshComplete();
            }
        }, 1500);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkAvailable(getContext())) {

                } else {
                    MyToast.getToast(getContext(), "网络连接异常，请检查网络").show();
                }
                mPullToRefreshView.onHeaderRefreshComplete();
            }
        }, 1500);
    }





}
