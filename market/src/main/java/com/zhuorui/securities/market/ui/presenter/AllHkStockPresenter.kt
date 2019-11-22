package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.ui.adapter.AllHkStockContainerAdapter
import com.zhuorui.securities.market.ui.adapter.AllHkStockNameAdapter
import com.zhuorui.securities.market.ui.view.AllHkStockView
import com.zhuorui.securities.market.ui.viewmodel.AllHkStockViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/24
 * Desc:
 */
class AllHkStockPresenter :AbsNetPresenter<AllHkStockView,AllHkStockViewModel>() {
    var nameList = ArrayList<Int>()
    var containerList = ArrayList<Int>()
    override fun init() {
        super.init()
    }
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.namelist?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoAllHkStockName(t)
                })
        }
        lifecycleOwner.let {
            viewModel?.containerlist?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoAllHkContainer(t)
                })
        }
    }
    fun getAllHkStockNameAdapter():AllHkStockNameAdapter{
        return AllHkStockNameAdapter()
    }
    fun getAllHkStockContainerAdapter():AllHkStockContainerAdapter{
        return AllHkStockContainerAdapter()
    }
    fun getAllHkStockNameData(){
        nameList.clear()
        containerList.clear()
        for (i in 0..19){
            nameList.add(i)
            containerList.add(i)
        }
        viewModel?.namelist?.value = nameList
        viewModel?.containerlist?.value=containerList
    }
    fun getAllHkStockContentData(){
        containerList.clear()
        for (i in 0..19){
            containerList.add(i)
        }
        viewModel?.containerlist?.value=containerList
    }
}