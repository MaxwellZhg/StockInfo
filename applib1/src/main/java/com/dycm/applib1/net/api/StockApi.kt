package com.dycm.applib1.net.api

/**
 * 测试
 */
interface StockApi {
    companion object {
        /**
         * 自选股股票列表（含大盘指数）
         */
        const val LIST = "/as-market/api/stock/selected/v1/view/list"

        /**
         * 同步自选股
         */
        const val SYN = "/stockmarket/api/stock/selected/v1/view/syn"

        /**
         * 搜索股票
         */
        const val SEARCH = "as-market/api/stock/selected/v1/view/list"

        /**
         * 添加自选股
         */
        const val ADD = "/stockmarket/api/stock/selected/view/v1/add"

        /**
         * 删除自选股
         */
        const val DEL = "/stockmarket/api/stock/selected/v1/view/del"

        /**
         * 置顶自选股
         */
        const val TOP = "/stockmarket/api/stock/selected/v1/view/top"

        /**
         * 行情（即K线实时数据）
         */
        const val MARKET = "/stockmarket/api/stock/market/v1"
    }
}