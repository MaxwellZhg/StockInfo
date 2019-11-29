package com.zhuorui.securities.market.event

import com.zhuorui.securities.market.net.response.StockConsInfoResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/21
 * Desc:成分股发给指数详情的事件
 * */
class StockConsEvent (var list: ArrayList<StockConsInfoResponse.ListInfo>)