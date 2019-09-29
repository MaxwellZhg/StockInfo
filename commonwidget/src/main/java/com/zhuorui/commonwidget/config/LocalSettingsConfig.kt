package com.zhuorui.commonwidget.config

import android.graphics.Color
import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.commonwidget.model.Subject.list
import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/11 14:52
 *    desc   : 保存本地设置信息
 */
class LocalSettingsConfig : AbsConfig(), Subject<Observer> {

    // 默认为红涨绿跌
    var stocksThemeColor: StocksThemeColor = StocksThemeColor.redUpGreenDown

    fun getUpColor(): Int {
        return when (stocksThemeColor) {
            StocksThemeColor.redUpGreenDown -> sorckColorRed
            else -> sorckColorGreen
        }
    }

    fun getDownColor(): Int {
        return when (stocksThemeColor) {
            StocksThemeColor.redUpGreenDown -> sorckColorGreen
            else -> sorckColorRed
        }
    }

    fun getUpBtnColor(): Int {
        return when (stocksThemeColor) {
            StocksThemeColor.redUpGreenDown -> stockbtnRedColor
            else -> stockbtnGreenColor
        }
    }

    fun getDownBtnColor(): Int {
        return when (stocksThemeColor) {
            StocksThemeColor.redUpGreenDown -> stockbtnGreenColor
            else -> stockbtnRedColor
        }
    }

    fun getDefaultColor(): Int {
        return sorckColor
    }


    // 默认为自动语言
    var appLanguage: AppLanguage = AppLanguage.auto

    override fun write() {
        StorageInfra.put(LocalSettingsConfig::class.java.simpleName, this)
    }

    fun saveStockColor(enum: StocksThemeColor) {
        stocksThemeColor = enum
        notifyAllObservers()
        write()
    }

    fun saveLanguage(enum: AppLanguage) {
        appLanguage = enum
        notifyAllObservers()
        write()
    }

    override fun registerObserver(obs: Observer?) {
        list.add(obs)
    }

    override fun removeObserver(obs: Observer?) {
        list.remove(obs)
    }

    override fun notifyAllObservers() {
        for (obs in list) {
            // 更新每一个观察者中的信息
            obs.update(this)
        }
    }

    override fun toString(): String {
        return "LocalSettingsConfig(stocksThemeColor=$stocksThemeColor, appLanguage=$appLanguage)"
    }

    companion object {

        val sorckColorRed = Color.parseColor("#FFce0019")
        val sorckColorGreen = Color.parseColor("#FF23803A")
        val sorckColor = Color.parseColor("#FFA4B2CB")
        val stockbtnRedColor = Color.parseColor("#FFAC3E19")
        val stockbtnGreenColor = Color.parseColor("#FF37672E")

        fun read(): LocalSettingsConfig {
            var config: LocalSettingsConfig? =
                StorageInfra.get(LocalSettingsConfig::class.java.simpleName, LocalSettingsConfig::class.java)
            if (config == null) {
                config = LocalSettingsConfig()
                config.write()
            }
            return config
        }

        fun clear() {
            StorageInfra.remove(LocalSettingsConfig::class.java.simpleName, LocalSettingsConfig::class.java.name)
        }
    }

}