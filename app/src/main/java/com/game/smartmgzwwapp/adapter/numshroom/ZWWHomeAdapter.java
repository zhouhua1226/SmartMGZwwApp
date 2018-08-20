package com.game.smartmgzwwapp.adapter.numshroom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.bean.RoomBean;
import com.game.smartmgzwwapp.utils.UrlUtils;

import java.util.List;

/**
 * Created by mi on 2018/8/9.
 */

public class ZWWHomeAdapter extends RecyclerView.Adapter<ZWWHomeAdapter.ZWWViewHolder> {
    private Context mContext;
    private List<RoomBean> mDatas;
    private  OnItemClickListener mOnItemClickListener;

    public ZWWHomeAdapter(Context context, List<RoomBean> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public ZWWHomeAdapter.ZWWViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_zww_home_item, parent, false);
        ZWWHomeAdapter.ZWWViewHolder zwwViewHolder = new ZWWHomeAdapter.ZWWViewHolder(view);
        return zwwViewHolder;
    }

    @Override
    public void onBindViewHolder(ZWWHomeAdapter.ZWWViewHolder holder, final int position) {
        RoomBean bean = mDatas.get(position);
        holder.money.setText(bean.getDollGold() + "次抓中");
        holder.name.setText(bean.getDollName());
        Glide.with(mContext).load(UrlUtils.APPPICTERURL + bean.getDollUrl())
                .error(R.drawable.loading)
                .into(holder.imageView);
        holder.itemView.setEnabled(true);
        if (bean.getDollState().equals("10")) {
            holder.connectIv.setBackgroundResource(R.drawable.icon_gree_potion);
            holder.connectTv.setText("空闲中");
            holder.connectTv.setTextColor(Color.parseColor("#39e100"));
        } else if (bean.getDollState().equals("11")) {
            holder.connectIv.setBackgroundResource(R.drawable.icon_yellow_potion);
            holder.connectTv.setText("游戏中");
            holder.connectTv.setTextColor(Color.parseColor("#fcd803"));
        } else {
            holder.connectIv.setBackgroundResource(R.drawable.icon_yellow_potion);
            holder.itemView.setEnabled(false);
            holder.connectTv.setText("维护中");
            holder.connectTv.setTextColor(Color.parseColor("#fcd803"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ZWWViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView money, connectTv;
        private ImageView connectIv;

        public ZWWViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.rriv_moppet_image);
            name = (TextView) itemView.findViewById(R.id.tv_moppet_name);
            money = (TextView) itemView.findViewById(R.id.tv_moppet_gold);
            connectIv = (ImageView) itemView.findViewById(R.id.iv_moppet_state);
            connectTv = (TextView) itemView.findViewById(R.id.tv_moppet_state);
        }
    }

    public void notify(List<RoomBean> lists) {
        this.mDatas = lists;
        notifyDataSetChanged();
    }

    public void setmOnItemClickListener( OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }
}
