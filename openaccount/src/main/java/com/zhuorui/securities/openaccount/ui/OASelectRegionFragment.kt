package com.zhuorui.securities.openaccount.ui

import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaSelectRegionBinding
import com.zhuorui.securities.openaccount.ui.presenter.OASelectRegionPresenter
import com.zhuorui.securities.openaccount.ui.view.OASeletRegionView
import com.zhuorui.securities.openaccount.ui.viewmodel.OASelectRegonViewModel
import kotlinx.android.synthetic.main.fragment_oa_select_region.*


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:01
 *    desc   : 提示用户准备开户资料
 */

class OASelectRegionFragment :
    AbsSwipeBackNetFragment<FragmentOaSelectRegionBinding, OASelectRegonViewModel, OASeletRegionView, OASelectRegionPresenter>(),
    OASeletRegionView,View.OnClickListener {

    override fun init() {
        next.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.next -> {
                start(OADataTipsFragment.newInstance())
            }
        }
    }

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
        get() = ViewModelProviders.of(this).get(OASelectRegonViewModel::class.java)

    override val getView: OASeletRegionView
        get() = this


}
