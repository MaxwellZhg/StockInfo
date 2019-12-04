package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.custom.ShareInfoPopupWindow
import com.zhuorui.securities.openaccount.databinding.FragmentOaDataSupplementaryTipsBinding
import com.zhuorui.securities.openaccount.databinding.FragmentOaSelectRegionBinding
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.ui.presenter.OADataSupplementaryTispPresenter
import com.zhuorui.securities.openaccount.ui.presenter.OASelectRegionPresenter
import com.zhuorui.securities.openaccount.ui.view.OADataSupplementaryTispView
import com.zhuorui.securities.openaccount.ui.view.OASeletRegionView
import com.zhuorui.securities.openaccount.ui.viewmodel.OADataSupplementaryTispViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OASelectRegonViewModel
import com.zhuorui.securities.personal.ui.MessageFragment
import com.zhuorui.securities.personal.ui.OAHelpCenterFragment
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener
import kotlinx.android.synthetic.main.fragment_oa_data_supplementary_tips.*
import kotlinx.android.synthetic.main.fragment_oa_select_region.*
import me.yokeyword.fragmentation.ISupportFragment

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:01
 *    desc   : 选择开户用户地区
 */

class OADataSupplementaryTispFragment :
    AbsSwipeBackNetFragment<FragmentOaDataSupplementaryTipsBinding, OADataSupplementaryTispViewModel, OADataSupplementaryTispView, OADataSupplementaryTispPresenter>(),
    OADataSupplementaryTispView, View.OnClickListener {

    companion object {
        fun newInstance(): OADataSupplementaryTispFragment {
            return OADataSupplementaryTispFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_data_supplementary_tips

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OADataSupplementaryTispPresenter
        get() = OADataSupplementaryTispPresenter()

    override val createViewModel: OADataSupplementaryTispViewModel?
        get() = ViewModelProviders.of(this).get(OADataSupplementaryTispViewModel::class.java)

    override val getView: OADataSupplementaryTispView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        btn_next.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_next -> {
                val f = OpenInfoManager.getInstance().getNextFragment()
                if (f != null) {
                    startWithPop(f)
                }
            }
        }
    }
}
