package com.dycm.applib4.ui.mvp

import com.dycm.applib4.databinding.OpenAccountFragmentBinding
import com.dycm.applib4.ui.contract.OpenAccountTabContract
import com.dycm.applib4.ui.viewmodel.OpenAccountViewModel
import com.dycm.base2app.mvp.wrapper.BaseViewWrapper

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