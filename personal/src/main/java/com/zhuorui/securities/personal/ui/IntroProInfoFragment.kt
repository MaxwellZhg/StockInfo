package com.zhuorui.securities.personal.ui

import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentIntroProInfoBinding
import com.zhuorui.securities.personal.ui.presenter.IntroProInfoPresenter
import com.zhuorui.securities.personal.ui.view.IntroProInfoView
import com.zhuorui.securities.personal.ui.viewmodel.IntroProInfoViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/12
 * Desc:产品介绍详情
 * */
class IntroProInfoFragment :AbsSwipeBackNetFragment<FragmentIntroProInfoBinding,IntroProInfoViewModel,IntroProInfoView,IntroProInfoPresenter>(),IntroProInfoView{
    override val layout: Int
        get() = R.layout.fragment_intro_pro_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: IntroProInfoPresenter
        get() = IntroProInfoPresenter()
    override val createViewModel: IntroProInfoViewModel?
        get() = ViewModelProviders.of(this).get(IntroProInfoViewModel::class.java)
    override val getView: IntroProInfoView
        get() = this
    companion object {
        fun newInstance(): IntroProInfoFragment {
            return IntroProInfoFragment()
        }
    }

}