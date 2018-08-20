package com.game.smartmgzwwapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.bean.GameListBean;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.view.GlideCircleTransform;

import java.util.List;

/**
 * Created by yincong on 2018/4/3 13:55
 * 修改人：
 * 修改时间：
 * 类描述：房间游戏记录
 */
public class PlayRecordAdapter extends RecyclerView.Adapter<PlayRecordAdapter.MyViewHolder> {

    private Context mContext;
    private List<GameListBean> mDatas;
    private LayoutInflater mInflater;

    public PlayRecordAdapter(Context context, List<GameListBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_playrecord, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name_tv.setText(mDatas.get(position).getNICKNAME());
        holder.time_tv.setText(mDatas.get(position).getCREATE_DATE());
        if (mDatas.get(position).getSTATE().equals("0")){
            holder.result_tv.setText("抓取失败");
            holder.result_tv.setTextColor(mContext.getResources().getColor(R.color.zww_broadcast_text));
        }else {
            holder.result_tv.setText("抓取成功");
            holder.result_tv.setTextColor(mContext.getResources().getColor(R.color.green));
        }

        Glide.with(mContext)
                .load(UrlUtils.APPPICTERURL+mDatas.get(position).getIMAGE_URL())
                .dontAnimate()
                .transform(new GlideCircleTransform(mContext))
                .into(holder.image_iv);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv, time_tv, result_tv;
        ImageView image_iv;

        public MyViewHolder(View view) {
            super(view);
            name_tv = (TextView) view.findViewById(R.id.name_tv);
            time_tv = (TextView) view.findViewById(R.id.time_tv);
            result_tv = (TextView) view.findViewById(R.id.result_tv);
            image_iv= (ImageView) view.findViewById(R.id.image_iv);
        }

    }

    public void notify(List<GameListBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }

}