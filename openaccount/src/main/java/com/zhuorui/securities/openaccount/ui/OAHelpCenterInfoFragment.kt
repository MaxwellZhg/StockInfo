package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaHelpcenterInfoBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAHelpCenterInfoPresenter
import com.zhuorui.securities.openaccount.ui.view.OAHelpCenterInfoView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAHelpCenterInfoViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/5
 * Desc:
 */
class OAHelpCenterInfoFragment :AbsSwipeBackFragment<FragmentOaHelpcenterInfoBinding,OAHelpCenterInfoViewModel,OAHelpCenterInfoView,OAHelpCenterInfoPresenter>(),OAHelpCenterInfoView{
    override val layout: Int
        get() = R.layout.fragment_oa_helpcenter_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAHelpCenterInfoPresenter
        get() = OAHelpCenterInfoPresenter()
    override val createViewModel: OAHelpCenterInfoViewModel?
        get() = ViewModelProviders.of(this).get(OAHelpCenterInfoViewModel::class.java)
    override val getView: OAHelpCenterInfoView
        get() = this
    companion object {
        fun newInstance(): OAHelpCenterInfoFragment {
            return OAHelpCenterInfoFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
    }

}