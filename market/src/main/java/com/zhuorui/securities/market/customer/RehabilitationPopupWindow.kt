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
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/10/29 17:44
 *    desc   : 是否复权弹窗
 */
class RehabilitationPopupWindow(contentView: View, mode: Int, width: Int, height: Int) :
    PopupWindow(contentView, width, height),
    View.OnClickListener {

    private var callBack: CallBack? = null

    interface CallBack {

        /**
         * 是否复权
         *@param mode 0 不复权 1 前复权 2 后复权
         */
        fun onSelected(mode: Int)
    }

    init {
        val noRehabilitation = contentView.findViewById<TextView>(R.id.no_rehabilitation)
        noRehabilitation.setOnClickListener(this)
        val beforeRehabilitation = contentView.findViewById<TextView>(R.id.before_rehabilitation)
        beforeRehabilitation.setOnClickListener(this)
        val afterRehabilitation = contentView.findViewById<TextView>(R.id.after_rehabilitation)
        afterRehabilitation.setOnClickListener(this)

        when (mode) {
            0 -> {
                ResUtil.getColor(R.color.color_53A0FD)?.let { noRehabilitation.setTextColor(it) }
            }
            1 -> {
                ResUtil.getColor(R.color.color_53A0FD)?.let { beforeRehabilitation.setTextColor(it) }
            }
            2 -> {
                ResUtil.getColor(R.color.color_53A0FD)?.let { afterRehabilitation.setTextColor(it) }
            }
        }
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.no_rehabilitation -> {
                callBack?.onSelected(0)
            }
            R.id.before_rehabilitation -> {
                callBack?.onSelected(1)
            }
            R.id.after_rehabilitation -> {
                callBack?.onSelected(2)
            }
        }
        dismiss()
    }

    companion object {

        fun create(context: Context, mode: Int, callback: CallBack): RehabilitationPopupWindow {
            val popupWindowView = View.inflate(context, R.layout.pop_rehabilitation, null)
            val popupWindow =
                RehabilitationPopupWindow(
                    popupWindowView,
                    mode,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            // 设置背景
            popupWindow.setBackgroundDrawable(ColorDrawable())
            // 外部点击事件
            popupWindow.isOutsideTouchable = true
            popupWindow.callBack = callback
            return popupWindow
        }
    }

}