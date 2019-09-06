package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaVediorecordBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAVedioRecordPresenter
import com.zhuorui.securities.openaccount.ui.view.OAVedioRecordView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAVedioRecordViewModel
import kotlinx.android.synthetic.main.fragment_oa_vediorecord.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/28
 * Desc: 活体检测录制视频页面
 */
class OAVedioRecordFragment :
    AbsSwipeBackNetFragment<FragmentOaVediorecordBinding, OAVedioRecordViewModel, OAVedioRecordView, OAVedioRecordPresenter>(),
    OAVedioRecordView, View.OnClickListener {

    private var progressDialog: ProgressDialog? = null
    private var onLazyInited = false
    private var onStop = false

    companion object {
        fun newInstance(): OAVedioRecordFragment {
            return OAVedioRecordFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_vediorecord

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAVedioRecordPresenter
        get() = OAVedioRecordPresenter()

    override val createViewModel: OAVedioRecordViewModel?
        get() = ViewModelProviders.of(this).get(OAVedioRecordViewModel::class.java)

    override val getView: OAVedioRecordView
        get() = this


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter?.setVerifyCode()
        btn_record.setOnClickListener(this)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        camera_view.init(true)
        onLazyInited = true
        progressDialog = context?.let { ProgressDialog(it) }
        progressDialog?.setCanceledOnTouchOutside(false)
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
        when (p0?.id) {
            R.id.btn_record -> {
                btn_record.isEnabled = false
                btn_record.alpha = 0.5f
                // 调用录制视频
                camera_view.recordVedio(
                    3000
                ) { data ->
                    if (data != null && data.isNotEmpty()) {
                        // 拿到视频流，进行上传
                        presenter?.uploadVedio(data)
                    }
                }
                // 播放数字码进度
                tv_change.start(3000)
            }
        }
    }

    override fun showUploading() {
        progressDialog?.show()
    }

    override fun hideUploading() {
        progressDialog?.dismiss()
    }

    override fun setProgressText(msg: String?) {
        progressDialog?.setMessage(msg)
    }

    override fun uploadComplete(isSuccessful: Boolean) {
        btn_record.isEnabled = true
        btn_record.alpha = 1.0f
        if (isSuccessful) {
            start(OATakeBankCradPhotoFragment.newInstance())
        } else {
            // 恢复画面
            camera_view.resetCamera()
            // 清除上一次的颜色效果
            tv_change.clear()
        }
    }
}