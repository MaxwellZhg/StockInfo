package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsBackFinishFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.custom.ShareInfoPopupWindow
import com.zhuorui.securities.openaccount.databinding.FragmentOpenAccountBinding
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.ui.presenter.OpenAccountTabPresenter
import com.zhuorui.securities.openaccount.ui.view.OpenAccountTabView
import com.zhuorui.securities.openaccount.ui.viewmodel.OpenAccountTabViewModel
import com.zhuorui.securities.personal.ui.LoginRegisterFragment
import com.zhuorui.securities.personal.ui.MessageFragment
import com.zhuorui.securities.personal.ui.OAHelpCenterFragment
import kotlinx.android.synthetic.main.fragment_oa_select_region.*
import kotlinx.android.synthetic.main.fragment_oa_select_region.top_bar
import kotlinx.android.synthetic.main.fragment_open_account.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/6
 * Desc:
 */
open class OpenAccountTabFragment :
    AbsBackFinishFragment<FragmentOpenAccountBinding, OpenAccountTabViewModel, OpenAccountTabView, OpenAccountTabPresenter>(),
    OpenAccountTabView, View.OnClickListener {
    override val layout: Int
        get() = R.layout.fragment_open_account

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OpenAccountTabPresenter
        get() = OpenAccountTabPresenter()

    override val createViewModel: OpenAccountTabViewModel?
        get() = ViewModelProviders.of(this).get(OpenAccountTabViewModel::class.java)

    override val getView: OpenAccountTabView
        get() = this


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        onJumpToOpenAccountPage()
        tv_btn_open.setOnClickListener(this)
        val location = IntArray(2)
        top_bar?.getLocationOnScreen(location)
        top_bar.setRightClickListener {
            // 显示更多操作
            context?.let {
                ShareInfoPopupWindow.create(it, object : ShareInfoPopupWindow.CallBack {
                    override fun onInfoMation() {
                        (parentFragment as AbsFragment<*, *, *, *>).start(MessageFragment.newInstance())
                    }

                    override fun onHelpCenter() {
                        (parentFragment as AbsFragment<*, *, *, *>).start(OAHelpCenterFragment.newInstance())
                    }
                }).showAsDropDown(top_bar, location[0], location[1])
            }
        }
    }

    override fun onJumpToOpenAccountPage() {
        when (presenter?.getLoginStatus()) {
            false -> {
                (parentFragment as AbsFragment<*, *, *, *>).start(LoginRegisterFragment.newInstance(1))
            }
        }
    }

    override fun init() {

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
               R.id.tv_btn_open->{
                   when (presenter?.getLoginStatus()) {
                       false -> {
                           (parentFragment as AbsFragment<*, *, *, *>).start(LoginRegisterFragment.newInstance(1))
                       }
                       true -> {
                           val f = OpenInfoManager.getInstance()?.getStartFragment()
                           if (f != null)
                               (parentFragment as AbsFragment<*, *, *, *>).start(f)
                       }
                   }
               }
        }
    }

    companion object {
        fun newInstance(): OpenAccountTabFragment {
            return OpenAccountTabFragment()
        }
    }
}