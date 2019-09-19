package com.zhuorui.commonwidget.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.Spanned
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import butterknife.BindView
import com.zhuorui.commonwidget.R
import com.zhuorui.commonwidget.R2
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.base2app.util.ResUtil

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/29 10:27
 *    desc   : 含有 标题、消息、确定按钮 对话框
 */
class TitleMessageConfirmDialog(
    context: Context,
    width: Int,
    private val canceledOnTouchOutside: Boolean,
    private val ignoreBack: Boolean
) : BaseDialog(context, width, WindowManager.LayoutParams.WRAP_CONTENT),
    View.OnClickListener {

    @BindView(R2.id.tv_title)
    lateinit var tv_title: TextView
    @BindView(R2.id.tv_msg)
    lateinit var tv_msg: TextView
    @BindView(R2.id.tv_confirm)
    lateinit var tv_confirm: TextView

    override val layout: Int
        get() = R.layout.dialog_title_message_confirm

    override fun init() {
        tv_confirm.setOnClickListener(this)

        changeDialogOutside(canceledOnTouchOutside)
        if (ignoreBack)
            ignoreBackPressed()
    }

    fun setTitleText(str: String): TitleMessageConfirmDialog {
        tv_title.text = str
        tv_title.visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
        return this
    }

    fun setTitleText(@StringRes strId: Int): TitleMessageConfirmDialog {
        tv_title.text = ResUtil.getString(strId)
        tv_title.visibility = if (strId == 0) View.GONE else View.VISIBLE
        return this
    }

    fun setTitleText(str: Spanned): TitleMessageConfirmDialog {
        tv_title.text = str
        tv_title.visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
        return this
    }

    fun setTitleText(str: Spannable): TitleMessageConfirmDialog {
        tv_title.text = str
        tv_title.visibility = if (TextUtils.isEmpty(str)) View.GONE else View.VISIBLE
        return this
    }

    fun setMsgText(str: String): TitleMessageConfirmDialog {
        tv_msg.text = str
        return this
    }

    fun setMsgText(@StringRes strId: Int): TitleMessageConfirmDialog {
        tv_msg.text = ResUtil.getString(strId)
        return this
    }

    fun setMsgText(str: Spanned): TitleMessageConfirmDialog {
        tv_msg.text = str
        return this
    }

    fun setMsgText(str: Spannable): TitleMessageConfirmDialog {
        tv_msg.text = str
        return this
    }

    fun setConfirmText(str: String): TitleMessageConfirmDialog {
        tv_confirm.text = str
        return this
    }

    fun setConfirmText(@StringRes strId: Int): TitleMessageConfirmDialog {
        tv_confirm.text = ResUtil.getString(strId)
        return this
    }

    override fun onClick(p0: View) {
        dialog?.dismiss()
    }

    // 默认提供的构造，有其他形态时，需要自行 new ConfirmDialog()
    companion object {

        /**
         * 提供一个默认构造宽度为225dp方法
         */
        fun createWidth225Dialog(
            context: Context,
            canceledOnTouchOutside: Boolean,
            ignoreBack: Boolean
        ): TitleMessageConfirmDialog {
            return TitleMessageConfirmDialog(
                context,
                ResUtil.getDimensionDp2Px(225f),
                canceledOnTouchOutside,
                ignoreBack
            )
        }
    }
}