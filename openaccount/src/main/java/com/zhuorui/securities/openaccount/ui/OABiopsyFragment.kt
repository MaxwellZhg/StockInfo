package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaBiopayBinding
import com.zhuorui.securities.openaccount.ui.presenter.OABiopsyPresenter
import com.zhuorui.securities.openaccount.ui.view.OABiopsyView
import com.zhuorui.securities.openaccount.ui.viewmodel.OABiopsyViewModel
import kotlinx.android.synthetic.main.fragment_oa_biopay.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/26
 * Desc: 活体检测页面
 */
class OABiopsyFragment :
    AbsSwipeBackNetFragment<FragmentOaBiopayBinding, OABiopsyViewModel, OABiopsyView, OABiopsyPresenter>(),
    OABiopsyView, View.OnClickListener {

    override val layout: Int
        get() = R.layout.fragment_oa_biopay

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OABiopsyPresenter
        get() = OABiopsyPresenter()

    override val createViewModel: OABiopsyViewModel?
        get() = ViewModelProviders.of(this).get(OABiopsyViewModel::class.java)

    override val getView: OABiopsyView
        get() = this

    companion object {
        fun newInstance(): OABiopsyFragment {
            return OABiopsyFragment()
        }
    }

    override fun onClick(p0: View?) {
        val verifyCode = presenter?.getVerifyCode()
        if (TextUtils.isEmpty(verifyCode)) return
        start(OAVedioRecordFragment.newInstance())
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        btn_vedio.setOnClickListener(this)
    }
}