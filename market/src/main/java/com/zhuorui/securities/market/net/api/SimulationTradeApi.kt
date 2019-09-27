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
    const val FUND_ACCOUNT = "as_sims/api/fund_account/v1/get_fund_account"

    /**
     * 计算盈亏和总盈亏所需参数接口
     */
    const val TODAY_PROFIT_AND_LOSS = "as_sims/api/account_asset_stat/v1/statistical/todayProfitAndLoss"

    /**
     * 创建资金账号
     */
    const val CREATE_FUND_ACCOUNT = "as_sims/api/fund_account/v1/create_fund_account"

    /**
     * 修改委托订单
     */
    const val UPDATE_TRUST_ORDER = "api/mock/update_trust_order"

    /**
     * 获取单支股票详情
     */
    const val STOCK_INFO = "as_market/api/hk_stock_basic/v1/get_one_by_tscode"

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
    const val FEE_COMPUTE = "as_sims/api/cost/v1/fee_compute"
    /**
     * 持仓
     */
    const val GET_POSITION = "as_sims/api/stock_hold/v1/hold"

    /**
     * 今日订单/历史订单
     */
    const val ORDER_LIST = "as_sims/api/trust/v1/order/list"
    /**
     * 交易费用规则模版规则
     */
    const val FEE_TEMPLATE = "as_sims/api/cost/v1/fee_template"

    /**
     * 买入股票
     */
    const val STOCK_BUY = "as_sims/api/trade/v1/buy"

    /**
     * 卖出股票
     */
    const val STOCK_SELL = "as_sims/api/trade/v1/sell"

    /**
     * 修改买入订单
     */
    const val UPDATE_BUY_TRUST = "as_sims/api/trade/v1/update_buy_trust"

    /**
     * 修改卖出订单
     */
    const val UPDATE_SELL_TRUST = "as_sims/api/trade/v1/update_sell_trust"

    /**
     * 撤消买入订单
     */
    const val CANCEL_BUY_TRUST = "as_sims/api/trade/v1/cancel_buy_trust"

    /**
     * 撤销卖出委托订单
     */
    const val CANCEL_SELL_TRUST = "as_sims/api/trade/v1/cancel_sell_trust"
}