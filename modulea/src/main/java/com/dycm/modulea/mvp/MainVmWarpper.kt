package com.dycm.modulea.mvp

import com.dycm.base2app.mvp.wrapper.BaseViewWrapper
import com.dycm.modulea.viewmodel.ModuleaViewModel
import com.dycm.modulea.contract.MainContract
import com.dycm.modulea.model.PersonalInfo
import com.dycm.modulea.databinding.FragmentModuleaBinding
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
class MainVmWarpper(view: MainContract.View) :BaseViewWrapper<MainContract.View,FragmentModuleaBinding>(),MainContract.ViewWrapper{
    private val viewModel by lazy {
        ModuleaViewModel()
    }
    init {
        attachView(view)
    }
    override fun setData(infos: List<PersonalInfo>) {
        LogUtils.e(infos.size.toString())
       viewModel.setInfo(infos[0])
    }

    override fun onBind() {
        dataBinding.viewmodel=viewModel
        dataBinding.executePendingBindings()
    }


}
