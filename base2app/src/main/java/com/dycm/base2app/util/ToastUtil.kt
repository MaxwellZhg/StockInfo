package com.dycm.base2app.util

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes

import com.dycm.base2app.BaseApplication
import com.dycm.base2app.R

/**
 * Created by xieyingwu on 2018/5/8
 * Toast工具类
 */
class ToastUtil private constructor() {

    private var toast: Toast? = null

    private object Builder {
        val instance = ToastUtil()
    }

    fun toast(@StringRes res: Int, duration: Int, gravity: Int) {
        createToast()
        val tv = toast!!.view.findViewById<TextView>(R.id.toast_tv)
        tv.setText(res)
        val yOffset = getYOffset(gravity)
        toast!!.setGravity(gravity, 0, yOffset)
        toast!!.duration = duration
        toast!!.show()
    }

    private fun getYOffset(gravity: Int): Int {
        if (Gravity.BOTTOM == gravity) {
            return ResUtil.getDimensionDp2Px(100f)
        } else if (Gravity.CENTER == gravity) {
            return 0
        }
        return 0
    }

    fun toast(cs: CharSequence, duration: Int, gravity: Int) {
        createToast()
        val tv = toast!!.view.findViewById<TextView>(R.id.toast_tv)
        tv.text = cs
        val yOffset = getYOffset(gravity)
        toast!!.setGravity(gravity, 0, yOffset)
        toast!!.duration = duration
        toast!!.show()
    }

    fun toast(@StringRes strRes: Int) {
        toast(strRes, Toast.LENGTH_SHORT, Gravity.BOTTOM)
    }

    fun toast(str: CharSequence) {
        toast(str, Toast.LENGTH_SHORT, Gravity.BOTTOM)
    }

    fun toastLong(@StringRes res: Int, gravity: Int) {
        toast(res, Toast.LENGTH_LONG, gravity)
    }

    fun toastLong(cs: CharSequence, gravity: Int) {
        toast(cs, Toast.LENGTH_LONG, gravity)
    }

    fun toastShort(cs: CharSequence, gravity: Int) {
        toast(cs, Toast.LENGTH_SHORT, gravity)
    }

    fun toastShort(@StringRes res: Int, gravity: Int) {
        toast(res, Toast.LENGTH_SHORT, gravity)
    }

    fun toastCenter(@StringRes res: Int) {
        toastLong(res, Gravity.CENTER)
    }

    fun toastCenter(cs: CharSequence) {
        toastLong(cs, Gravity.CENTER)
    }

    @SuppressLint("InflateParams")
    private fun createToast() {
        if (toast == null) {
            toast = Toast(BaseApplication.context)
            val view = LayoutInflater.from(BaseApplication.context).inflate(R.layout.layout_toast, null)
            toast!!.view = view
        }
    }

    companion object {

        val instance: ToastUtil
            get() = Builder.instance
    }
}
