package com.dycm.applib4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dycm.applib4.R
import com.dycm.applib4.ui.contract.OpenAccountTabContract
import com.dycm.applib4.ui.mvp.OpenAccountTabPresenter
import com.dycm.applib4.ui.mvp.OpenAccountTabVmWarpper
import com.dycm.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.dycm.applib4.databinding.OpenAccountFragmentBinding

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