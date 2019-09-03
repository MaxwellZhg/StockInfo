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
        camera_view.init(false)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        btn_record.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        camera_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        camera_view.onPause()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_record -> {
                // 调用录制视频
                camera_view.recordVedio(
                    6000
                ) { data ->
                    // 拿到视频流，进行上传
                    presenter?.uploadVedio(data)
                }
                // 先清除上一次的颜色效果
                tv_change.clear()
                // 播放数字码进度
                tv_change.start(6000)
            }
        }
    }

    override fun showUploading() {
        context?.let {
            progressDialog = ProgressDialog(it)
            progressDialog?.setCanceledOnTouchOutside(false)
            progressDialog?.show()
        }
    }

    override fun hideUploading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    /**
     * 上传完成，调到下一步
     */
    override fun uploadComplete() {
        start(OATakeBankCradPhotoFragment.newInstance())
    }
}