package com.zhuorui.securities.market.ui.presenter

import androidx.lifecycle.LifecycleOwner
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.ui.adapter.MarketNoticeInfoTipsAdapter
import com.zhuorui.securities.market.ui.view.MarketDetailNoticeView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailNoticeViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:56
 *    desc   :
 */
class MarketDetailNoticePresenter: AbsNetPresenter<MarketDetailNoticeView, MarketDetailNoticeViewModel>() {
    var list :ArrayList<Int> = ArrayList()
    override fun init() {
        super.init()
    }
    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        // 监听datas的变化
        lifecycleOwner.let {
            viewModel?.infoList?.observe(it,
                androidx.lifecycle.Observer<List<Int>> { t ->
                    view?.addIntoNoticeData(t)
                })
        }
    }

    fun getNoticeData(){
        list.clear()
        for(i in 0..29){
            list.add(i)
        }
        viewModel?.infoList?.value = list
    }

    fun getNoticeAdapter(): MarketNoticeInfoTipsAdapter {
        return MarketNoticeInfoTipsAdapter()
    }

}