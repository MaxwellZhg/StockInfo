package com.zhuorui.securities.openaccount.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.zhuorui.securities.base2app.BaseApplication;
import com.zhuorui.securities.base2app.infra.LogInfra;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pengxianglin on 2017/11/16.
 */
public class CameraHelper implements SurfaceHolder.Callback {
    private static final String TAG = "MediaUtils";
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mediaPlayer;
    private CamcorderProfile profile;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private File targetFile;
    private int previewWidth = 1280, previewHeight = 720;
    private boolean isRecording;
    private boolean isTakeing;
    private final int orientation_0 = 0, orientation_90 = 90, orientation_180 = 180, orientation_270 = 270, orientation_360 = 360;
    private int screenOrientation;
    private int cameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT; //1代表前置摄像头，0代表后置摄像头
    private CompleteListener completeListener;
    public OrientationEventListener mOrientationListener;
    private LinkedList<Disposable> disposables = new LinkedList<>();

    public CameraHelper(final Context context) {
        mOrientationListener = new OrientationEventListener(context) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation > 325 || orientation <= 45) {
                    screenOrientation = 0;
                } else if (orientation > 45 && orientation <= 135) {
                    screenOrientation = 270;
                } else if (orientation > 135 && orientation < 225) {
                    screenOrientation = 180;
                } else {
                    screenOrientation = 90;
                }
                LogInfra.Log.d(TAG, "orientation: " + screenOrientation);
            }
        };
    }

    public void setTargetDirAndTargetName(File targetDir, String targetFileName) {
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        targetFile = new File(targetDir, targetFileName);
    }

    private void deleteTargetFile() {
        if (targetFile == null) return;
        if (targetFile.exists()) {
            targetFile.delete();
        }
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.setFixedSize(previewWidth, previewHeight);
        mSurfaceHolder.addCallback(this);
    }

    /**
     * 拍照
     */
    public void capture() {
        if (prepareCapture()) {
            try {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (isRecording || isTakeing) return;
                        isTakeing = true;
                        try {
                            camera.takePicture(null, null, pictureCallback);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置相机横竖屏
     *
     * @param cameraId
     * @param camera
     */
    private void setCameraDisplayOrientation(int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = BaseApplication.Companion.getBaseApplication().getTopActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    private final Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            if (data != null) {
                try {
                    // 暂停
                    releaseCamera();

                    if (completeListener != null && data != null) {
                        try {
                            //  在子线程中计算视频流的Base64码，然后上传
                            Disposable disposable = Observable.create(new ObservableOnSubscribe<Bitmap>() {
                                @Override
                                public void subscribe(ObservableEmitter<Bitmap> emitter) {

                                    // 在子线程中解析生成相机返回的图片
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    //实际拍摄的方向
                                    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                                    android.hardware.Camera.getCameraInfo(cameraPosition, info);
                                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                        if (screenOrientation == orientation_0) {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_270);
                                        } else if (screenOrientation == orientation_90) {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_0);
                                        } else if (screenOrientation == orientation_180) {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_90);
                                        } else {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_180);
                                        }
                                    } else {
                                        if (screenOrientation == orientation_0) {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_90);
                                        } else if (screenOrientation == orientation_90) {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_0);
                                        } else if (screenOrientation == orientation_180) {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_270);
                                        } else {
                                            bitmap = rotateBitmapByDegree(bitmap, orientation_180);
                                        }
                                    }

                                    emitter.onNext(bitmap);
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Bitmap>() {
                                        @Override
                                        public void accept(Bitmap bitmap) {
                                            completeListener.onComplete(bitmap);
                                        }
                                    });
                            disposables.add(disposable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isTakeing = false;
            }
        }
    };

    /**
     * 录制
     */
    public void record() {
        if (isRecording) {
            try {
                mMediaRecorder.stop();  // stop the recording
            } catch (RuntimeException e) {
                Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
                //noinspection ResultOfMethodCallIgnored
                deleteTargetFile();
            }
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder
            isRecording = false;
        } else {
            startRecordThread();
        }
    }

    private boolean prepareCapture() {
        try {
            if (null != mCamera) {
                Camera.Parameters params = mCamera.getParameters();
                params.setPreviewSize(previewWidth, previewHeight);
                params.setPictureSize(previewWidth, previewHeight);
                //设置图片格式
                params.setPictureFormat(ImageFormat.JPEG);
                params.setJpegQuality(100);
                params.setJpegThumbnailQuality(100);
                List<String> modes = params.getSupportedFocusModes();
                if (modes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    //支持自动聚焦模式
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                mCamera.setParameters(params);
                setCameraDisplayOrientation(cameraPosition, mCamera);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean prepareRecord() {
        try {
            mMediaRecorder = new MediaRecorder();
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setProfile(profile);
            // 实际视屏录制后的方向
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraPosition, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                if (screenOrientation == orientation_0) {
                    mMediaRecorder.setOrientationHint(orientation_270);
                } else if (screenOrientation == orientation_90) {
                    mMediaRecorder.setOrientationHint(orientation_0);
                } else if (screenOrientation == orientation_180) {
                    mMediaRecorder.setOrientationHint(orientation_90);
                } else {
                    mMediaRecorder.setOrientationHint(orientation_180);
                }
            } else {
                if (screenOrientation == orientation_0) {
                    mMediaRecorder.setOrientationHint(orientation_90);
                } else if (screenOrientation == orientation_90) {
                    mMediaRecorder.setOrientationHint(orientation_0);
                } else if (screenOrientation == orientation_180) {
                    mMediaRecorder.setOrientationHint(orientation_270);
                } else {
                    mMediaRecorder.setOrientationHint(orientation_180);
                }
            }
            mMediaRecorder.setOutputFile(targetFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MediaRecorder", "Exception prepareRecord: ");
            releaseMediaRecorder();
            return false;
        }
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("MediaRecorder", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("MediaRecorder", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public void stopRecord() {
        Log.d("Recorder", "stopRecordSave");
        if (isRecording) {
            isRecording = false;
            try {
                mMediaRecorder.stop();
                releaseCamera();
                Log.d("Recorder", targetFile.getPath());
                if (completeListener != null) {
                    // TODO 返回视频数据
                    completeListener.onComplete((byte[]) null);
                    // 删除文件
                    deleteTargetFile();
                }
            } catch (RuntimeException r) {
                Log.d("Recorder", "RuntimeException: stop() is called immediately after start()");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                releaseMediaRecorder();
            }
        }
    }

    /**
     * 开始预览
     *
     * @param holder
     */
    private void startPreView(SurfaceHolder holder) {
        if (mCamera == null) {
            //根据用户的切换选择摄像头
            mCamera = Camera.open(cameraPosition);
        }
        if (mCamera != null) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraPosition, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                if (info.orientation == orientation_270) {
                    mCamera.setDisplayOrientation(orientation_90);
                }
            } else {
                if (info.orientation == orientation_90) {
                    mCamera.setDisplayOrientation(orientation_90);
                } else if (info.orientation == orientation_270) {
                    mCamera.setDisplayOrientation(orientation_270);
                }
            }
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(previewWidth, previewHeight);
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                profile.videoCodec = MediaRecorder.VideoEncoder.H264;
                // 这里是重点，分辨率和比特率
                // 分辨率越大视频大小越大，比特率越大视频越清晰
                // 清晰度由比特率决定，视频尺寸和像素量由分辨率决定
                // 比特率越高越清晰（前提是分辨率保持不变），分辨率越大视频尺寸越大。
                profile.videoFrameWidth = previewWidth;
                profile.videoFrameHeight = previewHeight;
                // 这样设置 1080p的视频 大小在5M , 可根据自己需求调节
                profile.videoBitRate = 3 * previewWidth * previewHeight;
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    //支持连续自动对焦模式
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            Log.d("Recorder", "release Recorder");
        }
    }

    public void release() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        releaseMediaPlayer();
        releaseCamera();
        releaseMediaRecorder();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();//停止播放
            }
            // release the mediaPlayer
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d("Recorder", "release mediaPlayer");
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
            Log.d("Recorder", "release Camera");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPreView(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        previewWidth = width;
        previewHeight = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            Log.d(TAG, "surfaceDestroyed: ");
            releaseCamera();
        }
        if (mMediaRecorder != null) {
            releaseMediaRecorder();
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            releaseMediaPlayer();
        }
    }

    private void startRecordThread() {
        if (prepareRecord()) {
            try {
                mMediaRecorder.start();
                isRecording = true;
                Log.d("Recorder", "Start Record");
            } catch (RuntimeException r) {
                releaseMediaRecorder();
                Log.d("Recorder", "RuntimeException: start() is called immediately after stop()");
            }
        }
    }

    /**
     * 设置拍摄完成回调
     *
     * @param completeListener 返回接口
     */
    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        int cameraCount;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == Camera.CameraInfo.CAMERA_FACING_BACK) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    cameraPosition = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    startPreView(mSurfaceHolder);
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    cameraPosition = Camera.CameraInfo.CAMERA_FACING_BACK;
                    startPreView(mSurfaceHolder);
                    break;
                }
            }
        }
    }

    /**
     * 旋转图片
     */
    private Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public void resetCamera() {
        if (mSurfaceHolder != null)
            startPreView(mSurfaceHolder);
    }

    public interface CompleteListener {

        /**
         * 返回拍摄的照片
         *
         * @param displayBitmap
         */
        void onComplete(Bitmap displayBitmap);

        /**
         * 返回拍摄的视频流
         *
         * @param data
         */
        void onComplete(byte[] data);
    }
}