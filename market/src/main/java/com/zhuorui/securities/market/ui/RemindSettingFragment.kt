package com.zhuorui.securities.market.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackEventFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentRemindSettingBinding
import com.zhuorui.securities.market.ui.presenter.RemindSettingPresenter
import com.zhuorui.securities.market.ui.view.RemindSettingView
import com.zhuorui.securities.market.ui.viewmodel.RemindSettingViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:22
 *    desc   :
 */
class RemindSettingFragment :
    AbsSwipeBackEventFragment<FragmentRemindSettingBinding, RemindSettingViewModel, RemindSettingView, RemindSettingPresenter>(),
    RemindSettingView {

    companion object {
        fun newInstance(): RemindSettingFragment {
            return RemindSettingFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_remind_setting

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: RemindSettingPresenter
        get() = RemindSettingPresenter()

    override val createViewModel: RemindSettingViewModel?
        get() = ViewModelProviders.of(this).get(RemindSettingViewModel::class.java)

    override val getView: RemindSettingView
        get() = this

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
}