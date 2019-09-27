package com.zhuorui.commonwidget.config

import android.graphics.Color
import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/11 14:52
 *    desc   : 保存本地设置信息
 */
class LocalSettingsConfig : AbsConfig() {
    val sorckColorRed = Color.parseColor("#FFce0019")
    val sorckColorGreen = Color.parseColor("#FF23803A")
    val sorckColor = Color.parseColor("#FFA4B2CB")

    // 默认为红涨绿跌
    var stocksThemeColor: StocksThemeColor = StocksThemeColor.redUpGreenDown
        set(value) {
            field = value
            write()
        }

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

    fun getUpDownColor(number: BigDecimal): Int {
        return getUpDownColor(number, sorckColor)
    }

    fun getUpDownColor(number: Double): Int {
        return getUpDownColor(number, sorckColor)
    }

    fun getUpDownColor(number: Double, defColor: Int): Int {
        return getUpDownColor(BigDecimal(number), defColor)
    }

    fun getUpDownColor(number: BigDecimal, defColor: Int): Int {
        return when (number.compareTo(BigDecimal(0))) {
            1 -> getUpColor()
            -1 -> getDownColor()
            else -> defColor
        }
    }


    // 默认为自动语言
    var appLanguage: AppLanguage = AppLanguage.auto
        set(value) {
            field = value
            write()
        }

    override fun write() {
        StorageInfra.put(LocalSettingsConfig::class.java.simpleName, this)
    }

    fun saveStockColor(enum: StocksThemeColor) {
        stocksThemeColor = enum
        write()
    }

    fun saveLanguage(enum: AppLanguage) {
        appLanguage = enum
        write()
    }

    override fun toString(): String {
        return "LocalSettingsConfig(stocksThemeColor=$stocksThemeColor, appLanguage=$appLanguage)"
    }

    companion object {


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