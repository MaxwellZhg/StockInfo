package com.zhuorui.securities.market.config

import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.infra.StorageInfra
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/14
 * Desc:
 */
class LoaclSearchConfig :AbsConfig(){

    private var serachStocks: ArrayList<SearchStockInfo> = ArrayList()

    /**
     * 添加搜索记录
     */
    @Synchronized
    fun add(stockInfo: SearchStockInfo): Boolean {
        if (isExist(stockInfo.ts!!, stockInfo.code!!)) {
            LogInfra.Log.d(TAG, "add " + stockInfo.name + " failed. Current cache zise " + serachStocks.size)
            return false
        }
        serachStocks.add(stockInfo)
        LogInfra.Log.d(TAG, "add " + stockInfo.name + " succeeded. Current cache zise " + serachStocks.size)
        write()
        return true
    }


    /**
     * 检查自选股列表中是否存在
     */
    fun isExist(ts: String, code: String): Boolean {
        return getStock(ts, code) != null
    }
    /**
     * 检查搜索股列表中是对比
     */
    private fun getStock(ts: String, code: String): SearchStockInfo? {
        for (stock in serachStocks) {
            if (stock.code.equals(code) && stock.ts.equals(ts)) {
                return stock
            }
        }
        return null
    }


    /**
     * 删除自选股
     */
    @Synchronized
    fun remove(ts: String, code: String): Boolean {
        val stock = getStock(ts, code)
        if (stock != null) {
            serachStocks.remove(stock)
            LogInfra.Log.d(TAG, "remove " + stock.name + " succeeded. Current cache zise " + serachStocks.size)
            write()
            return true
        }
        return false
    }

    override fun write() {
        StorageInfra.put(LoaclSearchConfig::class.java.simpleName, this)
    }


    companion object {

        private val TAG = "LoacLSearchConfig"

        private var instance: LoaclSearchConfig? = null

        fun getInstance(): LoaclSearchConfig {
            if (instance == null) {
                // 使用同步锁
                synchronized(LoaclSearchConfig::class.java) {
                    if (instance == null) {
                        instance = read()
                    }
                }
            }
            return instance!!
        }

        private fun read(): LoaclSearchConfig {
            var config: LoaclSearchConfig? =
                StorageInfra.get(LoaclSearchConfig::class.java.simpleName, LoaclSearchConfig::class.java)
            if (config == null) {
                config = LoaclSearchConfig()
                config.write()
            }
            return config
        }

        fun hasCache(): Boolean {
            return StorageInfra.get(LoaclSearchConfig::class.java.simpleName, LoaclSearchConfig::class.java) != null
        }
    }
}
