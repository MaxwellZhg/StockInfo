
package com.dycm.modulea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dycm.base2app.mvp.BaseMvpFinishNetFragment
import com.dycm.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.dycm.modulea.ModuleaViewModel
import com.dycm.modulea.contract.MainContract
import com.dycm.modulea.R
import com.dycm.modulea.databinding.FragmentModuleaBinding
import com.dycm.modulea.model.PersonalInfo
import com.dycm.modulea.mvp.MainPresenter
import com.dycm.modulea.mvp.MainVmWarpper
import kotlinx.android.synthetic.main.fragment_modulea.*
import me.jessyan.autosize.utils.LogUtils


/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/12
 * Desc:
 */

class ModuleFragment : BaseMvpNetVmFragment<MainPresenter,MainVmWarpper,FragmentModuleaBinding>(),MainContract.View{

    private  var viewModel: ModuleaViewModel? = null
    override fun showPersonalInfoDialog(info: PersonalInfo) {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun onEmpty(tag: Any?) {

    }

    override fun onError(tag: Any?, errorMsg: String?) {

    }

    override fun createPresenter(): MainPresenter {
         return  MainPresenter(this)
    }

    override fun isDestroyed(): Boolean {
         return false
    }

    override fun createWrapper(): MainVmWarpper {
       return MainVmWarpper(this)
    }

    override val layout: Int
        get() = R.layout.fragment_modulea

    override fun init() {
         btn.setOnClickListener {
            dataBinding.tvTest.text="222222"
         }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       dataBinding = generateDataBinding(inflater,container,layout)
        if (viewWrapper != null) {
            viewWrapper.setBinding(dataBinding)
        }
        viewModel= ModuleaViewModel()
        viewModel?.setIntroduction("1111111")
        dataBinding.viewmodel=viewModel
        dataBinding.executePendingBindings()
        //presenter.fetchData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}
