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
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/6
 * Desc:F10市场主营业务
 * */
class MainTradeYearsInfoPopWindow(contentView: View, num: Int, strList:ArrayList<String>,width: Int, height: Int) :
    PopupWindow(contentView, width, height),
    View.OnClickListener {

    private val mNum = num
    private val strInfo = strList
    private var callBack: OnYearInfoCallBack? = null
    private var popwidth = 0

    interface OnYearInfoCallBack {

        fun onYearsInfoClick(num: Int)
    }

    init {
        contentView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        popwidth = contentView.measuredWidth
        contentView.findViewById<TextView>(R.id.tv_1).setOnClickListener(this)
        contentView.findViewById<TextView>(R.id.tv_1).text=strInfo[0]
        contentView.findViewById<TextView>(R.id.tv_2).setOnClickListener(this)
        contentView.findViewById<TextView>(R.id.tv_2).text=strInfo[1]
        contentView.findViewById<TextView>(R.id.tv_3).setOnClickListener(this)
        contentView.findViewById<TextView>(R.id.tv_3).text=strInfo[2]
        contentView.findViewById<TextView>(R.id.tv_4).setOnClickListener(this)
        contentView.findViewById<TextView>(R.id.tv_4).text=strInfo[3]
        contentView.findViewById<TextView>(R.id.tv_5).setOnClickListener(this)
        contentView.findViewById<TextView>(R.id.tv_5).text=strInfo[4]
        var id = when (num) {
            1 -> {
                R.id.tv_1
            }
            2 -> {
                R.id.tv_2
            }
            3-> {
                R.id.tv_3
            }
            4->{
                R.id.tv_4
            }
            5->{
                R.id.tv_5
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
            R.id.tv_1 -> {
                onSelected(1)
            }
            R.id.tv_2 -> {
                onSelected(2)
            }
            R.id.tv_3 -> {
                onSelected(3)
            }
            R.id.tv_4 -> {
                onSelected(4)
            }
            R.id.tv_5 -> {
                onSelected(5)
            }
        }
        dismiss()
    }

    private fun onSelected(n: Int) {
        if (n != mNum) {
            callBack?.onYearsInfoClick(n)
        }
    }

    companion object {

        fun create(context: Context, mode: Int, strList:ArrayList<String>,callback: OnYearInfoCallBack): MainTradeYearsInfoPopWindow {
            val popupWindowView =
                View.inflate(context, R.layout.pop_mian_trade_years_layout, null)
            val popupWindow = MainTradeYearsInfoPopWindow(
                popupWindowView,
                mode,
                strList,
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
        val xoff = anchorW * 0.5 - (popwidth - 80      )//背景三角到最右侧距离
        showAsDropDown(anchor, xoff.toInt(), (anchor.resources.displayMetrics.density * 3).toInt())
    }



}