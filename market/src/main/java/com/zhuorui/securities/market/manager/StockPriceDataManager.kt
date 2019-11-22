package com.zhuorui.securities.market.manager

import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/22 15:02
 *    desc   : 用于存放股票价格数据
 */
class StockPriceDataManager private constructor(val ts: String, val code: String, val type: Int)