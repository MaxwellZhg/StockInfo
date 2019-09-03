package com.zhuorui.securities.openaccount.widget;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceView;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.zhuorui.securities.base2app.BaseApplication;
import com.zhuorui.securities.base2app.util.ToastUtil;
import com.zhuorui.securities.openaccount.camera.CameraHelper;

import java.security.InvalidParameterException;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/8/30 16:48
 * desc   : 自定义相机View
 */
public class CameraView extends SurfaceView implements CheckRequestPermissionsListener, CameraHelper.CompleteListener {

    // 初始化是否完成
    private boolean mInited;
    // 相机封装
    private CameraHelper cameraHelper;
    // 拍摄结果回调
    private TakePhotoCallBack takePhotoCallBack;
    private RecordVedioCallBack recordVedioCallBack;
    // 是否打开闪光灯
    private boolean isOpenFlash;
    // 是否是后置摄像头
    private boolean backFacing;

    public CameraView(Context context) {
        super(context);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param backFacing 使用哪个摄像头
     *                   true 后置
     *                   false 前置
     */
    public void init(boolean backFacing) {
        if (mInited) return;
        this.backFacing = backFacing;
        // 请求权限
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), this);
    }

    @Override
    public void onAllPermissionOk(Permission[] allPermissions) {
        // 获得权限，初始化录制界面
        mInited = true;
        cameraHelper = new CameraHelper(getContext());
        int cameraFacingType = backFacing ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        cameraHelper.setCameraPosition(cameraFacingType);
        cameraHelper.setSurfaceView(this);
        cameraHelper.setCompleteListener(this);
    }

    @Override
    public void onPermissionDenied(Permission[] refusedPermissions) {
        // 没有权限
        mInited = false;
        ToastUtil.Companion.getInstance().toast("没有权限");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    /**
     * 拍摄照片
     */
    public void takePhoto(TakePhotoCallBack callBack) {
        this.takePhotoCallBack = callBack;
        // 调用拍照
        cameraHelper.capture();
    }

    /**
     * 重置相机，需要恢复拍摄时调用
     */
    public void resetCamera() {
        cameraHelper.resetCamera();
    }

    /**
     * 拍摄视频
     *
     * @param duration 需要拍多长时间
     * @param callBack 拍摄结果返回
     */
    public void recordVedio(long duration, RecordVedioCallBack callBack) {
        this.recordVedioCallBack = callBack;

        cameraHelper.setTargetDirAndTargetName(BaseApplication.Companion.getContext().getExternalCacheDir(), System.currentTimeMillis() + ".mp4");

        // 调用录制
        cameraHelper.record();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止录制
                cameraHelper.stopRecord();
            }
        }, duration);
    }

    @Override
    public void onComplete(Bitmap displayBitmap) {
        if (takePhotoCallBack != null)
            takePhotoCallBack.onComplete(displayBitmap);
    }

    @Override
    public void onComplete(byte[] data) {
        if (recordVedioCallBack != null)
            recordVedioCallBack.onComplete(data);
    }

    /**
     * 释放资源
     */
    public void release() {
        if (cameraHelper != null) {
            cameraHelper.release();
        }
    }

    public void onResume() {
        if (!mInited) {
            init(backFacing);
            return;
        }
        if (cameraHelper != null && cameraHelper.mOrientationListener != null) {
            cameraHelper.mOrientationListener.enable();
        }
    }

    public void onPause() {
        if (cameraHelper != null && cameraHelper.mOrientationListener != null) {
            cameraHelper.mOrientationListener.disable();
        }
    }

    /**
     * 开关闪光灯
     */
    public boolean switchFlash() {
        if (cameraHelper != null) {
            cameraHelper.setOpenFlashMode(isOpenFlash ? Camera.Parameters.FLASH_MODE_ON : Camera.Parameters.FLASH_MODE_OFF);
            isOpenFlash = !isOpenFlash;
        } else {
            return false;
        }
        return isOpenFlash;
    }

    /**
     * 拍摄照片回调照片流
     */
    public interface TakePhotoCallBack {
        void onComplete(Bitmap displayBitmap);
    }

    /**
     * 拍摄视频回调视频流
     */
    public interface RecordVedioCallBack {
        void onComplete(byte[] vedioBytes);
    }
}