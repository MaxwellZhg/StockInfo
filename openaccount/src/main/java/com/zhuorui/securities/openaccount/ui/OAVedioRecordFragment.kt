package com.zhuorui.securities.openaccount.ui

import android.Manifest
import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import com.qw.soul.permission.SoulPermission
import com.qw.soul.permission.bean.Permission
import com.qw.soul.permission.bean.Permissions
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaVediorecordBinding
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.ui.presenter.OAVedioRecordPresenter
import com.zhuorui.securities.openaccount.ui.view.OAVedioRecordView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAVedioRecordViewModel
import com.zhuorui.securities.openaccount.utils.FileToBase64Util
import com.zhuorui.securities.openaccount.utils.FormatUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_oa_vediorecord.*
import me.jessyan.autosize.utils.LogUtils
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/28
 * Desc:
 */
class OAVedioRecordFragment :
    AbsSwipeBackNetFragment<FragmentOaVediorecordBinding, OAVedioRecordViewModel, OAVedioRecordView, OAVedioRecordPresenter>(),
    OAVedioRecordView, View.OnClickListener, CheckRequestPermissionsListener {
    private var camera: Camera? = null
    private var cameraSurfaceHolder: SurfaceHolder? = null
    private val mParams: Camera.Parameters? = null
    private var mediaRecorder: MediaRecorder? = null     //录制视频类
    private var isPreview = false             //摄像区域是否准备良好  
    private var isRecording = true           // true表示没有录像，点击开始；false表示正在录像，点击暂停  
    private var mRecVideoPath: File? = null
    private var mRecAudioFile: File? = null
    private val MAX_RECORD_TIME = 6 * 1000
    private val PLUSH_PROGRESS = 1000
    private val max = MAX_RECORD_TIME / PLUSH_PROGRESS
    private var cameraType = 1
    private var mPreBuffer = ByteArray(400)
    var compositeDisposable = CompositeDisposable()
    private var mProgress = 0
    override val layout: Int
        get() = R.layout.fragment_oa_vediorecord
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAVedioRecordPresenter
        get() = OAVedioRecordPresenter()
    override val createViewModel: OAVedioRecordViewModel?
        get() = OAVedioRecordViewModel()
    override val getView: OAVedioRecordView
        get() = this

    override fun init() {
        presenter?.requestCode()
        mRecVideoPath = File(
            Environment.getDataDirectory()
                .absolutePath + "/CameraDemo/video/ErrorVideo/"
        )
        if (!mRecVideoPath?.exists()!!) {
            mRecVideoPath?.mkdirs()
            LogUtils.e("创建文件夹")
        }

        cameraSurfaceHolder = camera_mysurfaceview?.holder
        cameraSurfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                cameraSurfaceHolder = holder
                 getPremission()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                cameraSurfaceHolder = holder
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                releaseCamera()
            }
        })

        btn_record.setOnClickListener(this)

    }

    private fun releaseCamera() {
        try {
            camera?.setPreviewCallback(null)
            camera?.stopPreview()
            camera?.lock()
            camera?.release()
            camera = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initView() {
        // 初始化摄像头  ，设置为前置相机
        try {
            camera = if (cameraType == 1) Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT) else {
                Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
            }
            val parameters = camera?.parameters
            parameters?.previewFrameRate = 30
            camera?.parameters = parameters
            camera?.setDisplayOrientation(90)
            camera?.addCallbackBuffer(mPreBuffer)
            camera?.setPreviewDisplay(cameraSurfaceHolder)
            camera?.startPreview()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_record -> {
                btn_record.isClickable = false
                startVedio()
            }
        }
    }

    private fun startVedio() {
        /*
         *此处摄像头的准备工作为mediaRecorder的前置操作，开启录像
         */
        mProgress = 0
        if (isRecording) {
            if (isPreview) {
                camera?.stopPreview()
                camera?.release()
                camera = null
            }
            if (null == mediaRecorder) {
                mediaRecorder = MediaRecorder()
            } else {
                mediaRecorder?.reset()
            }

            //判断类型开启前后置相机
            if (cameraType == 1) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            } else {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
            }
            camera?.lock()
            val parameters =  camera?.getParameters()
            if (cameraType == 0)
                parameters?.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera?.setDisplayOrientation(90)
            camera?.enableShutterSound(false)
            parameters?.previewFrameRate = 25
            camera?.setParameters(parameters)
            camera?.unlock()
            mediaRecorder?.setCamera(camera)
            mediaRecorder?.setOrientationHint(270)
            mediaRecorder?.setPreviewDisplay(cameraSurfaceHolder?.surface)
            mediaRecorder?.setVideoSource(MediaRecorder.VideoSource.CAMERA)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            mediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            mediaRecorder?.setVideoSize(640, 480)
            mediaRecorder?.setVideoEncodingBitRate(1200000)

            Log.e("ttttttttttttt", "mediaRecorder set sucess")

            try {
                mRecAudioFile = File.createTempFile("Vedio", ".mp4", mRecVideoPath)
                Log.e("ttttttttttttt", "11111111")
            } catch (e: IOException) {
                e.printStackTrace()
            }

            Log.e("ttttttttttttt", "..." + mRecAudioFile?.absolutePath)
            mediaRecorder?.setOutputFile(mRecAudioFile?.getAbsolutePath())
            try {
                mediaRecorder?.prepare()
                mediaRecorder?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isRecording = !isRecording
            Log.e("ttttttttttttt", "=====开始录制视频=====")
        }

        //在app build 中compile一下，是一个第三方的库
        Observable.interval(
            1000,
            TimeUnit.MILLISECONDS,
            AndroidSchedulers.mainThread()
        ).take(6).subscribe(object : Observer<Long> {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable.addAll(d)
               object : Thread() {
                    override fun run() {
                        while (mProgress < tv_change.max) {
                              changeTextColor()
                            try {
                                sleep(100)
                                mProgress+=8
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }

                        }
                    }
                }.start()
            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(t: Long) {

            }

            override fun onComplete() {
                val parameters = camera?.parameters
                parameters?.flashMode = Camera.Parameters.FLASH_MODE_OFF
                camera?.setParameters(parameters)
                /*
                  * 录像的关闭和资源释放
                  */
                mediaRecorder?.stop()
                mediaRecorder?.reset()
                mediaRecorder?.release()
                mediaRecorder = null
                FormatUtil.videoRename(mRecAudioFile)
               var str=FileToBase64Util.fileBase64String(mRecAudioFile?.path)
                presenter?.uploadVedio("")
                Log.e("ttttttttt", "=====录制完成，已保存=====")
                btn_record.isClickable = true
                isRecording = !isRecording
            }
        })
    }

    fun changeTextColor(){
        tv_change.setProgress(mProgress)
    }

    companion object {
        fun newInstance(): OAVedioRecordFragment {
            return OAVedioRecordFragment()
        }
    }
    override fun onAllPermissionOk(allPermissions: Array<out Permission>?) {
        initView()
    }

    override fun onPermissionDenied(refusedPermissions: Array<out Permission>?) {

    }
   fun getPremission(){
       SoulPermission.getInstance().checkAndRequestPermissions( Permissions.build(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),this)
   }

    override fun requestCode() {
      presenter?.requestVedioVerifyCode(OpenInfoManager.getInstance()?.info?.id)
    }

}



