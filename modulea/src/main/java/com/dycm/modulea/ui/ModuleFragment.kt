
package com.dycm.modulea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dycm.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.dycm.modulea.contract.MainContract
import com.dycm.modulea.R
import com.dycm.modulea.databinding.FragmentModuleaBinding
import com.dycm.modulea.model.PersonalInfo
import com.dycm.modulea.mvp.MainPresenter
import com.dycm.modulea.mvp.MainVmWarpper
import com.dycm.modulea.viewmodel.ModuleaViewModel
import kotlinx.android.synthetic.main.fragment_modulea.*


/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/12
 * Desc:
 */

class ModuleFragment : BaseMvpNetVmFragment<MainPresenter,MainVmWarpper,FragmentModuleaBinding>(),MainContract.View{
    override fun createPresenter(): MainPresenter {
         return  MainPresenter(this)
    }

    override fun isDestroyed(): Boolean {
         return false
    }

    override fun createWrapper(): MainVmWarpper {
       return MainVmWarpper(this)
    }
    private val viewModel by lazy {
        ModuleaViewModel()
    }

    override val layout: Int
        get() = R.layout.fragment_modulea

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
