package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentOaHelpcenterBinding
import com.zhuorui.securities.personal.ui.presenter.OAHelpCenterPresenter
import com.zhuorui.securities.personal.ui.view.OAHelpCenterView
import com.zhuorui.securities.personal.ui.viewmodel.OAHelpCenterViewModel
import kotlinx.android.synthetic.main.fragment_oa_helpcenter.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/4
 * Desc: 帮助中心二级界面
 */
class OAHelpCenterFragment :
    AbsSwipeBackFragment<FragmentOaHelpcenterBinding, OAHelpCenterViewModel, OAHelpCenterView, OAHelpCenterPresenter>(),
    OAHelpCenterView, View.OnClickListener {

    override val layout: Int
        get() = R.layout.fragment_oa_helpcenter

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAHelpCenterPresenter
        get() = OAHelpCenterPresenter()

    override val createViewModel: OAHelpCenterViewModel?
        get() = ViewModelProviders.of(this).get(OAHelpCenterViewModel::class.java)

    override val getView: OAHelpCenterView
        get() = this

    companion object {
        fun newInstance(): OAHelpCenterFragment {
            return OAHelpCenterFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tv_openaccount_license.setOnClickListener(this)
        tv_openaccount_info.setOnClickListener(this)
        tv_openaccount_time.setOnClickListener(this)
        tv_openaccount_age.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_openaccount_license -> {
                start(OAHelpCenterInfoFragment.newInstance(1))
            }
            R.id.tv_openaccount_info -> {
                start(OAHelpCenterInfoFragment.newInstance(2))
            }
            R.id.tv_openaccount_time -> {
                start(OAHelpCenterInfoFragment.newInstance(3))
            }
            R.id.tv_openaccount_age -> {
                start(OAHelpCenterInfoFragment.newInstance(4))
            }
        }
    }
}