package com.zhuorui.securities.market.socket.request

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/5 16:47
 *    desc   : 根据ts/code查询股票数据
 */
class GetStockDataByTsCodeRequestBody(val ts: String, val code: String)