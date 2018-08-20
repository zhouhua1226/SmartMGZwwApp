package com.game.smartmgzwwapp.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.utils.PermissionsUtils;
import com.game.smartmgzwwapp.view.GuessingSuccessDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.message.PushAgent;


/**
 * Created by zhouh on 2017/9/7.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static GuessingSuccessDialog guessingSuccessDialog;
    private static final String TAG = "BaseActivity---";
    private boolean isConfigChange=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.apptheme_bg);
        setContentView(getLayoutId());
        afterCreate(savedInstanceState);
        MyApplication.getInstance().activities.add(this);
        PushAgent.getInstance(this).onAppStart();
        setPermissionsReadFile();
//        RxBus.get().register(this);
//        //initDialog();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(UserUtils.ACTION_LOTTERY);
//        this.registerReceiver(LotteryReceiver, intentFilter);
        //友盟统计
         MobclickAgent.setDebugMode(true);
         MobclickAgent.openActivityDurationTrack(false);
         MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_GAME);  //游戏场景
         UMGameAgent.setDebugMode(true);//设置输出运行时日志
         UMGameAgent.init( this );
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    protected abstract void initView();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isConfigChange = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().activities.remove(this);
//        RxBus.get().unregister(this);
//        this.unregisterReceiver(LotteryReceiver);
    }
    private void initDialog() {
        if (guessingSuccessDialog == null) {
            guessingSuccessDialog = new GuessingSuccessDialog(this, R.style.easy_dialog_style);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
        UMGameAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
        UMGameAgent.onPause(this);
    }

    /** 重写getResources方法，使app字体大小不受手机设置字体大小的影响  */
    @Override
    public Resources getResources() {
        //获取到resources对象
        Resources res = super.getResources();
        //修改configuration的fontScale属性
        res.getConfiguration().fontScale = 1;
        //将修改后的值更新到metrics.scaledDensity属性上
        res.updateConfiguration(null,null);
        return res;
    }

    /**
     *  Android7.0+ 权限文件读取时
     */
    private void   setPermissionsReadFile(){
        if (Build.VERSION.SDK_INT >=24) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    //设置状态栏
    public   void setStatusBarColor(int color){
        Window window =getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
    //设置全屏，显示状态栏
    public void setTranslucentStatus() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();

            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                   WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);

        }
    }
    /**
     * 设置当前页标题。
     */
    @Override
    public void setTitle(CharSequence title) {
        TextView titleTxt = (TextView) findViewById(R.id.title_textview);
        if (titleTxt != null) {
            titleTxt.setVisibility(View.VISIBLE);
            titleTxt.setText(title);
        }
    }

    /**
     * 设置左上角“返回”按钮的默认动作
     *
     * @param listener
     */
    protected void setLeftBtnOnClickListener(View.OnClickListener listener) {
        View view = findViewById(R.id.ll_leftBtn);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }
    /**
     * 设置左上角返回按钮的默认行为。
     */
    protected void setLeftBtnDefaultOnClickListener() {
        setLeftBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    protected void setLeftBtnGoneListener() {
        View view = findViewById(R.id.ll_leftBtn);
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.onRequestPermissionsResult(requestCode, permissions,  grantResults);
    }
}
