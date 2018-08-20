package com.game.smartmgzwwapp.model.http.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import com.game.smartmgzwwapp.utils.Utils;
import com.hwangjr.rxbus.RxBus;

import java.io.File;

/**
 * Created by chen on 2018/4/3.
 * 下载apk 安装
 */
public class DownLoadRunnable implements Runnable {
    private String url;
    private Context mContext;
    private File mFile;
    private  String title="汤姆抓娃娃.apk";
    public DownLoadRunnable(Context context, String url ) {
        this.mContext = context;
        this.url = url;
    }
    @Override
    public void run() {
        //具体下载方法
        startDownload();
    }
    private long startDownload() {
        //获得DownloadManager对象
        DownloadManager downloadManager=(DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //获得下载id，这是下载任务生成时的唯一id，可通过此id获得下载信息
        long requestId= downloadManager.enqueue(CreateRequest(url));
        //查询下载信息方法
        queryDownloadProgress(requestId,downloadManager);
        return  requestId;
    }

    private void queryDownloadProgress(long requestId, DownloadManager downloadManager) {

        DownloadManager.Query query=new DownloadManager.Query();
        //根据任务编号id查询下载任务信息
        query.setFilterById(requestId);
        try {
            boolean isGoging=true;
            UpdateInfo updateInfo=new UpdateInfo();
            while (isGoging) {
                Cursor cursor = downloadManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    //获得下载状态
                    int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    switch (state) {
                        case DownloadManager.STATUS_SUCCESSFUL://下载成功
                            isGoging=false;
                            updateInfo.setState(downloadManager.STATUS_SUCCESSFUL);
                            RxBus.get().post(Utils.TAG_DOWN_LOAD, updateInfo);
                            install();
                            break;
                        case DownloadManager.STATUS_FAILED://下载失败
                            isGoging=false;
                            updateInfo.setState(downloadManager.STATUS_FAILED);//发送到主线程，更新ui
                            RxBus.get().post(Utils.TAG_DOWN_LOAD, updateInfo);
                            break;
                        case DownloadManager.STATUS_RUNNING://下载中
                            /**
                             * 计算下载下载率；
                             */
                            int totalSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                            int currentSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            int progress = (int) (((float) currentSize) / ((float) totalSize) * 100);
                         //   handler.obtainMessage(downloadManager.STATUS_RUNNING, progress).sendToTarget();//发送到主线程，更新ui
                            updateInfo.setState(downloadManager.STATUS_RUNNING);
                            updateInfo.setProgress(progress);
                            RxBus.get().post(Utils.TAG_DOWN_LOAD, updateInfo);
                            break;

                        case DownloadManager.STATUS_PAUSED://下载停止
                            updateInfo.setState(downloadManager.STATUS_PAUSED);
                            RxBus.get().post(Utils.TAG_DOWN_LOAD, updateInfo);
                            break;

                        case DownloadManager.STATUS_PENDING://准备下载
                            updateInfo.setState(downloadManager.STATUS_PENDING);
                            RxBus.get().post(Utils.TAG_DOWN_LOAD, updateInfo);
                            break;
                    }
                }
                if(cursor!=null){
                    cursor.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private DownloadManager.Request CreateRequest(String url) {
        if(url==null || url.equals("")){
            return null;
        }
        //存储位置为Android/data/包名/file/Download文件夹
        String files= mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        Utils.deleteAll(files);
        //存储位置为Android/data/包名/file/Download文件夹
        mFile = new File(files + "/"+title);
         if (mFile.isFile() && mFile.exists()) {
             mFile.delete(); //如果存在删除
        }
        DownloadManager.Request  request=new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);// 隐藏notification
       //  request.setAllowedNetworkTypes(request.NETWORK_WIFI);//设置下载网络环境为wifi
        request.setDestinationUri(Uri.fromFile(mFile));
        return  request;
    }
       /**
         * 下载完成安装apk
         */
    private void install() {
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installintent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");//存储位置为Android/data/包名/file/Download文件夹
        mContext.startActivity(installintent);
   }



 public class UpdateInfo {
     private int state;
     private int progress=0;
     public int getState() {
         return state;
     }
     public void setState(int state) {
         this.state = state;
     }
     public int getProgress() {
         return progress;
     }
     public void setProgress(int progress) {
         this.progress = progress;
     }
 }
}
