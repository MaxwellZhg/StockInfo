package com.zhuorui.securities.market.model

import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.securities.market.manager.StockIndexhandicapConsDataManager
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.socket.vo.StockHandicapData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/29
 * Desc:成分股订阅model
 */
class StockConsInfoModel : Observer {
    var position: Int? = null
    // 自选股信息
    var stockInfo: StockConsInfoResponse.ListInfo? = null
        set(value) {
            field = value

            val manager = StockIndexhandicapConsDataManager.getInstance(value!!.ts!!, value.code!!, 2)
            // 添加股价订阅
            manager.registerObserver(this)

        }

    // 长按
    var longClick: Boolean = false

    // 当股价变化时，触发onChange()
    private var onChangeDataCallBack: OnChangeDataCallBack? = null

    fun setOnChangeDataCallBack(onChangeDataCallBack: OnChangeDataCallBack?) {

        this.onChangeDataCallBack = onChangeDataCallBack

        if (onChangeDataCallBack == null) {
            StockIndexhandicapConsDataManager.getInstance(stockInfo!!.ts!!, stockInfo!!.code!!, 2).removeObserver(
                this
            )
        }
    }

    /**
     * 当股价数据变化时，触发此方法
     */
    override fun update(subject: Subject<*>?) {
        if (subject is StockIndexhandicapConsDataManager) {
            updatePrice(subject.stockConsIndexData)
        }
    }

    /**
     * 更新股价数据
     */
    private fun updatePrice(priceData: StockHandicapData?) {
        if (stockInfo != null && priceData != null) {
            stockInfo!!.last= priceData.last!!.toBigDecimal()
            stockInfo!!.diffRate = priceData.diffRate!!.toBigDecimal()
            stockInfo!!.turnover = priceData.turnover!!
            if (position != null) {
                onChangeDataCallBack?.onStockIndexConsChange(stockInfo!!, position!!)
            }
        }
    }

    interface OnChangeDataCallBack {

        fun onStockIndexConsChange(stockInfo: StockConsInfoResponse.ListInfo, position: Int)
    }
}