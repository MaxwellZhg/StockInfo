package com.zhuorui.securities.market.manager

import com.zhuorui.securities.market.model.STFundAccountData
import java.math.BigDecimal

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-26 10:33
 *    desc   :
 */
open class STInfoManager {
    private var info: STFundAccountData? = null

    companion object {
        private var instance: STInfoManager? = null

        fun getInstance(): STInfoManager {//使用同步锁
            if (instance == null) {
                synchronized(STInfoManager::class.java) {
                    if (instance == null) {
                        instance = STInfoManager()
                    }
                }
            }
            return instance!!
        }
    }

    fun getSTFundAccountData(): STFundAccountData {
        return if (info == null) {
            STFundAccountData("", BigDecimal(0))
        } else {
            info!!
        }
    }

    fun setSTFundAccountData(data: STFundAccountData) {
        info = data
    }

    fun destroy() {
        instance = null
    }

}