package com.game.smartmgzwwapp.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.utils.PermissionsUtils;

import static com.game.smartmgzwwapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;


/**
 * Created by yincong on 2017/11/30 19:44
 * 修改人：chen
 * 修改时间：
 * 类描述：
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {

    private final static String TAG = "UpdateDialog";
    private Context mtx;
    private TextView tv_title;//提示
    private Button dl_btn_confirm;//确定

    public UpdateDialog(Context context) {
        super(context);
    }

    public UpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
        mtx=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);
        findView();
        setListner();
        setStyle();
    }

    /**
     * 设置确认按钮的文字
     */
    public void setDialogConfirmText(String confirmText) {
        dl_btn_confirm.setText(confirmText);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setDialogTitle(String title) {
        tv_title.setText(title);
    }

    public void findView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        dl_btn_confirm = (Button) findViewById(R.id.update_sure_btn);
    }

    /**
     * 绑定监听
     **/
    private void setListner() {
        dl_btn_confirm.setOnClickListener(this);
    }

    private void setStyle() {
        //设置对话框不可取消
        this.setCancelable(false);
        //设置触摸对话框外面不可取消
        this.setCanceledOnTouchOutside(false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //获得应用窗口大小
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        //设置对话框居中显示
        layoutParams.gravity = Gravity.CENTER;
        //设置对话框宽度为屏幕的4/5
        layoutParams.width = (displaymetrics.widthPixels / 5) * 4;
    }

    /**
     * 点击监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_sure_btn:
                PermissionsUtils.checkPermissions((Activity) mtx, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIOM_EXTERNAL_STORAGE, new PermissionsUtils.PermissionsResultListener() {
                            @Override
                            public void onSuccessful() {
                                if (null !=listener) {
                                    listener.getResult(true);
                                }
                                UpdateDialog.this.dismiss();
                            }

                            @Override
                            public void onFailure() {

                            }
                        });
                break;

        }
    }

    private DialogResultListener listener;

    public void setDialogResultListener(DialogResultListener listener) {
        this.listener = listener;
    }

    public interface DialogResultListener {
        /**
         * 获取结果的方法
         *
         * @param sure false.取消  ture.确定
         */
        void getResult(boolean sure);
    }
}
