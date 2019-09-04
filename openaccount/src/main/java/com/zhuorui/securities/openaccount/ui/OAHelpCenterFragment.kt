package com.zhuorui.securities.openaccount.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.ui.presenter.OAHelpCenterPresenter
import com.zhuorui.securities.openaccount.ui.view.OAHelpCenterView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAHelpCenterViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/4
 * Desc:
 */
class OAHelpCenterFragment : AbsSwipeBackFragment<com.zhuorui.securities.openaccount.databinding.FragmentOaHelpcenterBinding,OAHelpCenterViewModel,OAHelpCenterView,OAHelpCenterPresenter>(),OAHelpCenterView{
    override val layout: Int
        get() = R.layout.fragment_oa_helpcenter
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAHelpCenterPresenter
        get() = OAHelpCenterPresenter()
    override val createViewModel: OAHelpCenterViewModel?
        get() =ViewModelProviders.of(this).get(OAHelpCenterViewModel::class.java)
    override val getView: OAHelpCenterView
        get() = this
    companion object {
        fun newInstance(): OAHelpCenterFragment {
            return OAHelpCenterFragment()
        }
    }
}