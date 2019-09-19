package com.zhuorui.securities.market.net.api

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/16 10:29
 *    desc   : 模拟炒股
 */
object SimulationTradeApi {

    /**
     * 查询资金账户接口
     */
    const val FUND_ACCOUNT = "api/fund_account/v1/get_fund_account"

    /**
     * 创建资金账号
     */
    const val CREATE_FUND_ACCOUNT = "api/mock/create_fund_account"

    /**
     * 修改委托订单
     */
    const val UPDATE_TRUST_ORDER = "api/mock/update_trust_order"

    /**
     * 撤销订单
     */
    const val CANCEL_TRUST_ORDER = "/api/mock/cancel_trust_order"

    /**
     * 获取单支股票详情
     */
    const val STOCK_INFO = "http://192.168.1.212:1105/as_market/api/hk/stock/basic/v1/get/one/by/tscode"

    /**
     * 获取批量股票详情
     */
    const val STOCKS_INFO = "http://192.168.1.212:1105/as_market/api/hk/stock/basic/v1/get/list/by/tscode"

    /**
     * 更新股票数据
     */
    const val UPDATE_STOCK_DATA = "http://192.168.1.212:1105/as_market/api/hk/stock/basic/v1/update/info/by/id"

    /**
     * 计算交易费用
     */
    const val FEE_COMPUTE = "as_sims/api/fee/v1/fee_compute"

    /**
     * 持仓
     */
    const val GET_POSITION = "api/stock_hold/v1/get_position"

    /**
     * 今日订单/历史订单
     */
    const val order_list = "api/trust_record/v1/order/list"

}