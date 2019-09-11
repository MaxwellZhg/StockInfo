package com.zhuorui.securities.market.custom

import android.content.Context
import android.view.WindowManager
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/10 16:14
 *    desc   : 交易订单对话框
 */
class TradingStocksOrderDialog(
    context: Context,
    width: Int,
    private val canceledOnTouchOutside: Boolean,
    private val ignoreBack: Boolean
) : BaseDialog(context, width, WindowManager.LayoutParams.WRAP_CONTENT) {

    override val layout: Int
        get() = R.layout.dialog_trading_stocks_order

    // 默认提供的构造，有其他形态时，需要自行 new ConfirmDialog()
    companion object {

        /**
         * 提供一个默认构造宽度为225dp方法
         */
        fun createDialog(
            context: Context,
            canceledOnTouchOutside: Boolean,
            ignoreBack: Boolean
        ): TradingStocksOrderDialog {
            return TradingStocksOrderDialog(
                context,
                ResUtil.getDimensionDp2Px(260f),
                canceledOnTouchOutside,
                ignoreBack
            )
        }
    }
}