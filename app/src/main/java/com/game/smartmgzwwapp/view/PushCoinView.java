package com.game.smartmgzwwapp.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.game.smartmgzwwapp.R;

/**
 * Created by mi on 2018/7/12.
 */

public class PushCoinView extends LinearLayout {
    private View mView;
    private ProgressBar mProgressBar;
    private int mPercent=0;
    public PushCoinView(Context context) {
        this(context, null);
    }

    public PushCoinView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.push_coin_day_view, this);
        mProgressBar=(ProgressBar)findViewById(R.id.progress);
    }

    public void  setProgress(int number) {
         int  xPercent= (int) (number * 0.47);
         setAnimation(xPercent);
    }
    private void setAnimation( int xPercent) {
        ValueAnimator animator = ValueAnimator.ofInt(mPercent, xPercent).setDuration(1200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgressBar.setProgress((int) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
        mPercent=xPercent;
    }

}


