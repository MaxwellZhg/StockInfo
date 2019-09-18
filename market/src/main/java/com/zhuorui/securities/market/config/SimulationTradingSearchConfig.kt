package com.zhuorui.securities.market.config

import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockMarketInfo

/**
 *    desc  :模拟炒股搜索纪录
 */
class SimulationTradingSearchConfig : AbsConfig() {

    private var stocks: MutableList<SearchStockInfo> = ArrayList()

    fun getStocks(): MutableList<SearchStockInfo> {
        return ArrayList(stocks)
    }

    /**
     * 检查列表中是否存在
     */
    private fun getStock(ts: String, code: String): SearchStockInfo? {
        for (stock in stocks) {
            if (stock.code.equals(code) && stock.ts.equals(ts)) {
                return stock
            }
        }
        return null
    }

    /**
     * 检查列表中是否存在
     */
    fun isExist(ts: String, code: String): Boolean {
        return getStock(ts, code) != null
    }

    /**
     * 添加
     */
    fun replaceAll(list: MutableList<SearchStockInfo>): Boolean {
        if (list.isNullOrEmpty()) {
            return false
        }
        stocks.clear()
        stocks.addAll(list)
        write()
        return true
    }

    /**
     * 添加
     */
    fun add(stockInfo: SearchStockInfo): Boolean {
        val data = getStock(stockInfo.ts!!, stockInfo.code!!)
        if (data != null) {
            stocks.remove(data)
        }
        stocks.add(0, stockInfo)
        write()
        return true
    }

    /**
     * 删除
     */
    fun remove(code: String, ts: String): Boolean {
        val stock = getStock(ts, code)
        if (stock != null) {
            stocks.remove(stock)
            write()
            return true
        }
        return false
    }

    /**
     * 清空
     */
    fun clear() {
        stocks.clear()
        write()
    }


    override fun write() {
        StorageInfra.put(SimulationTradingSearchConfig::class.java.simpleName, this)
    }

    companion object {
        fun read(): SimulationTradingSearchConfig {
            var config: SimulationTradingSearchConfig? =
                StorageInfra.get(
                    SimulationTradingSearchConfig::class.java.simpleName,
                    SimulationTradingSearchConfig::class.java
                )
            if (config == null) {
                config = SimulationTradingSearchConfig()
                config.write()
            }
            return config
        }
    }
}