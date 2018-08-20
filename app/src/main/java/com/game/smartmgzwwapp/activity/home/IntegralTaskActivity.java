package com.game.smartmgzwwapp.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.activity.mushroom.HomeActivity;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.PermissionsUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.GifView;
import com.game.smartmgzwwapp.view.MyToast;
import com.game.smartmgzwwapp.view.ShareDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.game.smartmgzwwapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;


/**
 * Created by mi on 2018/8/13.
 */

public class IntegralTaskActivity extends BaseActivity {
    @BindView(R.id.lv_malayout)
    RelativeLayout mLayout;
    private  WebView webView;

    private GifView newswebGifView;
    private String TAG="IntegralActivity";
    private WebSettings s;
    private String urlPath;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_integral;
    }
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        getPointsMallUrl();
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);
        mLayout.addView(webView);

        newswebGifView = new GifView(getApplicationContext());
        newswebGifView.setLayoutParams(params);
        mLayout.addView(newswebGifView);
        newswebGifView.setEnabled(false);
        newswebGifView.setMovieResource(R.raw.waitloadinggif);
        newswebGifView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView!=null&&urlPath!=null){
            fresh();
        }
    }

    private void loadUrl(){
        Log.e(TAG,"图片新闻url="+urlPath);
        webView.loadUrl(urlPath.replace("\"", "/"));
        webView.reload();
    }
    //设置属性
    @SuppressLint("JavascriptInterface")
    private void fresh(){

        loadUrl();
        s= webView.getSettings();
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setJavaScriptEnabled(true);
        s.setGeolocationEnabled(true);
        s.setDomStorageEnabled(true);
        s.setBlockNetworkImage(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭WebView中缓存
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new  JsInterface(), "AndroidWebView");
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);

            }
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                s.setBlockNetworkImage(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    //webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                newswebGifView.setVisibility(View.GONE);
                MyToast.getToast(getApplicationContext(),"加载出错，请重试！");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if(progress == 100){
                    newswebGifView.setVisibility(View.GONE);
                    mLayout.removeView(newswebGifView);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }

    /**
     *积分商城
     */
    private void getPointsMallUrl(){
        HttpManager.getInstance().getPointsMallTask(UserUtils.USER_ID ,new RequestSubscriber<Result<String>>() {
            @Override
            public void _onSuccess(Result<String> loginInfoResult) {
                if(loginInfoResult.getData()!=null){
                    urlPath=loginInfoResult.getData();
                    fresh();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }

    //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
    private class JsInterface {
        public JsInterface( ) {
        }
        @JavascriptInterface
        public void onBacktask() {
            finish();
        }
        //分享
        @JavascriptInterface
        public void share() {
            PermissionsUtils.checkPermissions(IntegralTaskActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIOM_EXTERNAL_STORAGE, new PermissionsUtils.PermissionsResultListener() {
                        @Override
                        public void onSuccessful() {
                            shareApp();
                        }
                        @Override
                        public void onFailure() {
                        }
                    });
        }
        //邀请好友
        @JavascriptInterface
        public void  invite() {
            Utils.toActivity(IntegralTaskActivity.this,LnvitationCodeActivity.class);
        }
        //抓娃娃
        @JavascriptInterface
        public void  fist() {
            HomeActivity.mMainActivity.finish();
            Intent intent=new Intent(IntegralTaskActivity.this,HomeActivity.class);
            intent.putExtra("mainIndex",0);
            Utils.toActivity(IntegralTaskActivity.this,intent);
            finish();
        }
        //充值
        @JavascriptInterface
        public void recharge() {
            Utils.toActivity(IntegralTaskActivity.this,RechargeActivity.class);
        }
    }

    //分享
    private void shareApp() {
        new ShareDialog(this, new ShareDialog.OnShareIndexOnClicker() {
            @Override
            public void ShareIndexOnClicker(int index, final UMWeb web, final SHARE_MEDIA shareMedia) {
                new ShareAction(IntegralTaskActivity.this)
                        .withMedia(web)
                        .setPlatform(shareMedia)
                        .setCallback(shareListener).share();
            }
        });
    }


    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            shareGame();
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getApplicationContext(),"分享失败"+t.getMessage(),Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getApplicationContext(),"分享取消",Toast.LENGTH_SHORT).show();
        }
    };
    private void shareGame(){
        HttpManager.getInstance().shareGame(UserUtils.USER_ID ,new RequestSubscriber<Result<Void>>() {
            @Override
            public void _onSuccess(Result<Void> loginInfoResult) {
                if(loginInfoResult.getCode()==0){
                    Toast.makeText(getApplicationContext(),"分享成功",Toast.LENGTH_SHORT).show();
                    fresh();
                }
            }
            @Override
            public void _onError(Throwable e) {
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
