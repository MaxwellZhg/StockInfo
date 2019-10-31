package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
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
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.coustomList?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoCoustomData(t)
                })
            viewModel?.usaList?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoUsaData(t)
                })
            viewModel?.enuList?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoEnuData(t)
                })
            viewModel?.asiaList?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoAsiaData(t)
                })
        }
    }

    fun getGlobalInfoTipsAdapter(): GlobalStockInfoTipsAdapter {
       return  GlobalStockInfoTipsAdapter()
    }


    fun getData(){
        coustomList.clear()
        for (i in 0..9) {
            coustomList.add(i)
        }
        enuList.clear()
        for (i in 0..6) {
            enuList.add(i)
        }
        usaList.clear()
        for (i in 0..4) {
            usaList.add(i)
        }
        asiaList.clear()
        for (i in 0..8) {
            asiaList.add(i)
        }
        viewModel?.usaList?.value=usaList
        viewModel?.coustomList?.value=coustomList
        viewModel?.enuList?.value=enuList
        viewModel?.asiaList?.value=asiaList
    }
}