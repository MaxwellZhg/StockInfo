package com.zhuorui.securities.openaccount.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentTakePhotoBinding
import com.zhuorui.securities.openaccount.ui.presenter.TakePhotoPresenter
import com.zhuorui.securities.openaccount.ui.view.TakePhotoView
import com.zhuorui.securities.openaccount.ui.viewmodel.TakePhotoViewModel
import kotlinx.android.synthetic.main.fragment_take_photo.*
import me.yokeyword.fragmentation.ISupportFragment

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/9/2 17:47
 * desc   : 拍摄身份证、银行卡页面
 */
class TakePhotoFragment :
    AbsFragment<FragmentTakePhotoBinding, TakePhotoViewModel, TakePhotoView, TakePhotoPresenter>(),
    TakePhotoView, View.OnClickListener {

    private var onLazyInited = false
    private var onStop = false

    companion object {
        // 身份证
        const val ID_CRAD = 1
        // 银行卡
        const val BANK_CRAD = 2

        fun newInstance(photoType: Int): TakePhotoFragment {
            val bundle = Bundle()
            bundle.putInt("photoType", photoType)
            val fragment = TakePhotoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_take_photo

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: TakePhotoPresenter
        get() = TakePhotoPresenter()

    override val createViewModel: TakePhotoViewModel?
        get() = ViewModelProviders.of(this).get(TakePhotoViewModel::class.java)

    override val getView: TakePhotoView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        iv_light.setOnClickListener(this)
        iv_camera.setOnClickListener(this)
        iv_camera_close.setOnClickListener(this)
        iv_camera_ok.setOnClickListener(this)
        tv_back.setOnClickListener(this)

        when (arguments?.get("photoType")) {
            ID_CRAD -> {
                presenter?.setTakePhotoTips(R.string.take_id_card_photo_tips)
            }
            BANK_CRAD -> {
                presenter?.setTakePhotoTips(R.string.take_bank_card_photo_tips)
            }
        }

        camera_view.init(false)

        onLazyInited = true
    }

    override fun onResume() {
        super.onResume()
        if (onLazyInited && onStop) {
            onStop = false
            camera_view.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        camera_view.onPause()
    }

    override fun onStop() {
        super.onStop()
        onStop = true
    }

    override fun onClick(p0: View?) {
        when (p0) {
            iv_light -> {
                // 开关闪光灯
                iv_light.setImageResource(if (camera_view.switchFlash()) R.mipmap.ic_light_open else R.mipmap.ic_light_close)
            }
            iv_camera -> {
                // 拍摄照片
                camera_view.takePhoto {
                    // 拍摄完成
                    presenter?.takePhoto(true)
                    val bundle = Bundle()
                    bundle.putParcelable(Bitmap::javaClass.name, it)
                    setFragmentResult(ISupportFragment.RESULT_OK, bundle)
                }
            }
            iv_camera_close -> {
                // 退出拍摄
                setFragmentResult(ISupportFragment.RESULT_CANCELED, null)
                pop()
            }
            iv_camera_ok -> {
                // 勾选拍摄的照片
                pop()
            }
            tv_back -> {
                // 取消拍摄的照片
                presenter?.takePhoto(false)
                camera_view.resetCamera()
                setFragmentResult(ISupportFragment.RESULT_CANCELED, null)
            }
        }
    }
}