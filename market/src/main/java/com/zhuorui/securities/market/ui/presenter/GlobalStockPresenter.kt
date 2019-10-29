package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.model.GlobalStockInfo
import com.zhuorui.securities.market.ui.adapter.GlobalStockInfoAdapter
import com.zhuorui.securities.market.ui.adapter.GlobalStockInfoTipsAdapter
import com.zhuorui.securities.market.ui.view.GlobalStockView
import com.zhuorui.securities.market.ui.viewmodel.GlobalStockViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/29
 * Desc:
 */
class GlobalStockPresenter :AbsNetPresenter<GlobalStockView,GlobalStockViewModel>(){
    var coustomList = ArrayList<Int>()
    var usaList= ArrayList<Int>()
    var enuList= ArrayList<Int>()
    var asiaList= ArrayList<Int>()
    var dataList = ArrayList<GlobalStockInfo>()
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infoList?.observe(it,
                androidx.lifecycle.Observer<List<GlobalStockInfo>> { t ->
                    view?.addIntoData(t)
                })
        }
    }

    fun getGlobalInfoAdapter(): GlobalStockInfoAdapter {
       return  GlobalStockInfoAdapter(context)
    }

    fun getData(){
        usaList.clear()
        enuList.clear()
        asiaList.clear()
        coustomList.clear()
        for (i in 0..4) {
            usaList.add(i)
        }
        for (i in 0..6) {
            enuList.add(i)
        }
        for (i in 0..8) {
            asiaList.add(i)
        }
        for (i in 0..9) {
            coustomList.add(i)
        }
        dataList.add(GlobalStockInfo(1,coustomList))
        dataList.add(GlobalStockInfo(2,usaList))
        dataList.add(GlobalStockInfo(3,enuList))
        dataList.add(GlobalStockInfo(4,asiaList))
        viewModel?.infoList?.value=dataList
    }



}