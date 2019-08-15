package com.zhuorui.securities.applib1.ui.mvp

import com.zhuorui.securities.applib1.databinding.FragmentAllChooseStockBinding
import com.zhuorui.securities.applib1.ui.contract.TopicStockListContract
import com.zhuorui.securities.applib1.ui.viewmodel.TopicStockListViewModel
import com.zhuorui.securities.base2app.mvp.wrapper.BaseViewWrapper

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */

class TopicListStockVmWarpper(view: TopicStockListContract.View) :BaseViewWrapper<TopicStockListContract.View,FragmentAllChooseStockBinding>(),TopicStockListContract.ViewWrapper{
    private val viewModel by lazy {
        TopicStockListViewModel()
    }
    init {
        attachView(view)
    }
    override fun setData() {
        dataBinding.viewmodel = viewModel
        dataBinding.executePendingBindings()
    }

}