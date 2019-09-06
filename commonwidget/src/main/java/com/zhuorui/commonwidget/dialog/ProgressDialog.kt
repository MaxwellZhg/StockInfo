package com.zhuorui.commonwidget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import com.zhuorui.commonwidget.R
import com.zhuorui.commonwidget.ZRLoadingView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/22
 * Desc:
 */

class ProgressDialog(context: Context) : Dialog(context, R.style.loading_dialog_style) {

    var loadingView: ZRLoadingView? = null
    //dismiss回调
    private var dismissCallback: () -> Unit = {}

    init {
        setContentView(R.layout.layout_dialog_loading)
        loadingView = findViewById(R.id.logind)
        // 设置居中
        window!!.attributes.gravity = Gravity.CENTER
        val lp = window!!.attributes
        // 设置背景层透明度
        lp.dimAmount = 0.6f
        window!!.attributes = lp
        // 取消监听
        setOnCancelListener {
            dismissCallback()
        }
    }

    override fun show() {
        super.show()
        // 启动动画
        loadingView?.start()
    }

    override fun dismiss() {
        if (isShowing)
            super.dismiss()
        loadingView?.stop()
    }

    fun setMessage(msg: String?) {
        loadingView?.setMessage(msg)
    }

    /**
     * 销毁
     */
    fun setDismissCallback(callback: () -> Unit) {
        dismissCallback = callback
    }
}
