package com.game.smartmgzwwapp.model.http.download;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.game.smartmgzwwapp.utils.Utils;

import java.io.File;

/**
 * Created by mi on 2018/7/24.
 */

public class DownloadManagerUtil {
    private Context mContext;
    private String title = "汤姆抓娃娃.apk";
    private String desc = "下载完成后，点击安装";

    public DownloadManagerUtil(Context context) {
        mContext = context;
    }

    public long download(String url) {
        //存储位置为Android/data/包名/file/Download文件夹
        String files = null;
        try { //Android/data/包名/file/Download为null
            files = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        } catch (NullPointerException e) {
            files = Environment.getExternalStorageDirectory() + "/temp/";
        }
        Utils.deleteAll(files);
        //存储位置为Android/data/包名/file/Download文件夹

        File mFile = new File(files + "/" + title);
        if (mFile.isFile() && mFile.exists()) {
            mFile.delete(); //如果存在删除
        }
        Uri uri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        //设置WIFI下进行更新
        //  req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //下载中和下载完后都显示通知栏
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0

        req.setDestinationUri(Uri.fromFile(mFile));
        //通知栏标题
        req.setTitle(title);
        //通知栏描述信息
        req.setDescription(desc);
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");
        //获取下载任务ID
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        return dm.enqueue(req);
    }

    /**
     * 下载前先移除前一个任务，防止重复下载
     *
     * @param downloadId
     */
    public void clearCurrentTask(long downloadId) {
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            dm.remove(downloadId);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 针对手机下载管理器禁用
     */
    public boolean isDownloadManagerAvailable() {
        int state = mContext.getPackageManager().getApplicationEnabledSetting(
                "com.android.providers.downloads");
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            //打开下载器管理
            String packageName = "com.android.providers.downloads";
            try {
                Intent intent = new Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                mContext.startActivity(intent);

            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(
                        android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                mContext.startActivity(intent);
            }
            return false;
        } else {//正常下载
            return true;
        }
    }
}

