package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.SearchAllEvent
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.ui.adapter.SeachAllofInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter
import com.zhuorui.securities.market.ui.view.SearchResultInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchResultInfoViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
class SearchResultInfoPresenter(type: SearchStokcInfoEnum?) : AbsNetPresenter<SearchResultInfoView, SearchResultInfoViewModel>() {
    private var type =type
    var list = ArrayList<SearchDeafaultData>()
    var listhot = ArrayList<Int>()
    var history = ArrayList<Int>()
    override fun init() {
        super.init()
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchALLEvent(event: SearchAllEvent) {
            view?.detailInfo(event.str)
    }

    fun getData(type: SearchStokcInfoEnum?) {
        listhot.clear()
        history.clear()
        list.clear()
        for (i in 0..4) {
            listhot.add(i)
        }
        for (i in 0..4) {
            history.add(i)
        }
        var data = SearchDeafaultData(listhot, history)
        list.add(data)
        list.add(data)
        viewModel?.adapter?.value?.clearItems()
        if (viewModel?.adapter?.value?.items == null) {
            viewModel?.adapter?.value?.items = ArrayList()
        }
        viewModel?.adapter?.value?.addItems(list)
        LogUtils.e("tttttt" + viewModel?.adapter?.value?.items?.size.toString())
    }


    fun getAdapter(): SeachAllofInfoAdapter? {
        if (viewModel?.adapter?.value == null) {
            viewModel?.adapter?.value = SeachAllofInfoAdapter(context)
        }
        return viewModel?.adapter?.value
    }

    fun getStockAdapter(): StockAdapter? {
        if (viewModel?.stockadapter?.value == null) {
            viewModel?.stockadapter?.value = StockAdapter()
        }
        return viewModel?.stockadapter?.value
    }

    fun getStockInfoAdapter(): StockInfoAdapter? {
        if (viewModel?.infoadapter?.value == null) {
            viewModel?.infoadapter?.value = StockInfoAdapter()
        }
        return viewModel?.infoadapter?.value
    }

    fun getStockData(){
        listhot.clear()
        for (i in 0..4) {
            listhot.add(i)
        }
        viewModel?.stockadapter?.value?.clearItems()
        if (viewModel?.stockadapter?.value?.items == null) {
            viewModel?.stockadapter?.value?.items = ArrayList()
        }
        viewModel?.stockadapter?.value?.addItems(listhot)
        LogUtils.e("tttttt" + viewModel?.stockadapter?.value?.items?.size.toString())
    }
    fun getStockInfoData(){
        history.clear()
        for (i in 0..4) {
            history.add(i)
        }
        viewModel?.infoadapter?.value?.clearItems()
        if (viewModel?.infoadapter?.value?.items == null) {
            viewModel?.infoadapter?.value?.items = ArrayList()
        }
        viewModel?.infoadapter?.value?.addItems(history)
        LogUtils.e("tttttt" + viewModel?.infoadapter?.value?.items?.size.toString())
    }

}