package com.game.smartmgzwwapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.bean.MessageBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mi on 2018/3/28.
 */

public class MessageAdapter extends BaseAdapter {
    // 上下文本
    private Context context;
    private List<MessageBean> datas;
    private Animation animation;
    private Map<Integer,Boolean>  isFirst=new HashMap<Integer,Boolean>();
    /** 构造方法 实现初始化 */
    public MessageAdapter(Context context, List<MessageBean> list) {
        this.context = context;
        this.datas = list;
        animation= AnimationUtils.loadAnimation(context, R.anim.anim_come_in);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return datas.get(arg0);
    }
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    /** 获得视图*/
    @Override
    public View getView(int position, View view, ViewGroup parent) {
       ViewHolder holder;
        // 判断View是否为空
        if (view == null) {
            holder = new  ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            // 得到布局文件
            view = inflater.inflate(R.layout.message_loop_item, null);
            // 得到控件
            holder.tv_content = (TextView) view.findViewById(R.id.tv_message_content);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_message_name );
            view.setTag(holder);
        } else {
            holder = ( ViewHolder) view.getTag();
        }
        // 如果是第一次加载该view，则使用动画
        if (isFirst.get(position)) {
            view.setAnimation(animation);
            isFirst.put(position,false);
        }
        MessageBean messageBean=datas.get(position);
        if(messageBean!=null){
            // 给控件赋值
            holder.tv_name.setText(datas.get(position).getUserName()+":");
            holder.tv_content.setText(datas.get(position).getContent()+position);
        }
        return view;
    }

    /** 静态类 */
    static class ViewHolder {
        TextView tv_content;
        TextView tv_name;
    }

    public void  notifyDataChanged(MessageBean mb){
        datas.add(mb);
        isFirst.put((datas.size()-1),true);
        this.notifyDataSetChanged();
    }
}

