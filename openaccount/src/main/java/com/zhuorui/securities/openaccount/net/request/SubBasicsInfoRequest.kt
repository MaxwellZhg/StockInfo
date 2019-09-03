package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-03 18:22
 *    desc   :
 */
class SubBasicsInfoRequest(
    val mailbox: String, // 邮箱
    val occupation: Int, //职业状态
    val taxType: Int, // 税务类型
    val taxState: String, // 税务国家/地区
    val taxNumber: String, // 税务编号
    val income: Int, // 财产状况
    val rate: Int, // 交易频率
    val risk: Int, // 风险承受能力
    val capitalSource: String, // 资金来源
    val investShares: Int, // 股票投资经验
    val investBond: Int, // 债券投资经验
    val investGoldForeign: Int, // 外币/黄金投资经验
    val investFund: Int, // 基金/理财投资经验
    val id: String, //开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }


}