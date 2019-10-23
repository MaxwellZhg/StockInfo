package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:
 */
class MarketPointPresenter :AbsNetPresenter<MarketPointView,MarketPointViewModel>(){
    var history = ArrayList<Int>()
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infos?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addInfoToAdapter(t)
                })
        }
    }
    fun getData(){
        history.clear()
        for (i in 0..19) {
            history.add(i)
        }
        viewModel?.infos?.value = history
    }
    fun getMarketInfoAdapter(): MarketPartInfoAdapter {
        return MarketPartInfoAdapter(2)
    }

}