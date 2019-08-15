package com.zhuorui.securities.applib5.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuorui.securities.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.zhuorui.securities.applib5.R
import com.zhuorui.securities.applib5.databinding.FragmentMyBinding
import com.zhuorui.securities.applib5.ui.contract.MyTabContract
import com.zhuorui.securities.applib5.ui.mvp.MyTabPresenter
import com.zhuorui.securities.applib5.ui.mvp.MyTabVmWarpper


/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/6
 * Desc:
 */
class MyTabFragment:BaseMvpNetVmFragment<MyTabPresenter,MyTabVmWarpper,FragmentMyBinding>(),MyTabContract.View{
    override fun createPresenter(): MyTabPresenter {
       return MyTabPresenter(this)
    }

    override fun isDestroyed(): Boolean {
       return false
    }

    override fun createWrapper(): MyTabVmWarpper {
      return MyTabVmWarpper(this)
    }

    override val layout: Int
        get() = R.layout.fragment_my

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