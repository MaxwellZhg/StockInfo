package com.zhuorui.securities.infomation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.BR
import com.zhuorui.securities.infomation.ui.contract.OpenAccountTabContract
import com.zhuorui.securities.infomation.ui.presenter.OpenAccountTabPresenter
import com.zhuorui.securities.base2app.ui.fragment.AbsBackFinishFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.infomation.databinding.OpenAccountFragmentBinding
import com.zhuorui.securities.infomation.ui.view.OpenAccountTabView
import com.zhuorui.securities.infomation.ui.viewmodel.OpenAccountTabViewModel
import com.zhuorui.securities.infomation.ui.viewmodel.SettingPswViewModel
import kotlinx.android.synthetic.main.open_account_fragment.*
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/6
 * Desc:
 */
class OpenAccountTabFragment :
    AbsBackFinishFragment<OpenAccountFragmentBinding,OpenAccountTabViewModel,OpenAccountTabView,OpenAccountTabPresenter>(),OpenAccountTabView,View.OnClickListener{
    override val layout: Int
        get() = R.layout.open_account_fragment

    override val viewModelId: Int
        get() = BR.viewmodel

    override val createPresenter: OpenAccountTabPresenter
        get() = OpenAccountTabPresenter()

    override val createViewModel: OpenAccountTabViewModel?
        get() = ViewModelProviders.of(this).get(OpenAccountTabViewModel::class.java)

    override val getView: OpenAccountTabView
        get() = this

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun init() {
        (parentFragment as AbsFragment<*, *, *, *>).start(LoginRegisterFragment.newInstance())
    }
    override fun onClick(p0: View?) {
      when(p0?.id){

        }
    }

    companion object {
        fun newInstance(): OpenAccountTabFragment {
            return OpenAccountTabFragment()

        }
    }

}