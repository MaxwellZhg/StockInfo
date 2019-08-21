package com.zhuorui.securities.infomation.ui.dailog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.zhuorui.securities.infomation.R
import kotlinx.android.synthetic.main.item_dailog_info.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/19
 * Desc:
 */

class InfoDialog :Dialog {
    constructor(context: Context) : this(context, 0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.dialog) {
        setContentView(R.layout.item_dailog_info)
        window!!.setGravity(Gravity.CENTER)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setCanceledOnTouchOutside(false)
    }
    public fun setOnclickListener(listener: View.OnClickListener){
        if (rl_gotomain !== null){
            rl_gotomain.setOnClickListener(listener)
        }
        if (rl_completeinfo !== null){
            rl_completeinfo.setOnClickListener(listener)
        }
    }
}