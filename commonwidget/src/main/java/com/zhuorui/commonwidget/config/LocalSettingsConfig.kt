package com.zhuorui.commonwidget.config

import android.graphics.Color
import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra
import java.util.ArrayList

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/11 14:52
 *    desc   : 保存本地设置信息
 */
class LocalSettingsConfig private constructor(): AbsConfig(), Subject<Observer> {

    // 存储订阅者
    private val observerList = ArrayList<Observer>()

    private val sorckColorRed = Color.parseColor("#FFD9001B")
    private val sorckColorGreen = Color.parseColor("#FF00CC00")
    private val sorckColor = Color.parseColor("#FF7B889E")
    private val stockbtnRedColor = Color.parseColor("#FFAC3E19")
    private val stockbtnGreenColor = Color.parseColor("#FF37672E")


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
        write()
    }

    override fun registerObserver(obs: Observer) {
        observerList.add(obs)
    }

    override fun removeObserver(obs: Observer) {
        observerList.remove(obs)
    }

    override fun notifyAllObservers() {
        for (obs in observerList) {
            // 更新每一个观察者中的信息
            obs.update(this)
        }
    }

    override fun toString(): String {
        return "LocalSettingsConfig(stocksThemeColor=$stocksThemeColor, appLanguage=$appLanguage)"
    }

    /**
     * 根据价格获取涨跌颜色
     * @param price 当前价格
     * @param oldPrice 开盘价，或用作对比价格
     */
    fun getUpDownColor(price: Float, oldPrice: Float): Int {
        return getUpDownColor(price, oldPrice, getDefaultColor())
    }

    /**
     * 根据价格获取涨跌颜色
     * @param price 当前价格
     * @param oldPrice 开盘价，或用作对比价格
     * @param defColor 平价颜色
     */
    fun getUpDownColor(price: Float, oldPrice: Float, defColor: Int): Int {
        return if (price > oldPrice) {
            getUpColor()
        } else if (price < oldPrice) {
            getDownColor()
        } else {
            defColor
        }
    }

    /**
     * 根据价格获取涨跌颜色
     * @param price 当前价格
     * @param oldPrice 开盘价，或用作对比价格
     * @param defColor 平价颜色
     */
    fun getUpDownColor(price: Double, oldPrice: Double, defColor: Int): Int {
        return if (price > oldPrice) {
            getUpColor()
        } else if (price < oldPrice) {
            getDownColor()
        } else {
            defColor
        }
    }


    companion object {


        private var instance: LocalSettingsConfig? = null

        fun getInstance(): LocalSettingsConfig {//使用同步锁
            if (instance == null) {
                synchronized(LocalSettingsConfig::class.java) {
                    if (instance == null) {
                        instance = read()
                    }
                }
            }
            return instance!!
        }

        private fun read(): LocalSettingsConfig {
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
            instance = null
        }
    }

}