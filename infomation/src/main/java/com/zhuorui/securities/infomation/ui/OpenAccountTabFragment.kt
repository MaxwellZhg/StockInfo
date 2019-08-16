package com.zhuorui.securities.infomation.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.ui.contract.OpenAccountTabContract
import com.zhuorui.securities.infomation.ui.mvp.OpenAccountTabPresenter
import com.zhuorui.securities.infomation.ui.mvp.OpenAccountTabVmWarpper
import com.zhuorui.securities.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.infomation.databinding.OpenAccountFragmentBinding

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/6
 * Desc:
 */
class OpenAccountTabFragment :BaseMvpNetVmFragment<OpenAccountTabPresenter,OpenAccountTabVmWarpper,OpenAccountFragmentBinding>(),OpenAccountTabContract.View{
    override fun createPresenter(): OpenAccountTabPresenter {
        return OpenAccountTabPresenter(this)
    }

    override fun isDestroyed(): Boolean {
        return false
    }

    override fun createWrapper(): OpenAccountTabVmWarpper {
       return OpenAccountTabVmWarpper(this)
    }

    override val layout: Int
        get() = R.layout.open_account_fragment
    override fun init() {
        (parentFragment as AbsFragment).start(LoginRegisterFragment())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = generateDataBinding(inflater,container,layout)
        if (viewWrapper != null) {
            viewWrapper.setBinding(dataBinding)
        }
        presenter.fetchData()
        return dataBinding.root
    }

}