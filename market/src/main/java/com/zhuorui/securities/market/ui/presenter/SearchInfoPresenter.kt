package com.zhuorui.securities.market.ui.presenter

import android.content.Context
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.SearchAllEvent
import com.zhuorui.securities.market.model.SearchDeafaultData
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
    var list =ArrayList<SearchDeafaultData>()
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
        var data=SearchDeafaultData(listhot,history)
        list.add(data)
        list.add(data)
       viewModel?.adapter?.value?.clearItems()
      if (viewModel?.adapter?.value?.items == null) {
            viewModel?.adapter?.value?.items = ArrayList()
        }
        viewModel?.adapter?.value?.addItems(list)
        LogUtils.e(viewModel?.adapter?.value?.items?.size.toString())
    }
    fun getAdapter(): SearchInfoAdapter? {
        if (viewModel?.adapter?.value == null) {
            viewModel?.adapter?.value = SearchInfoAdapter(context)
        }
        return viewModel?.adapter?.value
    }

    fun initViewPager(str:String){
        RxBus.getDefault().post(SearchAllEvent(str))
    }


}