package com.game.smartmgzwwapp.adapter.numshroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mi on 2018/8/9.
 */

public class RankCatchAdapter extends RecyclerView.Adapter<RankCatchAdapter.ListRankViewHolder> {

    private Context mContext;
    private List<UserBean> mDatas=new ArrayList<>();
    private LayoutInflater mInflater;
    private RankCatchAdapter.OnItemClickListener mOnItemClickListener;

    public RankCatchAdapter(Context context, List<UserBean>list ){
        this.mContext=context;
        this.mDatas=list;
        mInflater=LayoutInflater.from(context);

    }

    public interface OnItemClickListener{
        void onItemClick(int position);

    }

    public void setOnItemClickListener(RankCatchAdapter.OnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

    @Override
    public RankCatchAdapter.ListRankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=mInflater.inflate(R.layout.list_brank_catch_item,parent,false);
        RankCatchAdapter.ListRankViewHolder listRankViewHolder=new RankCatchAdapter.ListRankViewHolder(view);
        return listRankViewHolder;
    }

    @Override
    public void onBindViewHolder(final RankCatchAdapter.ListRankViewHolder holder, final int position) {
        UserBean bean = mDatas.get(position);
        holder.rank_order.setText((position + 4) + "");
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
            holder.rank_number.setText(bean.getDOLLTOTAL()+"次抓中");
        if (bean.getNICKNAME().equals("")) {
            holder.rank_name.setText(bean.getUSERNAME());
        } else {
            holder.rank_name.setText(bean.getNICKNAME());
        }

    }

    public void notify(List<UserBean> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ListRankViewHolder extends RecyclerView.ViewHolder{

        TextView rank_name,rank_number,rank_order;
        public ListRankViewHolder(View itemView) {
            super(itemView);
            rank_order= (TextView) itemView.findViewById(R.id.tv_brank_catch_order);
            rank_name= (TextView) itemView.findViewById(R.id.tv_brank_catch_name);
            rank_number= (TextView) itemView.findViewById(R.id.tv_brank_catch_number);

        }
    }
}

