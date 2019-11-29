package com.zhuorui.commonwidget.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
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
 *    desc   : App内部顶部通知
 */
class NoticeDialog(
    context: Context,
    width: Int,
    val callback: CallBack
) : BaseDialog(context, width, WindowManager.LayoutParams.WRAP_CONTENT, R.style.DialogTransparent),
    View.OnClickListener {

    @BindView(R2.id.dialog_view)
    lateinit var dialog_view: View
    @BindView(R2.id.iv_msg_icon)
    lateinit var iv_msg_icon: ImageView
    @BindView(R2.id.tv_msg_type)
    lateinit var tv_msg_type: TextView
    @BindView(R2.id.tv_msg_time)
    lateinit var tv_msg_time: TextView
    @BindView(R2.id.tv_msg_title)
    lateinit var tv_msg_title: TextView
    @BindView(R2.id.tv_msg_content)
    lateinit var tv_msg_content: TextView

    private val handler = Handler()

    override val layout: Int
        get() = R.layout.dialog_notice

    override fun init() {
        dialog_view.setOnClickListener(this)
    }

    fun setDisPlayNoticeInfo(
        @DrawableRes iconResId: Int, @StringRes msgTypeStrId: Int, time: String,
        title: String,
        content: String
    ): NoticeDialog {
        iv_msg_icon.setImageResource(iconResId)
        tv_msg_type.setText(msgTypeStrId)
        tv_msg_time.text = time
        tv_msg_title.text = title
        tv_msg_content.text = content
        return this
    }

    override fun onClick(p0: View) {
        dialog?.dismiss()
        // 回调点击事件
        callback.onMessageClicked()
    }

    override fun show() {
        val window = dialog!!.window ?: return
        // //设置窗口弹出动画
        window.setWindowAnimations(R.style.dialogWindowAnim)
        // 设置弹出位置处于屏幕上方
        updatePosition(0, 0, Gravity.TOP)
        super.show()
    }

    override fun onShow(dialog: DialogInterface) {
        super.onShow(dialog)
        // 3秒后自动消失
        handler.postDelayed({ hide() }, 3000)
    }

    /***
     * 回调点击
     */
    interface CallBack {

        fun onMessageClicked()
    }

    companion object {

        fun createDialog(
            context: Context,
            callback: CallBack
        ): NoticeDialog {
            return NoticeDialog(context, ResUtil.getDimensionDp2Px(355f), callback)
        }
    }
}