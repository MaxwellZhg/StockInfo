package com.zhuorui.securities.market.config

import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.infra.StorageInfra
import com.zhuorui.securities.market.model.SearchStockInfo

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/14
 * Desc: 本地搜索股票信息记录缓存
 */
class LocalSearchConfig : AbsConfig() {

    private var serachStocks: ArrayList<String> = ArrayList()


    @Synchronized
    fun getStocks(): ArrayList<String> {
        return serachStocks
    }
    /**
     * 添加搜索记录
     */
    @Synchronized
    fun add(str: String): Boolean {
        if (isExist(str)) {
            LogInfra.Log.d(TAG, "add " + str + " failed. Current cache zise " + serachStocks.size)
            return false
        }
        if(serachStocks.size<10) {
            serachStocks.add(str)
        }else{
            serachStocks.add(0,str)
            serachStocks.removeAt(10)
        }
        LogInfra.Log.d(TAG, "add " + str + " succeeded. Current cache zise " + serachStocks.size)
        write()
        return true
    }


    /**
     * 检查自选股列表中是否存在
     */
    fun isExist(ts: String): Boolean {
        return getStock(ts) != null
    }

    /**
     * 检查搜索股列表中是对比
     */
    private fun getStock(ts: String): String? {
        for (stock in serachStocks) {
            if (stock == ts) {
                return stock
            }
        }
        return null
    }


    /**
     * 删除自选股
     */
    @Synchronized
    fun remove(ts: String): Boolean {
        val stock = getStock(ts)
        if (stock != null) {
            serachStocks.remove(stock)
            LogInfra.Log.d(TAG, "remove " + stock + " succeeded. Current cache zise " + serachStocks.size)
            write()
            return true
        }
        return false
    }

    override fun write() {
        StorageInfra.put(LocalSearchConfig::class.java.simpleName, this)
    }

    @Synchronized
    fun clear() {
        serachStocks.clear()
        StorageInfra.remove(LocalSearchConfig::class.java.simpleName, LocalSearchConfig::class.java.name)
    }



    companion object {

        private val TAG = "LocaLSearchConfig"

        private var instance: LocalSearchConfig? = null

        fun getInstance(): LocalSearchConfig {
            if (instance == null) {
                // 使用同步锁
                synchronized(LocalSearchConfig::class.java) {
                    if (instance == null) {
                        instance = read()
                    }
                }
            }
            return instance!!
        }

        private fun read(): LocalSearchConfig {
            var config: LocalSearchConfig? =
                StorageInfra.get(LocalSearchConfig::class.java.simpleName, LocalSearchConfig::class.java)
            if (config == null) {
                config = LocalSearchConfig()
                config.write()
            }
            return config
        }

        fun hasCache(): Boolean {
            return StorageInfra.get(LocalSearchConfig::class.java.simpleName, LocalSearchConfig::class.java) != null
        }
    }
}
