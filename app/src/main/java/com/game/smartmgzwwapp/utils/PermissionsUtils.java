package com.game.smartmgzwwapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mi on 2018/8/2.
 */

public class PermissionsUtils {
    public static int PERMISSIOM_EXTERNAL_STORAGE=0x0030;
    public static int PERMISSIOM_CAMERA=0x0031;
    public static Context mtx;
    public static PermissionsResultListener mListener;
    public static int mRequestCode;
    public static List<String> mListPermissions;

    //判断权限是否申请
    public static boolean isHavePermissions(String permissions) {
        if (ContextCompat.checkSelfPermission(mtx, permissions) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    //申请权限
    public static void applyPermissions() {
        if (!mListPermissions.isEmpty()) {
            int size = mListPermissions.size();
            ActivityCompat.requestPermissions((Activity) mtx, mListPermissions.toArray(new String[size]), mRequestCode);
        } else {
            mListener.onSuccessful();
        }
    }

    public static void checkPermissions(Activity _Activity, String[] permissions, int requestCode, PermissionsResultListener listener) {
        //权限不能为空
        if (permissions != null || permissions.length != 0) {
            mtx = _Activity;
            mListener = listener;
            mRequestCode = requestCode;
            mListPermissions = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (!isHavePermissions(permissions[i])) {
                    mListPermissions.add(permissions[i]);
                }
            }
            //遍历完后申请
            applyPermissions();
        }
    }

    /**
     * 权限处理结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == mRequestCode) {
            if (grantResults.length > 0) {
                int agreeSize = 0;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        agreeSize++;
                    }
                }
                if (agreeSize == grantResults.length) {
                    if(mListener!=null){
                        mListener.onSuccessful();
                    }
                } else {
                    if(mListener!=null){
                        mListener.onFailure();
                    }
                 //    setPermission();
                }
            } else {
                Toast.makeText(mtx, "失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //手动设置权限
    public static void setPermission() {
        AlertDialog dialog = new AlertDialog.Builder(mtx)
                .setMessage("设置应用权限")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", mtx.getPackageName(), null);
                        intent.setData(uri);
                        mtx.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();
    }

    public interface PermissionsResultListener {
        //全部成功
        void onSuccessful();
        // 部分授权成功
        //  void onContinue(int[] grantResults);
        //失败
           void onFailure();
    }
}
