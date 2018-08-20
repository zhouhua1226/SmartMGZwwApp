package com.game.smartmgzwwapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.bean.PayCardBean;

import java.util.List;

/**
 * Created by chenw on 2018/7/17.
 */

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.RechargeHolder>{
    private Context mtx;
    private List<PayCardBean> mPlate;
    private String isFirst;
    private OnItemClickListener mOnItemClickListener;
    private Boolean isCharge=false;
    public RechargeAdapter(Context _mtx, String isFirst, List<PayCardBean> _mPlate, OnItemClickListener _mOnItemClickListener) {
        this.mtx=_mtx;
        this.mPlate=_mPlate;
        this.isFirst=isFirst;
        this.mOnItemClickListener=_mOnItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RechargeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView= LayoutInflater.from(mtx).inflate(R.layout.row_recharge_item, parent, false);
        RechargeHolder mHolder=new RechargeHolder(mView);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(RechargeHolder holder, final int position) {
        if(isFirst.equals("0")){
            holder.icon_notice.setVisibility(View.VISIBLE);
            holder.icon_money.setImageResource(R.drawable.recharge_10_icon0 + position);
            holder.money.setText("￥" + mPlate.get(position).getAMOUNT());
            holder.sendMoney.setText("送" + mPlate.get(position).getFIRSTAWARD() + "币");
            holder.summoney.setText("共 " + mPlate.get(position).getFIRSTAWARD_GOLD() + " 币");
            } else {
            holder.icon_notice.setVisibility(View.INVISIBLE);
            holder.icon_money.setImageResource(R.drawable.recharge_10_icon0 + position);
            holder.money.setText("￥" + mPlate.get(position).getAMOUNT());
            if( mPlate.get(position).getAWARD().equals("0")){
                holder.sendMoney.setVisibility(View.INVISIBLE);
            }else{
                holder.sendMoney.setVisibility(View.VISIBLE);
                holder.sendMoney.setText("送" + mPlate.get(position).getAWARD() + "币");
            }
            holder.summoney.setText("共 " + mPlate.get(position).getGOLD() + " 币");
            }

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

    public void setDataSetChanged(List<PayCardBean> mPayCardBeans) {
        mPlate=mPayCardBeans;
        notifyDataSetChanged();
    }

    public void setIsFirstReacher(String isFirst) {
        this.isFirst=isFirst;
    }


    class RechargeHolder extends  RecyclerView.ViewHolder{
        private LinearLayout llbg;
        private RelativeLayout rlbg;
        private ImageView icon_notice,icon_money;
        private TextView money,sendMoney,summoney;
        public RechargeHolder(View itemView) {
            super(itemView);
            llbg= (LinearLayout) itemView.findViewById(R.id.ll_chargere_bg);
            rlbg= (RelativeLayout) itemView.findViewById(R.id.rl_chargere_bg);
            icon_notice= (ImageView) itemView.findViewById(R.id.iv_recharge_first_notice);
            icon_money= (ImageView) itemView.findViewById(R.id.iv_recharge_money);
            money= (TextView) itemView.findViewById(R.id.tv_recharge_money);
            sendMoney= (TextView) itemView.findViewById(R.id.tv_send_money);
            summoney= (TextView) itemView.findViewById(R.id.tv_recharge_summoney);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
