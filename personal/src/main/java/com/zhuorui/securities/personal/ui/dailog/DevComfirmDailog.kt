package com.zhuorui.securities.personal.ui.dailog

import android.content.Context
import android.text.Spannable
import android.text.Spanned
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.StringRes
import butterknife.BindView
import com.zhuorui.commonwidget.R2
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/16
 * Desc:
 */
class DevComfirmDailog(
    context: Context,
    width: Int,
    private val canceledOnTouchOutside: Boolean,
    private val ignoreBack: Boolean
) : BaseDialog(context,width,WindowManager.LayoutParams.WRAP_CONTENT),View.OnClickListener{
    override val layout: Int
        get() = R.layout.layout_dev_comfirm_dailog
    @BindView(R2.id.tv_notice)
    lateinit var tv_notice: TextView
    @BindView(R2.id.tv_msg)
    lateinit var tv_msg: TextView
    @BindView(R2.id.tv_cancel)
    lateinit var tv_cancel: TextView
    @BindView(R2.id.tv_confirm)
    lateinit var tv_confirm: TextView

    private var callBack: CallBack? = null


    override fun init() {
        tv_cancel.setOnClickListener(this)
        tv_confirm.setOnClickListener(this)

        changeDialogOutside(canceledOnTouchOutside)
        if (ignoreBack)
            ignoreBackPressed()
    }
        fun setNoticeText(@StringRes strId: Int): DevComfirmDailog {
            tv_notice.text = ResUtil.getString(strId)
            return this
        }

    fun setMsgText(str: String): DevComfirmDailog {
        tv_msg.text = str
        return this
    }

    fun setMsgText(@StringRes strId: Int): DevComfirmDailog {
        tv_msg.text = ResUtil.getString(strId)
        return this
    }

    fun setMsgText(str: Spanned): DevComfirmDailog {
        tv_msg.text = str
        return this
    }

    fun setMsgText(str: Spannable): DevComfirmDailog {
        tv_msg.text = str
        return this
    }

    fun setCancelText(str: String): DevComfirmDailog {
        tv_cancel.text = str
        return this
    }

    fun setCancelText(@StringRes strId: Int): DevComfirmDailog {
        tv_cancel.text = ResUtil.getString(strId)
        return this
    }

    fun setConfirmText(str: String): DevComfirmDailog {
        tv_confirm.text = str
        return this
    }

    fun setConfirmText(@StringRes strId: Int): DevComfirmDailog {
        tv_confirm.text = ResUtil.getString(strId)
        return this
    }

    override fun onClick(p0: View) {
        when (p0) {
            tv_cancel -> {
                callBack?.onCancel()
            }
            tv_confirm -> {
                callBack?.onConfirm()
            }
        }
        dialog?.dismiss()
    }

    fun setCallBack(callBack: CallBack): DevComfirmDailog {
        this.callBack = callBack
        return this
    }
    /***
     * 回调点击
     */
    interface CallBack {

        fun onCancel()

        fun onConfirm()
    }

    // 默认提供的构造，有其他形态时，需要自行 new ConfirmDialog()
    companion object {

        /**
         * 提供一个默认构造宽度为250dp方法
         */
        fun createWidth255Dialog(
            context: Context,
            canceledOnTouchOutside: Boolean,
            ignoreBack: Boolean
        ): DevComfirmDailog {
            return DevComfirmDailog(context, ResUtil.getDimensionDp2Px(255f), canceledOnTouchOutside, ignoreBack)
        }
    }
    }