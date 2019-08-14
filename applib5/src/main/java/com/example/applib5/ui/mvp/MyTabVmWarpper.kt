package com.example.applib5.ui.mvp

import com.dycm.base2app.mvp.wrapper.BaseViewWrapper
import com.example.applib5.databinding.FragmentMyBinding
import com.example.applib5.ui.contract.MyTabContract
import com.example.applib5.ui.viewmodel.MyTabViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class MyTabVmWarpper(view: MyTabContract.View) : BaseViewWrapper<MyTabContract.View, FragmentMyBinding>(),MyTabContract.ViewWrapper{
    private val viewModel by lazy {
        MyTabViewModel()
    }
    override fun setData() {

    }

    override fun onBind() {
        dataBinding.viewmodel=viewModel
        dataBinding.executePendingBindings()
    }

}