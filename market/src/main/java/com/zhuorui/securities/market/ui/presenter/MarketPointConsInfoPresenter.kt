package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketPointConsInfoView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointConsInfoViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketPointConsInfoPresenter :AbsNetPresenter<MarketPointConsInfoView, MarketPointConsInfoViewModel>(){
    var info =ArrayList<Int>()
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.pointInfos?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addPointInfoAdapter(t)
                })
        }
    }

    fun getInfoData(){
        info.clear()
        for (i in 0..19) {
            info.add(i)
        }
        viewModel?.pointInfos?.value = info
    }

    fun getMarketPointInfoAdapter(): MarketPointInfoAdapter {
        return  MarketPointInfoAdapter()
    }
}