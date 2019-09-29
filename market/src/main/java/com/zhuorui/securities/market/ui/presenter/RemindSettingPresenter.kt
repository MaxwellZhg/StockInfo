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
    override fun init() {
        super.init()
    }
  fun deatilSave(upprice:String,downprice:String,uprate:String,downrate:String,stockinfo:StockMarketInfo?){

  }
}