package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.base2app.util.AppUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaSignatureBinding
import com.zhuorui.securities.openaccount.ui.presenter.OASignaturePresenter
import com.zhuorui.securities.openaccount.ui.view.OASignatureView
import com.zhuorui.securities.openaccount.ui.viewmodel.OASignatureViewModel
import kotlinx.android.synthetic.main.fragment_oa_signature.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/28 11:24
 *    desc   : 电子签名页面
 */
class OASignatureFragment :
    AbsSwipeBackFragment<FragmentOaSignatureBinding, OASignatureViewModel, OASignatureView, OASignaturePresenter>(),
    OASignatureView {

    companion object {
        fun newInstance(): OASignatureFragment {
            return OASignatureFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_signature

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OASignaturePresenter
        get() = OASignaturePresenter()

    override val createViewModel: OASignatureViewModel?
        get() = ViewModelProviders.of(this).get(OASignatureViewModel::class.java)

    override val getView: OASignatureView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val layoutParams = content_view.layoutParams
        layoutParams.width = AppUtil.phoneScreenHeight - top_bar.height
        layoutParams.height = AppUtil.phoneScreenWidth
        content_view.requestLayout()
        content_view.rotation = 90f
        val translation = (layoutParams.width - layoutParams.height) / 2
        content_view.translationX = -translation - 1f
        content_view.translationY = translation.toFloat()
    }
}