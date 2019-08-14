package com.dycm.applib3.ui.mvp

import com.dycm.applib3.databinding.FragmentInfoBinding
import com.dycm.applib3.ui.contract.InfomationTabContract
import com.dycm.applib3.ui.viewmodel.InfomationViewModel
import com.dycm.base2app.mvp.wrapper.BaseViewWrapper

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class InfomationTabWarpper(view: InfomationTabContract.View) : BaseViewWrapper<InfomationTabContract.View, FragmentInfoBinding>(),InfomationTabContract.ViewWrapper {
    private val viewModel by lazy {
       InfomationViewModel()
    }

    init {
        attachView(view)
    }

    override fun setData() {

    }

    override fun onBind() {
        dataBinding.viewmodel = viewModel
        dataBinding.executePendingBindings()
    }
}
