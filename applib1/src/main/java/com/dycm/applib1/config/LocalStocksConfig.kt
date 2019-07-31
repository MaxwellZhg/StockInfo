package com.dycm.applib1.config

import com.dycm.applib1.model.StockTopic
import com.dycm.base2app.infra.AbsConfig
import com.dycm.base2app.infra.StorageInfra

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

    companion object {
        fun read(): LocalStocksConfig {
            var config: LocalStocksConfig? = StorageInfra.get(LocalStocksConfig::class.java)
            if (config == null) {
                config = LocalStocksConfig()
                config.write()
            }
            return config
        }
    }
}