package com.zhuorui.commonwidget.common

import android.graphics.Color
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/8
 * Desc:国家码全局设置
 * */
class CountryCodeConfig :AbsConfig(){
    var defaultCode="86"

   fun setDefalutCode(str:String){
       defaultCode =defaultCode
        write()
    }
    companion object {
        fun read(): CountryCodeConfig {
            var config: CountryCodeConfig? =
                StorageInfra.get(CountryCodeConfig::class.java.simpleName, CountryCodeConfig::class.java)
            if (config == null) {
                config = CountryCodeConfig()
                config.write()
            }
            return config
        }

        fun clear() {
            StorageInfra.remove(CountryCodeConfig::class.java.simpleName, CountryCodeConfig::class.java.name)
        }
    }
}