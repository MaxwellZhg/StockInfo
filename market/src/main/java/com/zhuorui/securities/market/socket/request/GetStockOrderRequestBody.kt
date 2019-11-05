package com.zhuorui.securities.market.socket.request

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 16:31
 *    desc   : 拉取webSocket查询最新委托挂单数据
 */
class GetStockOrderRequestBody(val ts: String, val code: String)