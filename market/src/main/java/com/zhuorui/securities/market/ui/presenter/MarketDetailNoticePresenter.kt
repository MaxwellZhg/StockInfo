package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.model.TestNoticeData
import com.zhuorui.securities.market.ui.adapter.MarketNoticeInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
class MarketDetailNoticePresenter: AbsNetPresenter<MarketDetailNoticeView, MarketDetailNoticeViewModel>() {
    var listInfo :ArrayList<TestNoticeData> = ArrayList()
    var list :ArrayList<Int> = ArrayList()
    override fun init() {
        super.init()
    }
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infoList?.observe(it,
                androidx.lifecycle.Observer<List<TestNoticeData>> { t ->
                    view?.addIntoNoticeData(t)
                })
        }
    }

    fun getNoticeData(){
        listInfo.clear()
        list.clear()
        for(i in 0..9){
            list.add(i)
        }
        listInfo.add(TestNoticeData("今天",list))
        listInfo.add(TestNoticeData("昨天",list))
        listInfo.add(TestNoticeData("前天",list))
        viewModel?.infoList?.value = listInfo
    }

    fun getNoticeAdapter():MarketNoticeInfoAdapter{
        return MarketNoticeInfoAdapter()
    }

}