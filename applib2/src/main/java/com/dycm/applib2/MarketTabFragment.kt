package com.dycm.applib2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.dycm.applib2.contract.MarketTabContract
import com.dycm.applib2.databinding.FargmentTestsubBinding
import com.dycm.applib2.mvp.MarketTabPresenter
import com.dycm.applib2.mvp.MarketTabVmWarpper
import com.dycm.base2app.mvp.wrapper.BaseMvpSwipeVmFragment
import com.dycm.base2app.ui.fragment.AbsSwipeBackFragment
import kotlinx.android.synthetic.main.fargment_testsub.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/18 10:40
 *    desc   :
 */
@Route(path = AppLib2Constants.FRAGMENT_URL_TESTSUB)
class MarketTabFragment : BaseMvpSwipeVmFragment<MarketTabPresenter,MarketTabVmWarpper,FargmentTestsubBinding>(),MarketTabContract.View {
    override fun createPresenter(): MarketTabPresenter {
        return MarketTabPresenter(this)
    }

    override fun isDestroyed(): Boolean {
        return false
    }

    override fun createWrapper(): MarketTabVmWarpper {
        return MarketTabVmWarpper(this)
    }

    var params: String ?= null

    override val layout: Int
        get() = R.layout.fargment_testsub

    @SuppressLint("SetTextI18n")
    override fun init() {
        params = arguments?.getString("params")
        textviwe.text = textviwe.text.toString() + params
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