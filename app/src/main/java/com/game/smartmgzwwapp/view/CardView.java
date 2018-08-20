package com.game.smartmgzwwapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by chenw on 2018/7/19.
 */

public class CardView extends LinearLayout {

    public Context mtx;
    public int logo;
    public int overtime;
    public boolean isShow;
    public   int type=0; //0 无卡  1，周卡，2，月卡
    private OnCardViewOnClickListener mOnCardViewOnClickListener;
    public CardView(Context context) {
        super(context);
        this.mtx=context;
        initView();
    }
    public CardView(Context context,int type,int logo,int overtime,boolean isShow,OnCardViewOnClickListener
            onCardViewOnClickListener) {
        super(context);
        this.mtx=context;
        this.logo=logo;
        this.overtime=overtime;
        this.type=type;
        this.isShow=isShow;
        this.mOnCardViewOnClickListener=onCardViewOnClickListener;
        initView();
    }
    private void initView() {
         View mView= LayoutInflater.from(mtx).inflate(R.layout.widget_card_view, this);
         TextView cardData=(TextView)findViewById(R.id.tv_card_data);
         RelativeLayout cardBg=(RelativeLayout)findViewById(R.id.view_card_bg);
         cardBg.setBackgroundResource(logo);
         if(isShow) {
             Calendar cal = Calendar.getInstance();
             cal.add(Calendar.DATE, overtime);
             String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
             cardData.setText("有效期:" + tomorrow);
        }
        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnCardViewOnClickListener!=null){
                    mOnCardViewOnClickListener.OnCardViewOnClick(type);
                }
            }
        });
    }

  public interface  OnCardViewOnClickListener{
      void OnCardViewOnClick(int type);
  }

}

