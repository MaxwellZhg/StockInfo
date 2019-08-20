package com.zhuorui.securities.market.config

import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.base2app.infra.StorageInfra

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/30 19:04
 *    desc   : 本地添加的自选股纪录
 */
class LocalStocksConfig : AbsConfig() {

    private var stocks: ArrayList<StockTopic> = ArrayList()

    fun getStocks(): ArrayList<StockTopic> {
        return stocks
    }

    /**
     * 添加自选股
     */
    fun add(stockInfo: StockTopic): Boolean {
        for (stock in stocks) {
            if (stock.code == stockInfo.code && stock.ts == stockInfo.ts) {
                return false
            }
        }
        stocks.add(stockInfo)
        write()
        return true
    }

    /**
     * 删除自选股
     */
    fun remove(code: String?, ts: String?): Boolean {
        for (stock in stocks) {
            if (stock.code == code && stock.ts == ts) {
                stocks.remove(stock)
                write()
                return true
            }
        }
        return false
    }

    override fun write() {
        StorageInfra.put(LocalStocksConfig::class.java.simpleName, this)
    }

    companion object {
        fun read(): LocalStocksConfig {
            var config: LocalStocksConfig? =
                StorageInfra.get(LocalStocksConfig::class.java.simpleName, LocalStocksConfig::class.java)
            if (config == null) {
                config = LocalStocksConfig()
                config.write()
            }
            return config
        }
    }
}