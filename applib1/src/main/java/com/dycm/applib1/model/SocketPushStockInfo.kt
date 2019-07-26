package com.dycm.applib1.model

class SocketPushStockInfo {

    var code: String? = null// 股票代码
    var dataType: Int = 0// 数据类型(1:行情,2:K线,3:盘口,4:股价)
    var data: Data? = null// 股票数据
    var ts: String? = null// 属于的股票市场(SZ-深圳,SH-上海,HK-港股,US-美股)

    inner class Data {
        var lotSize: Double = 0.toDouble()
        var name: String?=null
        var price: Double = 0.toDouble()
        var lastPrice: Double = 0.toDouble()
        var openPrice: Double = 0.toDouble()
        var amount: Double = 0.toDouble()
        var time: String?=null
        var high: Double = 0.toDouble()
        var low: Double = 0.toDouble()

        fun updateData(info: SocketPushStockInfo?): Boolean {
            if (info?.data == null) return false
            lotSize = info.data!!.lotSize
            name = info.data!!.name
            price = info.data!!.price
            lastPrice = info.data!!.lastPrice
            openPrice = info.data!!.openPrice
            amount = info.data!!.amount
            time = info.data!!.time
            high = info.data!!.high
            low = info.data!!.low
            return true
        }
    }
}