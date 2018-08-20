package com.game.smartmgzwwapp.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.adapter.ShareAdapter;
import com.game.smartmgzwwapp.bean.PlateBean;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 22077 on 2017/6/15.
 */

public class ShareDialog {
    private final TextView cancel;
    private final RecyclerView mRecyclerView;
   private Dialog mAlertDialog;
    private View mView;
    private Context mContext;
    private List<PlateBean> mPlate=new ArrayList<>();

    private UMWeb web;
    private SHARE_MEDIA shareMedia;
    private OnShareIndexOnClicker onShareIndexOnClicker;
    public ShareDialog(Context context, OnShareIndexOnClicker onShareIndexOnClicker) {
        this.mContext = context;
        mAlertDialog = new Dialog(context, R.style.dialog);
        mAlertDialog.setCanceledOnTouchOutside(true);
        mAlertDialog.show();
        mAlertDialog.setContentView(R.layout.widget_share_dialog);

        cancel = (TextView) mAlertDialog.findViewById(R.id.tv_option_cancel);
        mRecyclerView = (RecyclerView) mAlertDialog.findViewById(R.id.share_recyclerview);
        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mAlertDialog.getWindow().setGravity(Gravity.BOTTOM);
        this.onShareIndexOnClicker=onShareIndexOnClicker;
        initMedia();
        setInitView();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });
    }
    private void setInitView() {
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_qq ,null,"QQ"));
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_qzone ,null,"QQ空间"));
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_wechat ,null,"微信"));
        mPlate.add(new PlateBean(R.drawable.umeng_socialize_wxcircle,null,"朋友圈"));
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(8));
        mRecyclerView.setAdapter(new ShareAdapter(mContext, mPlate, new ShareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mAlertDialog.dismiss();
                switch (position) {
                    //QQ好友
                    case 0:
                        shareMedia = SHARE_MEDIA.QQ;
                        break;
                    case 1://QQ朋友圈
                        shareMedia = SHARE_MEDIA.QZONE;
                        break;
                    case 2://微信好友
                        shareMedia = SHARE_MEDIA.WEIXIN;
                        break;
                    case 3://微信朋友圈
                        shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                        break;
                    case 4://新浪微博
                        shareMedia = SHARE_MEDIA.SINA;
                        break;
                }
                onShareIndexOnClicker.ShareIndexOnClicker(position, web, shareMedia);
            }

        }));

    }

    private void initMedia(){
        web = new UMWeb(mContext.getString(R.string.load_web));
        web.setTitle(mContext.getString(R.string.app_name));
        web.setDescription(mContext.getString(R.string.app_description));
        web.setThumb(new UMImage(mContext,BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_app_numshroom)));
    }
    //分享监听接口
  public interface  OnShareIndexOnClicker{
          void ShareIndexOnClicker(int index, UMWeb web, SHARE_MEDIA shareMedia);
    }
}
