package com.dycm.applib3.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dycm.applib3.R
import com.dycm.applib3.databinding.FragmentInfoBinding
import com.dycm.applib3.ui.contract.InfomationTabContract
import com.dycm.applib3.ui.mvp.InfomationTabPresenter
import com.dycm.applib3.ui.mvp.InfomationTabWarpper
import com.dycm.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/6
 * Desc:
 */
class InfomationTabFragment : BaseMvpNetVmFragment<InfomationTabPresenter,InfomationTabWarpper, FragmentInfoBinding>(),InfomationTabContract.View {
    override fun createPresenter(): InfomationTabPresenter {
        return InfomationTabPresenter(this)
    }

    override fun isDestroyed(): Boolean {
       return  false
    }

    override fun createWrapper(): InfomationTabWarpper {
       return InfomationTabWarpper(this)
    }

    override val layout: Int
        get() = R.layout.fragment_info

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