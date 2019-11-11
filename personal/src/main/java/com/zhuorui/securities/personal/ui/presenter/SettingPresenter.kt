package com.zhuorui.securities.personal.ui.presenter

import com.zhuorui.commonwidget.config.AppLanguage
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.commonwidget.config.StocksThemeColor
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.adapter.SettingDataAdapter
import com.zhuorui.securities.personal.ui.model.SettingData
import com.zhuorui.securities.personal.ui.view.SettingView
import com.zhuorui.securities.personal.ui.viewmodel.SettingViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:
 */
class SettingPresenter : AbsNetPresenter<SettingView, SettingViewModel>() {
    var listinfo = ArrayList<SettingData>()
    override fun init() {
        super.init()
    }

    fun getData(type: Int, tips: String?) {
        listinfo.clear()
        when (type) {
            1 -> {
                listinfo.add(SettingData(ResUtil.getString(R.string.red_up_green_down), false))
                listinfo.add(SettingData(ResUtil.getString(R.string.green_up_red_down), false))
            }
            2 -> {
                listinfo.add(SettingData(ResUtil.getString(R.string.auto), false))
                listinfo.add(SettingData(ResUtil.getString(R.string.simple_cn), false))
                listinfo.add(SettingData(ResUtil.getString(R.string.unsimple_cn), false))
                listinfo.add(SettingData(ResUtil.getString(R.string.english), false))
            }
        }
        for (info in listinfo) {
            info.choose = false
            if (info.title == tips) {
                info.choose = true
            }
        }
        viewModel?.adapter?.value?.clearItems()
        if (viewModel?.adapter?.value?.items == null) {
            viewModel?.adapter?.value?.items = ArrayList()
        }
        viewModel?.adapter?.value?.addItems(listinfo)
    }


    fun getAdapter(): SettingDataAdapter? {
        if (viewModel?.adapter?.value == null) {
            viewModel?.adapter?.value = SettingDataAdapter()
        }
        return viewModel?.adapter?.value
    }

    fun detailSaveState(type:Int ,str:String?){
        when(type){
            1->{
               if(str.equals(ResUtil.getString(R.string.red_up_green_down))){
                   LocalSettingsConfig.getInstance().saveStockColor(StocksThemeColor.redUpGreenDown)
               }else{
                   LocalSettingsConfig.getInstance().saveStockColor(StocksThemeColor.greenUpRedDown)
               }
            }
            2->{
             when(str){
                   ResUtil.getString(R.string.auto)->{
                       LocalSettingsConfig.getInstance().saveLanguage(AppLanguage.auto)
                   }
                   ResUtil.getString(R.string.simple_cn)->{
                       LocalSettingsConfig.getInstance().saveLanguage(AppLanguage.zh_CN)
                   }
                   ResUtil.getString(R.string.unsimple_cn)->{
                       LocalSettingsConfig.getInstance().saveLanguage(AppLanguage.zh_HK)
                   }
                   ResUtil.getString(R.string.english)->{
                       LocalSettingsConfig.getInstance().saveLanguage(AppLanguage.en_US)
                   }
               }
            }
        }
    }

}