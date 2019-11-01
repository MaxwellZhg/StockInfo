package com.zhuorui.securities.market.socket.vo

import com.zhuorui.securities.market.model.BaseStockMarket
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/10/30 15:19
 *    desc   : 股票成交明细
 */
class StockTradeDetailData(
    val name: String,// 股票名称
    val diffPreMark: Int,// 0-等于昨收   1-高出昨收  -1-低于昨收
    val tradeMode: Int,// 0-中性盘  1-主动买入  2-主动卖出
    val time: String,// 成交时间 格式为：yyyyMMddHHmmssSSS
    val price: BigDecimal,// 成交价格
    val qty: BigDecimal,// 成交量
    val cancelflag: String, // N-未撤单  Y-已撤单
    val recordId: String// 记录唯一id 32位字符串
) : BaseStockMarket()