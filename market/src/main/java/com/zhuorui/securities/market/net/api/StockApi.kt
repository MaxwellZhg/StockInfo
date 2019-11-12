package com.zhuorui.securities.market.net.api

/**
 * 股票行情
 */
interface StockApi {
    companion object {
        /**
         * 自选股股票列表,已登陆身份
         */
        const val MY_LIST = "as_market/api/stock/selected/v1/view/mylist"

        /**
         * 同步自选股
         */
        const val SYN = "as_market/api/stock/selected/v1/view/syn"

        /**
         * 搜索股票
         */
        const val SEARCH = "as_market/api/stock/selected/v1/view/list"

        /**
         * 添加自选股
         */
        const val ADD = "as_market/api/stock/selected/v1/view/add"

        /**
         * 删除自选股
         */
        const val DEL = "as_market/api/stock/selected/v1/view/del"

        /**
         * 置顶自选股
         */
        const val TOP = "as_market/api/stock/selected/v1/view/top"

        /**
         * 行情（即K线实时数据）
         */
        const val MARKET = "stockmarket/api/stock/market/v1"

        /**
         * 关键字搜索
         */
        const val SEARCH_TOPIC = "as_market/api/stock/view/v1/search"

        /**
         * 个股行情资讯
         */
        const val NEWS_LIST = "as_market/api/news_hk/v1/news/list"

        /**
         * 公告列表
         */
        const val BASE_INFO = "as_market/api/stock/iis_oss_record/v1/getBaseInfo"
        /**
         * 公告查询
         */
        const val GET_ALL_ATTACHMENT = "as_market/api/stock/iis_oss_record/v1/getAllAttachmentByLineId"

        /**
         * F10简况
         */
        const val F10_BRIE = "as_market/api/f10/v1/profile"

        /**
         * F10获取持股变动列表分页
         */
        const val SHARE_HOLDER_LIST ="as_market/api/f10/v1/share_holder/list"

        /**
         * F10获取分红派息列表
         */
        const val DIVIDENT_LIST = "as_market/api/f10/v1/divident/list"

        /**
         * F10获取回购列表分页
         */
        const val REPO_LIST = "as_market/api/f10/v1/repo/list"

        /**
         * F10财报信息
         */
      //  const val REPO_LIST = "as_market/api/f10/v1/repo/list"
    }
}