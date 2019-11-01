package com.zhuorui.securities.market.customer

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R


/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com0
 *    date   : 2019-11-01 14:52
 *    desc   :
 */
class OrderBrokerNumPopWindow(contentView: View, num: Int, width: Int, height: Int) :
    PopupWindow(contentView, width, height),
    View.OnClickListener {

    private val mNum = num
    private var callBack: OnSelectCallBack? = null
    private var popwidth = 0

    interface OnSelectCallBack {

        fun onSelected(num: Int)
    }

    init {
        contentView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        popwidth = contentView.measuredWidth
        contentView.findViewById<View>(R.id.layout_5).setOnClickListener(this)
        contentView.findViewById<View>(R.id.layout_10).setOnClickListener(this)
        contentView.findViewById<View>(R.id.layout_40).setOnClickListener(this)
        contentView.findViewById<View>(R.id.layout_multiseriate).setOnClickListener(this)
        var id = when (num) {
            5 -> {
                R.id.tv_5
            }
            10 -> {
                R.id.tv_10
            }
            40 -> {
                R.id.tv_40
            }
            else -> 0
        }
        if (id != 0) {
            ResUtil.getColor(R.color.color_53A0FD)
                ?.let { contentView.findViewById<TextView>(id).setTextColor(it) }
        }

    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.layout_5 -> {
                onSelected(5)
            }
            R.id.layout_10 -> {
                onSelected(10)
            }
            R.id.layout_40 -> {
                onSelected(40)
            }
            R.id.layout_multiseriate -> {
                onSelected(-1)
            }
        }
        dismiss()
    }

    private fun onSelected(n: Int) {
        if (n != mNum) {
            callBack?.onSelected(n)
        }
    }

    companion object {

        fun create(context: Context, mode: Int, callback: OnSelectCallBack): OrderBrokerNumPopWindow {
            val popupWindowView =
                View.inflate(context, R.layout.pop_order_broker_num, null)
            val popupWindow = OrderBrokerNumPopWindow(
                popupWindowView,
                mode,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            // 设置背景
            popupWindow.setBackgroundDrawable(ColorDrawable())
            // 外部点击事件
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.callBack = callback
            return popupWindow
        }
    }

    override fun showAsDropDown(anchor: View?) {
        //计算三角到anchor中间位置偏移量
        val anchorW = anchor!!.width
        val xoff = anchorW * 0.5 - (popwidth - 30)//背景三角到最右侧距离
        showAsDropDown(anchor, xoff.toInt(), (anchor.resources.displayMetrics.density * 3).toInt())
    }

}