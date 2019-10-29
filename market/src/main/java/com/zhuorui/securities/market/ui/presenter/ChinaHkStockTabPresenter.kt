package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.ui.adapter.ChinaHkStockAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.view.ChinaHkStockTabView
import com.zhuorui.securities.market.ui.viewmodel.ChinaHkStockTabViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/28
 * Desc:
 */
class ChinaHkStockTabPresenter :AbsNetPresenter<ChinaHkStockTabView,ChinaHkStockTabViewModel>(){
    var infoList = ArrayList<Int>()
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.listInfo?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoRvData(t)
                })
        }
    }

    fun setRvData(){
        infoList.clear()
        for (i in 0..4){
            infoList.add(i)
        }
        viewModel?.listInfo?.value=infoList
    }
    fun getChinaHkStockAdapter(): ChinaHkStockAdapter {
        return ChinaHkStockAdapter()
    }
}