package com.game.smartmgzwwapp.activity.home;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.game.smartmgzwwapp.R;
import com.game.smartmgzwwapp.base.BaseActivity;
import com.game.smartmgzwwapp.bean.AppUserBean;
import com.game.smartmgzwwapp.bean.Result;
import com.game.smartmgzwwapp.model.http.HttpManager;
import com.game.smartmgzwwapp.model.http.RequestSubscriber;
import com.game.smartmgzwwapp.utils.Base64;
import com.game.smartmgzwwapp.utils.BitmapUtils;
import com.game.smartmgzwwapp.utils.LogUtils;
import com.game.smartmgzwwapp.utils.PermissionsUtils;
import com.game.smartmgzwwapp.utils.UrlUtils;
import com.game.smartmgzwwapp.utils.UserUtils;
import com.game.smartmgzwwapp.utils.Utils;
import com.game.smartmgzwwapp.view.MyToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.game.smartmgzwwapp.utils.PermissionsUtils.PERMISSIOM_CAMERA;
import static com.game.smartmgzwwapp.utils.PermissionsUtils.PERMISSIOM_EXTERNAL_STORAGE;


/**
 * Created by hongxiu on 2017/9/26.
 */
public class TakePhotoActivity extends BaseActivity {
    private static final String TAG = "TakePhotoActivity-";
    public static final int GALLERY_REQUEST_CODE = 2;    // 相册选图标记
    public static final int CAMERA_REQUEST_CODE = 3;    // 相机拍照标记
    public static final int GALLERY_PICTURE_CUT =4 ;// 图片裁剪

    private TextView shootTv;
    private TextView photoTv;
    private TextView cancelTv;
    private File file;
    private String base64;
    private Uri outputUri;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_picture;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        initFile();
    }

    private void initFile() {
        String files= null;
        try {
            files = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        } catch (NullPointerException e) {
            files= Environment.getExternalStorageDirectory()+"/temp/";
        }
        Utils.deleteAll(files);//清空
        file = new File(files + "/"+System.currentTimeMillis() + ".jpg");
        if (file.isFile() && file.exists()) {
            file.delete(); //如果存在删除
        }
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @OnClick({R.id.shoot_tv, R.id.photo_tv, R.id.cancel_tv})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.shoot_tv:
                takePhone();
                break;
            case R.id.photo_tv:
                selectPicture();
                break;
            case R.id.cancel_tv:
                finish();
                break;
        }
    }


    private void getFaceImage(String userId,String faceImage){
        HttpManager.getInstance().getFaceImage(userId, faceImage, new RequestSubscriber<Result<AppUserBean>>() {
            @Override
            public void _onSuccess(Result<AppUserBean> result) {
                if(result.getCode()==0) {
                    UserUtils.UserImage= UrlUtils.USERFACEIMAGEURL+result.getData().getAppUser().getIMAGE_URL();
                    MyToast.getToast(getApplicationContext(),"修改成功！").show();
                     finish();
                }else{
                    MyToast.getToast(getApplicationContext(),"修改失败!").show();
                }
            }
            @Override
            public void _onError(Throwable e) {
                LogUtils.loge("getFaceImage::::" + e.getMessage(),TAG);
            }
        });
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     *
     */
    private void takePhone() {
        PermissionsUtils.checkPermissions(this, new String[]{Manifest.permission.CAMERA},
                PERMISSIOM_CAMERA, new PermissionsUtils.PermissionsResultListener() {
                    @Override
                    public void onSuccessful() {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent,  CAMERA_REQUEST_CODE);
                    }

                    @Override
                    public void onFailure() {

                    }
                });

    }
    /**
     *
     */
    private void selectPicture() {
        PermissionsUtils.checkPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSIOM_EXTERNAL_STORAGE, new PermissionsUtils.PermissionsResultListener() {
                    @Override
                    public void onSuccessful() {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_REQUEST_CODE); // 打开相册
                    }

                    @Override
                    public void onFailure() {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case   GALLERY_REQUEST_CODE: //选择图片
                    if(data.getData()!=null){
                        cropPhoto(data.getData());
                    }
                    break;
                case   CAMERA_REQUEST_CODE: //照相图片
                    //  updateusrimg(file.getPath());
                    cropPhoto(Uri.fromFile(file));
                    break;
                case  GALLERY_PICTURE_CUT://裁剪
                     updateusrimg(outputUri.getPath());
                    break;
            }
        }
    }

    private void updateusrimg(String path) {
        Bitmap bitmap= BitmapUtils.compressImageFromFile(path);
        base64= Base64.encode(BitmapUtils.compressBmpFromBmp(bitmap));
        getFaceImage(UserUtils.USER_ID,base64);
    }

    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        // 创建File对象，用于存储裁剪后的图片，避免更改原图
        File file = new File(getExternalCacheDir(), "crop_image.jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //裁剪图片的宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("crop", "true");//可裁剪
        // 裁剪后输出图片的尺寸大小
        //intent.putExtra("outputX", 400);
        //intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出图片格式
        intent.putExtra("noFaceDetection", true);//取消人脸识别
        startActivityForResult(intent,  GALLERY_PICTURE_CUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.onRequestPermissionsResult(requestCode, permissions,  grantResults);
    }
}

