package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.view.HkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.HkStockDetailViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:
 */
class HkStockDetailPresenter :AbsNetPresenter<HkStockDetailView,HkStockDetailViewModel>(){
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
        for (i in 0..4) {
            history.add(i)
        }
        viewModel?.infos?.value = history
    }

    fun getMarketInfoAdapter(): MarketPartInfoAdapter {
          return MarketPartInfoAdapter()
    }
}