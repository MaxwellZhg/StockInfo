package com.zhuorui.securities.infomation.ui.config

import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra
import com.zhuorui.securities.infomation.net.response.UserLoginCodeResponse
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LocalLoginResConfig : AbsConfig() {

    private var loginRes: LinkedList<UserLoginCodeResponse> = LinkedList()

    fun getloginRes(): LinkedList<UserLoginCodeResponse> {
        return loginRes
    }

    /**
     * 添加自选股
     */
    fun add(loginResInfo: UserLoginCodeResponse): Boolean {
        loginRes.add(0,loginResInfo)
        write()
        return true
    }

    /**
     * 删除自选股
     */
    fun remove(): Boolean {
        return if(loginRes.size>0){
            loginRes.removeAt(0)
            true
        }else {
            false
        }
    }

    override fun write() {
        StorageInfra.put(LocalLoginResConfig::class.java.simpleName, this)
    }

    companion object {
        fun read(): LocalLoginResConfig {
            var config: LocalLoginResConfig? =
                StorageInfra.get(LocalLoginResConfig::class.java.simpleName, LocalLoginResConfig::class.java)
            if (config == null) {
                config = LocalLoginResConfig()
                config.write()
            }
            return config
        }
    }
}