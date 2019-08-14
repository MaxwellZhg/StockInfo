package com.dycm.modulea.mvp

import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.base2app.mvp.wrapper.BaseViewWrapper
import com.dycm.modulea.ModuleaViewModel
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
    init {
        attachView(view)
    }
    override fun setData(infos: List<PersonalInfo>) {
        LogUtils.e(infos.size.toString())
    }

    override fun onBind() {
        super.onBind()
    }

}
