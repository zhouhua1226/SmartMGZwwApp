package com.game.smartmgzwwapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.bean.PlateBean;

import java.util.List;

/**
 * Created by chenw on 2018/7/16.
 */

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareHolder>{
   private  Context mtx;
    private List<PlateBean> mPlate;
    private OnItemClickListener mOnItemClickListener;
    public ShareAdapter(Context _mtx,List<PlateBean> _mPlate,OnItemClickListener _mOnItemClickListener) {
        this.mtx=_mtx;
        this.mPlate=_mPlate;
        this.mOnItemClickListener=_mOnItemClickListener;
    }

    @Override
    public ShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView= LayoutInflater.from(mtx).inflate(R.layout.row_share_item, parent, false);
        ShareHolder mHolder=new ShareHolder(mView);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(ShareHolder holder, final int position) {
        holder.icon.setBackgroundResource(mPlate.get(position).id);
        holder.title.setText(mPlate.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlate.size();
    }

    class ShareHolder extends  RecyclerView.ViewHolder{
        private ImageView icon;
        private TextView title;
       public ShareHolder(View itemView) {
           super(itemView);
           icon= (ImageView) itemView.findViewById(R.id.iv_iamage_iocn);
           title= (TextView) itemView.findViewById(R.id.tv_share_title);
       }
   }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
