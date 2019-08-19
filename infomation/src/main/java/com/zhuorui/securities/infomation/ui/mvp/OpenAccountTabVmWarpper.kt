package com.zhuorui.securities.infomation.ui.mvp

import com.zhuorui.securities.infomation.databinding.OpenAccountFragmentBinding
import com.zhuorui.securities.infomation.ui.contract.OpenAccountTabContract
import com.zhuorui.securities.infomation.ui.viewmodel.OpenAccountViewModel
import com.zhuorui.securities.base2app.mvp.wrapper.BaseViewWrapper

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class OpenAccountTabVmWarpper(view:OpenAccountTabContract.View): BaseViewWrapper<OpenAccountTabContract.View, OpenAccountFragmentBinding>(),OpenAccountTabContract.ViewWrapper {
    private val viewModel by lazy {
        OpenAccountViewModel()
    }

    override fun setData() {

    }

    override fun onBind() {
        dataBinding.viewmodel = viewModel
        dataBinding.executePendingBindings()
    }
}