package com.zhuorui.securities.market.ui.presenter

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.ChageSearchTabEvent
import com.zhuorui.securities.market.event.NotifyStockCountEvent
import com.zhuorui.securities.market.event.SelectsSearchTabEvent
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.TestSeachDefaultData
import com.zhuorui.securities.market.ui.adapter.SearchInfoAdapter
import com.zhuorui.securities.market.ui.view.SearchInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchInfoViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/19
 * Desc:
 */
class SearchInfoPresenter(context: Context) : AbsNetPresenter<SearchInfoView,SearchInfoViewModel>(){
    var list =ArrayList<TestSeachDefaultData>()
    var listhot =ArrayList<Int>()
    var history =ArrayList<Int>()
    override fun init() {
        super.init()
    }
    fun getData(){
        for(i in 0..5){
            listhot.add(i)
        }
        for(i in 0..3){
            history.add(i)
        }
        var data=TestSeachDefaultData(listhot,history)
        list.add(data)
        list.add(data)
        viewModel?.searchInfoDatas?.value=list
    }
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.searchInfoDatas?.observe(it,
                androidx.lifecycle.Observer<List<TestSeachDefaultData>> { t ->
                    view?.notifyDataSetChanged(t)
                })
        }
    }
    fun getAdapter(): SearchInfoAdapter? {
        return SearchInfoAdapter(context)
    }

    fun initViewPager(str:String,enum:SearchStokcInfoEnum?){
        RxBus.getDefault().post(SelectsSearchTabEvent(str,enum))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSearchChangMoreEvent(event: ChageSearchTabEvent) {
        view?.changeTab(event)
    }



}