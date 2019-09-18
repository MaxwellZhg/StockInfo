package com.zhuorui.securities.personal.config

import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra
import com.zhuorui.securities.personal.model.AppLanguage
import com.zhuorui.securities.personal.model.StocksThemeColor

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/11 14:52
 *    desc   : 保存本地设置信息
 */
class LocalSettingsConfig : AbsConfig() {

    // 默认为红涨绿跌
    var stocksThemeColor: StocksThemeColor = StocksThemeColor.redUpGreenDown
       set(value) {
            field = value
            write()
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
    fun saveStockColor(enum:StocksThemeColor){
        stocksThemeColor=enum
        write()
    }

    fun saveLanguage(enum: AppLanguage){
        appLanguage=enum
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