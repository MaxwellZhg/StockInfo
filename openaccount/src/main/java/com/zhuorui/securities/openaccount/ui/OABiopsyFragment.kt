package com.zhuorui.securities.openaccount.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.ui.presenter.OABiopsyPresenter
import com.zhuorui.securities.openaccount.ui.view.OABiopsyView
import com.zhuorui.securities.openaccount.ui.viewmodel.OABiopsyViewModel
import com.zhuorui.securities.openaccount.databinding.FragmentOaBiopayBinding

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/26
 * Desc:
 */
class OABiopsyFragment :
    AbsSwipeBackNetFragment<FragmentOaBiopayBinding, OABiopsyViewModel, OABiopsyView, OABiopsyPresenter>(),
    OABiopsyView {

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

    override fun init() {

    }

    companion object {
        fun newInstance(): OABiopsyFragment {
            return OABiopsyFragment()
        }
    }
}