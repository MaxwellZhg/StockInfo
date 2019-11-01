package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.event.ChageSearchTabEvent
import com.zhuorui.securities.market.event.MarketDetailInfoEvent
import com.zhuorui.securities.market.ui.adapter.MarketInfoAdapter
import com.zhuorui.securities.market.ui.view.MarketDetailCapitalView
import com.zhuorui.securities.market.ui.view.MarketDetailInformationView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailCapitalViewModel
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailInformationViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
class MarketDetailInformationPresenter: AbsNetPresenter<MarketDetailInformationView, MarketDetailInformationViewModel>() {
    var listInfo:ArrayList<Int> = ArrayList()
    override fun init() {
        super.init()
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infoList?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoInfoData(t)
                })
        }
    }
    fun getInfoData(){
        for(i in 0..9){
            listInfo.add(i)
        }
        viewModel?.infoList?.value = listInfo
    }

    fun getInfoAdapter(): MarketInfoAdapter {
        return MarketInfoAdapter()
    }
    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onChangeInfoTypeEvent(event: MarketDetailInfoEvent) {
        view?.changeInfoTypeData(event)
    }


}