package com.zhuorui.commonwidget.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import butterknife.BindView
import com.zhuorui.commonwidget.R
import com.zhuorui.commonwidget.R2
import com.zhuorui.commonwidget.R2.id.tv_album
import com.zhuorui.securities.base2app.dialog.BaseBottomSheetsDialog
import com.zhuorui.securities.base2app.util.GetPhotoFromAlbumUtil
import com.zhuorui.securities.pickerview.date.DatePicker
import me.jessyan.autosize.utils.LogUtils
import me.yokeyword.fragmentation.ISupportFragment
import java.io.File

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-27 09:57
 *    desc   :
 */
class GetPicturesModeDialog(context: Context) : BaseBottomSheetsDialog(context),
    View.OnClickListener {

    var listener: OnGetPicturesModeListener? = null

    val TO_ALBUM_REQUEST_CODE = 1201
    val TO_CAMERA_REQUEST_CODE = 1101

    @BindView(R2.id.tv_album)
    lateinit var tv_album: TextView
    @BindView(R2.id.tv_shot)
    lateinit var tv_shot: TextView
    @BindView(R2.id.tv_cancel)
    lateinit var cancel: TextView

    override val layout: Int
        get() = R.layout.dialog_get_pictures_mode

    init {
        tv_album.setOnClickListener(this)
        tv_shot.setOnClickListener(this)
        cancel.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_shot -> {
                hide()
                val uri: Uri? = GetPhotoFromAlbumUtil.getOutputMediaFileUri(listener?.getCameraSavePath(), p0.context)
                listener?.goCamera(listener?.getToCameraRequestCode(), uri)
            }
            R.id.tv_album -> {
                hide()
                listener?.goAlbum(listener?.getToAlbumRequestCode())
            }
            R.id.tv_cancel -> {
                hide()
            }
        }
    }

    //活动请求的回调，用requestCode来匹配
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            listener?.getToCameraRequestCode() -> {
                var camerPath: String? = listener?.getCameraSavePath()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    listener?.onPicturePath(camerPath)
                } else {
                    listener?.onPicturePath(Uri.fromFile(File(camerPath)).encodedPath)
                }
            }
            listener?.getToAlbumRequestCode() -> {
                var path: String? = GetPhotoFromAlbumUtil.getRealPathFromUri(context, data?.data)
                listener?.onPicturePath(path)
            }
        }
    }

    fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        if (resultCode != ISupportFragment.RESULT_OK) return
        when (requestCode) {
            listener?.getToCameraRequestCode() -> {
                val bm = data?.getParcelable<Bitmap>(Bitmap::javaClass.name)
                listener?.onPictureBitmap(bm)
            }
        }
    }

    interface OnGetPicturesModeListener {


        /**
         * 获取相册图片RequestCode
         */
        fun getToAlbumRequestCode(): Int {
            return 1201
        }

        /**
         * 获取拍照RequestCode
         */
        fun getToCameraRequestCode(): Int {
            return 1101
        }

        /**
         * 获取拍照保存地址
         * 调用APP拍照可不传地址
         */
        fun getCameraSavePath(): String {
            return ""
        }

        /**
         * 返回图片地址（调用dialog的回调方法处理，此方法才会有结果返回）
         */
        fun onPicturePath(path: String?)

        /**
         * 返回图片bitmap 调用dialog的回调方法处理，此方法才会有结果返回）
         */
        fun onPictureBitmap(bm: Bitmap?)

        /**
         * 去拍照
         */
        fun goCamera(toCameraRequestCode: Int?, uri: Uri?)

        /**
         * 去相册
         */
        fun goAlbum(toAlbumRequestCode: Int?)


    }

}

