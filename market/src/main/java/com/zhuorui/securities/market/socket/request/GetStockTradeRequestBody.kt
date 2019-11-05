package com.zhuorui.securities.market.socket.request

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/5 16:47
 *    desc   : 查询逐笔成交数据
 */
class GetStockTradeRequestBody(val ts: String, val code: String)