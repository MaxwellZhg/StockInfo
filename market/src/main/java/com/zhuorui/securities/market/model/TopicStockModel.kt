package com.zhuorui.securities.market.model

import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.securities.market.manager.StockPriceDataManager
import com.zhuorui.securities.market.util.MathUtil
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/22 15:43
 *    desc   : 自选股列表展示数据对象
 */
class TopicStockModel : Observer {
    var position: Int? = null
    // 自选股信息
    var stockInfo: StockMarketInfo? = null
        set(value) {
            field = value

            val manager = StockPriceDataManager.getInstance(value!!.ts!!, value.code!!, value.type!!)
            // 添加股价订阅
            manager.registerObserver(this)

            // 当没有价格数据时，从缓存中取数据
            if (value.last == null) {
                val data = manager.priceData
                updatePrice(data)
            }

        }

    // 长按
    var longClick: Boolean = false

    // 当股价变化时，触发onChange()
    private var onChangeDataCallBack: OnChangeDataCallBack? = null

    fun setOnChangeDataCallBack(onChangeDataCallBack: OnChangeDataCallBack?) {

        this.onChangeDataCallBack = onChangeDataCallBack

        if (onChangeDataCallBack == null) {
            StockPriceDataManager.getInstance(stockInfo!!.ts!!, stockInfo!!.code!!, stockInfo!!.type!!).removeObserver(
                this
            )
        }
    }

    /**
     * 当股价数据变化时，触发此方法
     */
    override fun update(subject: Subject<*>?) {
        if (subject is StockPriceDataManager) {
            updatePrice(subject.priceData)
        }
    }

    /**
     * 更新股价数据
     */
    private fun updatePrice(priceData: PushStockPriceData?) {
        if (stockInfo != null && priceData != null) {
            stockInfo!!.last = priceData.last!!
            stockInfo!!.diffPrice = priceData.last!!.subtract(priceData.open)
            stockInfo!!.diffRate = MathUtil.divide2(
                stockInfo!!.diffPrice!!.multiply(BigDecimal.valueOf(100)),
                priceData.open!!
            )
            stockInfo!!.pctTag = priceData.pctTag

            if (position != null) {
                onChangeDataCallBack?.onPriceChange(stockInfo!!, position!!)
            }
        }
    }

    interface OnChangeDataCallBack {

        fun onPriceChange(stockInfo: StockMarketInfo, position: Int)
    }
}