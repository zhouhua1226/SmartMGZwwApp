package com.game.smartmgzwwapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;


/**
 * Created chen mi on 2018/4/3.
 */

public class LoadProgressView extends Dialog  {
    private final static String TAG = "LoadProgressView";
    private Context context;
    private TextView mTextView;//提示
    private ProgressBar mProgressBar;//取消
    public LoadProgressView(Context context) {
        super(context);
    }
    public LoadProgressView(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public LoadProgressView(Context context, int theme) {
        super(context, theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_progress_dialog);
        setStyle();
        findView();
    }
    /**
     * 设置内容
     */
    public void setProbarPercent(float percent) {
        mProgressBar.setProgress((int) percent);
        mTextView.setText(percent+"%");
    }

    public void findView() {
        mTextView = (TextView) findViewById(R.id.mTextView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
    }

    private void setStyle() {
        //设置对话框不可取消
        this.setCancelable(false);
        //设置触摸对话框外面不可取消
        this.setCanceledOnTouchOutside(false);
       // this.getWindow().setGravity(Gravity.CENTER );

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //获得应用窗口大小
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        //设置对话框居中显示
        layoutParams.gravity = Gravity.CENTER;
         //设置对话框宽度为屏幕的4/5
        layoutParams.width = (displaymetrics.widthPixels/5)*4;
    }

}
