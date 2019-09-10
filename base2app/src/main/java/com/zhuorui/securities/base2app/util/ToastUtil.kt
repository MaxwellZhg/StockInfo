package com.zhuorui.securities.base2app.util

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.zhuorui.securities.base2app.BaseApplication
import com.zhuorui.securities.base2app.R

/**
 * Created by xieyingwu on 2018/5/8
 * Toast工具类
 */
class ToastUtil private constructor() {

    private var toast: Toast? = null
    private var centerToast: Toast? = null

    private object Builder {
        val instance = ToastUtil()
    }

    /**
     * 采用系统的toast
     */
    @SuppressLint("ShowToast")
    fun toast(@StringRes res: Int, duration: Int) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.context, res, duration)
        }
        toast?.setText(res)
        toast?.show()
    }

    /**
     * 采用系统的toast
     */
    @SuppressLint("ShowToast")
    fun toast(cs: CharSequence, duration: Int) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.context, cs, duration)
        }
        toast?.setText(cs)
        toast?.show()
    }

    fun toast(@StringRes strRes: Int) {
        toast(strRes, Toast.LENGTH_SHORT)
    }

    fun toast(str: CharSequence) {
        toast(str, Toast.LENGTH_SHORT)
    }

    fun toastLong(@StringRes res: Int) {
        toast(res, Toast.LENGTH_LONG)
    }

    fun toastLong(cs: CharSequence) {
        toast(cs, Toast.LENGTH_LONG)
    }

    /**
     * 自定义居中的toast
     */
    @SuppressLint("ShowToast")
    fun toastCenter(@StringRes res: Int, duration: Int) {
        createToast()
        val tv = centerToast!!.view.findViewById<TextView>(R.id.toast_tv)
        tv.text = ResUtil.getString(res)
        centerToast!!.setGravity(Gravity.CENTER, 0, 0)
        centerToast!!.duration = duration
        centerToast!!.show()
    }

    /**
     * 自定义居中的toast
     */
    @SuppressLint("ShowToast")
    fun toastCenter(cs: CharSequence, duration: Int) {
        createToast()
        val tv = centerToast!!.view.findViewById<TextView>(R.id.toast_tv)
        tv.text = cs
        centerToast!!.setGravity(Gravity.CENTER, 0, 0)
        centerToast!!.duration = duration
        centerToast!!.show()
    }

    @SuppressLint("InflateParams")
    private fun createToast() {
        if (centerToast == null) {
            centerToast = Toast(BaseApplication.context)
            val view = LayoutInflater.from(BaseApplication.context).inflate(R.layout.layout_toast, null)
            centerToast!!.view = view
        }
    }

    fun toastCenter(@StringRes res: Int) {
        toastCenter(res, Toast.LENGTH_SHORT)
    }

    fun toastCenter(cs: CharSequence) {
        toastCenter(cs, Toast.LENGTH_SHORT)
    }

    fun toastCenterLong(@StringRes res: Int) {
        toastCenter(res, Toast.LENGTH_LONG)
    }

    fun toastCenterLong(cs: CharSequence) {
        toastCenter(cs, Toast.LENGTH_LONG)
    }

    companion object {

        val instance: ToastUtil
            get() = Builder.instance
    }
}
