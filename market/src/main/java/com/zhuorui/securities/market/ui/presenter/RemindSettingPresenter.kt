package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.SearchDeafaultData
import com.zhuorui.securities.market.model.SettingNoticeData
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.ui.adapter.SearchInfoAdapter
import com.zhuorui.securities.market.ui.adapter.SettingNoticeAdapter
import com.zhuorui.securities.market.ui.view.RemindSettingView
import com.zhuorui.securities.market.ui.viewmodel.RemindSettingViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:28
 *    desc   :
 */
class RemindSettingPresenter : AbsEventPresenter<RemindSettingView, RemindSettingViewModel>() {
    var listnotice:ArrayList<SettingNoticeData> = ArrayList<SettingNoticeData>()
    fun initData(stockInfo: StockMarketInfo?){
        listnotice.add(SettingNoticeData(R.mipmap.ic_rise_arrow,ResUtil.getString(R.string.rise_threshold),"",false,false,stockInfo))
        listnotice.add(SettingNoticeData(R.mipmap.ic_down_arrow,ResUtil.getString(R.string.down_threshold),"",false,false,stockInfo))
        listnotice.add(SettingNoticeData(R.mipmap.ic_rise_range,ResUtil.getString(R.string.rise_range),"",false,false,stockInfo))
        listnotice.add(SettingNoticeData(R.mipmap.ic_down_range,ResUtil.getString(R.string.down_range),"",false,false,stockInfo))
        viewModel?.adapter?.value?.clearItems()
        if (viewModel?.adapter?.value?.items == null) {
            viewModel?.adapter?.value?.items = ArrayList()
        }
        viewModel?.adapter?.value?.addItems(listnotice)
        LogUtils.e(viewModel?.adapter?.value?.items?.size.toString())
    }
    fun getAdapter(): SettingNoticeAdapter? {
        if (viewModel?.adapter?.value == null) {
            viewModel?.adapter?.value = SettingNoticeAdapter()
        }
        return viewModel?.adapter?.value
    }
}