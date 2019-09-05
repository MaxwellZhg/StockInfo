package com.zhuorui.securities.openaccount.custom

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/4
 * Desc:
 */
class ShareInfoPopupWindow (contentView: View, width: Int, height: Int) : PopupWindow(contentView, width, height),
    View.OnClickListener {

    private var callBack: CallBack? = null

    interface CallBack {
        fun onInfoMation()

        fun onHelpCenter()
    }

    init {
        contentView.findViewById<View>(R.id.tv_share_info).setOnClickListener(this)
        contentView.findViewById<View>(R.id.tv_share_helpcenter).setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_share_info -> {
                dismiss()
                callBack?.onInfoMation()
            }
            R.id.tv_share_helpcenter -> {
                dismiss()
                callBack?.onHelpCenter()
            }
        }
    }


    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        super.showAsDropDown(anchor, xoff, yoff, Gravity.RIGHT)
    }

    companion object {

        fun create(context: Context, callback: CallBack): ShareInfoPopupWindow {
            val popupWindowView = View.inflate(context, R.layout.layout_item_share_info, null)
            val popupWindow =
                ShareInfoPopupWindow(
                    popupWindowView,
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