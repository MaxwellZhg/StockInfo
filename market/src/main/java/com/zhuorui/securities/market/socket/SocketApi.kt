package com.zhuorui.securities.market.socket

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/22 15:20
 * desc   : 定义常量
 */
object SocketApi {

    /**
     * 定义socket中常量
     */
    const val SOCKET_URL = "ws://192.168.1.213:1949" // 服务器地址
    const val SOCKET_AUTH_SIGNATURE = "069de7990c0c4b8d87f516b7478e9f4a" // 链接认证签名

    const val AUTH = "auth.auth" // 链接认证
    const val TOPIC_BIND = "topic.reBind" // 订阅
    const val TOPIC_UNBIND = "topic.unBind" // 取消订阅
    const val TOPIC_UNBIND_ALL = "topic.unBindAll" // 取消所有订阅

    const val PUSH_STOCK_DATA = "push.stock.data" //  推送股票数据

    const val PUSH_STOCK_INFO = "push.stock.info" //  推送股票行情
    const val PUSH_STOCK_KLINE = "push.stock.kline" // 推送股票K线

    const val GET_KLINE_FIVE_DAY = "kline.getFiveDay" // 获取五日K
    const val GET_KLINE_GET_DAILY = "kline.getDaily" // 获取日K
    const val GET_KLINE_MINUTE = "kline.getMinute" // 获取分时

    const val PUSH_STOCK_KLINE_COMPENSATION_DATA = "push.stock.compensationData.kline" // 推送股票K线补偿数据
    const val PUSH_STOCK_TRANS = "push.stock.trans" // 推送股票盘口

    const val GET_STOCK_PRICE = "stock.price.getStockPrice"// 查询股票价格数据
    const val PUSH_STOCK_PRICE = "push.stock.last" // 推送股票价格

    const val GET_STOCK_TRADE = "trade.getToday"// 查询逐笔成交数据
    const val PUSH_STOCK_TRADE = "push.stock.trade" // 推送逐笔成交数据

    const val GET_STOCK_TRADESTA = "trade.getStaToday"// 查询逐笔成交统计数据
    const val PUSH_STOCK_TRADESTA = "push.stock.tradesta" // 推送逐笔成交统计数据

    const val GET_STOCK_ORDER = "order.getToday" // 获取最新委托挂单数据(买卖盘)
    const val PUSH_STOCK_ORDER = "push.stock.order" // 推送委托挂单数据(买卖盘)

    const val GET_STOCK_ORDER_BROKER = "orderbroker.getToday" // 查询最新买卖经纪数据(买卖盘席位)
    const val PUSH_STOCK_ORDERBROKER = "push.stock.orderbroker" // 推送买卖经纪席位数据(买卖盘席位)

    const val GET_STOCK_HANDICAP = "handicap.getStockHandicap" // 查询股票盘口数据
    const val PUSH_STOCK_HANDICAP = "push.stock.stockhandicap" // 推送股票盘口数据

    const val GET_CAPITAL = "capital.getCapital" //查询资金统计数据
    const val PUSH_STOCK_CAPITAL = "push.stock.capital" //服务器推送资金统计数据

}
