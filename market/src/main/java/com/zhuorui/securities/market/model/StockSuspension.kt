package com.zhuorui.securities.market.model

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/20 10:09
 *    desc   : 股市状态
 */
enum class StockSuspension {
    empty,// 空值，无效
    normal,// 1-正常
    suspension,// 2-暂停交易或者停牌
    resumption// 3-复牌
}