package com.zhuorui.securities.market.config

import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.infra.StorageInfra
import com.zhuorui.securities.market.model.StockMarketInfo

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/30 19:04
 *    desc   : 本地添加的自选股纪录
 */
class LocalStocksConfig : AbsConfig() {

    private var stocks: ArrayList<StockMarketInfo> = ArrayList()

    @Synchronized
    fun getStocks(vararg ts: String): ArrayList<StockMarketInfo> {
        return when {
            ts.isNullOrEmpty() -> ArrayList(stocks)
            else -> {
                val tempList = ArrayList<StockMarketInfo>()
                for (stock in stocks) {
                    if (ts.contains(stock.ts)) {
                        tempList.add(stock)
                    }
                }
                tempList
            }
        }
    }

    /**
     * 检查自选股列表中是否存在
     */
    private fun getStock(ts: String, code: String): StockMarketInfo? {
        for (stock in stocks) {
            if (stock.code.equals(code) && stock.ts.equals(ts)) {
                return stock
            }
        }
        return null
    }

    /**
     * 检查自选股列表中是否存在
     */
    fun isExist(ts: String, code: String): Boolean {
        return getStock(ts, code) != null
    }

    /**
     * 更新自选股，当不存在时会新增
     */
    @Synchronized
    fun update(list: MutableList<StockMarketInfo>): Boolean {
        if (list.isNullOrEmpty()) {
            return false
        }

        if (stocks.isEmpty()) {
            stocks.addAll(list)
        } else {
            // 拷贝数据
            val tempList = ArrayList<StockMarketInfo>(stocks)
//            Collections.copy(tempList, stocks)
            for (item in list) {
                var isExist = false
                for (stock in tempList) {
                    if (stock.ts.equals(item.ts) && stock.code.equals(item.code)) {
                        // 更新数据
                        StockMarketInfo.copyProperties(item, stock)
                        tempList.remove(stock)
                        isExist = true
                        break
                    }
                }
                if (!isExist) {
                    // 插入数据
                    stocks.add(item)
                }
            }
        }

        write()
        return true
    }

    /**
     * 添加自选股
     */
    @Synchronized
    fun add(stockInfo: StockMarketInfo): Boolean {
        if (isExist(stockInfo.ts!!, stockInfo.code!!)) {
            LogInfra.Log.d(TAG, "add " + stockInfo.name + " failed.")
            return false
        }
        stocks.add(stockInfo)
        write()
        LogInfra.Log.d(TAG, "add " + stockInfo.name + " succeeded.")
        return true
    }

    /**
     * 删除自选股
     */
    @Synchronized
    fun remove(ts: String, code: String): Boolean {
        val stock = getStock(ts, code)
        if (stock != null) {
            LogInfra.Log.d(TAG, "remove " + stock.name + " succeeded.")
            stocks.remove(stock)
            write()
            return true
        }
        return false
    }

    override fun write() {
        StorageInfra.put(LocalStocksConfig::class.java.simpleName, this)
    }

    companion object {

        private val TAG = "LocalStocksConfig"

        private var instance: LocalStocksConfig? = null

        fun getInstance(): LocalStocksConfig {
            if (instance == null) {
                // 使用同步锁
                synchronized(LocalStocksConfig::class.java) {
                    if (instance == null) {
                        instance = read()
                    }
                }
            }
            return instance!!
        }

        private fun read(): LocalStocksConfig {
            var config: LocalStocksConfig? =
                StorageInfra.get(LocalStocksConfig::class.java.simpleName, LocalStocksConfig::class.java)
            if (config == null) {
                config = LocalStocksConfig()
                config.write()
            }
            return config
        }

        fun clear() {
            StorageInfra.remove(LocalStocksConfig::class.java)
        }

        fun hasCache(): Boolean {
            return StorageInfra.get(LocalStocksConfig::class.java.simpleName, LocalStocksConfig::class.java) != null
        }
    }
}