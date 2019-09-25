package com.zhuorui.securities.market.custom

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import butterknife.BindView
import com.zhuorui.commonwidget.StateButton
import com.zhuorui.securities.base2app.BaseApplication
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.base2app.util.AppUtil
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.R2

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
) : BaseDialog(context, width, WindowManager.LayoutParams.WRAP_CONTENT), View.OnClickListener {

    @BindView(R2.id.tv_account)
    lateinit var tv_account: TextView
    @BindView(R2.id.tv_type)
    lateinit var tv_type: TextView
    @BindView(R2.id.tv_stock_name)
    lateinit var tv_stock_name: TextView
    @BindView(R2.id.tv_price)
    lateinit var tv_price: TextView
    @BindView(R2.id.tv_count)
    lateinit var tv_count: TextView
    @BindView(R2.id.tv_commission)
    lateinit var tv_commission: TextView
    @BindView(R2.id.tv_money)
    lateinit var tv_money: TextView

    @BindView(R2.id.btn_cancel)
    lateinit var btn_cancel: StateButton
    @BindView(R2.id.btn_confirm)
    lateinit var btn_confirm: StateButton

    private var callBack: CallBack? = null

    override val layout: Int
        get() = R.layout.dialog_trading_stocks_order

    override fun init() {
        btn_cancel.setOnClickListener(this)
        btn_confirm.setOnClickListener(this)

        changeDialogOutside(canceledOnTouchOutside)
        if (ignoreBack)
            ignoreBackPressed()
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_cancel -> {
                callBack?.onCancel()
            }
            btn_confirm -> {
                callBack?.onConfirm()
            }
        }
        dialog?.dismiss()
    }

    fun setCallBack(callBack: CallBack): TradingStocksOrderDialog {
        this.callBack = callBack
        return this
    }

    fun setInfo(
        accountId: String,
        chargeType: Int,
        stockName: String,
        tsCode: String,
        price: String,
        count: String,
        commission: Double,
        money: String
    ): TradingStocksOrderDialog {
        tv_account.text = BaseApplication.context?.getString(R.string.order_detail_account, accountId)
        tv_type.text =
            ResUtil.getString(if (chargeType == 1) R.string.order_detail_type_buy else R.string.order_detail_type_sell)
        tv_stock_name.text = BaseApplication.context?.getString(R.string.order_detail_stock, "$stockName（$tsCode）")
        tv_price.text = BaseApplication.context?.getString(R.string.order_detail_price, price)
        tv_count.text = BaseApplication.context?.getString(R.string.order_detail_count, count)
        tv_commission.text = BaseApplication.context?.getString(R.string.order_detail_commission, commission.toString())
        tv_money.text = BaseApplication.context?.getString(R.string.order_detail_money, money)
        return this
    }

    /***
     * 回调点击
     */
    interface CallBack {

        fun onCancel()

        fun onConfirm()
    }

    // 默认提供的构造，有其他形态时，需要自行 new ConfirmDialog()
    companion object {

        /**
         * 提供一个默认宽度的对话框
         */
        fun createDialog(
            context: Context,
            canceledOnTouchOutside: Boolean,
            ignoreBack: Boolean
        ): TradingStocksOrderDialog {
            return TradingStocksOrderDialog(
                context,
                (AppUtil.phoneScreenWidth * 0.6933).toInt(),
                canceledOnTouchOutside,
                ignoreBack
            )
        }
    }
}