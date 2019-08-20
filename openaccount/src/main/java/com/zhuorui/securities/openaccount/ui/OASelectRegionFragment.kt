package com.zhuorui.securities.openaccount.ui

import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaSelectRegionBinding
import com.zhuorui.securities.openaccount.ui.presenter.OASelectRegionPresenter
import com.zhuorui.securities.openaccount.ui.view.OASeletRegionView
import com.zhuorui.securities.openaccount.ui.viewmodel.OASelectRegonViewModel


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:01
 *    desc   :
 */

class OASelectRegionFragment :
    AbsSwipeBackNetFragment<FragmentOaSelectRegionBinding, OASelectRegonViewModel, OASeletRegionView, OASelectRegionPresenter>(),OASeletRegionView{

    companion object {
        fun newInstance(): OASelectRegionFragment {
            return OASelectRegionFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_select_region

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OASelectRegionPresenter
        get() = OASelectRegionPresenter()

    override val createViewModel: OASelectRegonViewModel?
        get() = OASelectRegonViewModel()

    override val getView: OASeletRegionView
        get() = this

}
