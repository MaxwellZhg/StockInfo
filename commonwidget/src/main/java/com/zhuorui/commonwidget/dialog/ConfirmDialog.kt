package com.zhuorui.commonwidget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.zhuorui.commonwidget.R

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-29 09:42
 *    desc   :
 */
open class ConfirmDialog(context: Context) :Dialog(context){

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val params = window!!.attributes
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.dimAmount = 0.7f
        window!!.attributes = params
        setCanceledOnTouchOutside(false)

    }

    override fun show() {
        if (context is Activity && (context as Activity).isFinishing) {
            return
        }
        if (isShowing) super.dismiss()
        super.show()
    }


    class Builder(context: Context){
        val context: Context = context
        var title: CharSequence? = null
        var message: CharSequence? = null
        var tips: CharSequence? = null
        var positive: CharSequence? = null
        var negative: CharSequence? = null
        var layout: View? = null
        var expand: View? = null
        var cancelable = true

        var positiveButtonClickListener: DialogInterface.OnClickListener? = null
        var negativeButtonClickListener: DialogInterface.OnClickListener? = null
        var cancelListener: DialogInterface.OnCancelListener? = null

        fun setMessage(message: CharSequence): Builder {
            this.message = message
            return this
        }

        fun setMessage(message: Int): Builder {
            this.message = context.getText(message)
            return this
        }

        fun setTips(tips: Int): Builder {
            this.tips = context.getText(tips)
            return this
        }

        fun setTips(tips: CharSequence): Builder {
            this.tips = tips
            return this
        }

        fun setTitle(title: Int): Builder {
            this.title = context.getText(title)
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            this.title = title
            return this
        }

        fun setPositiveButton(
            positive: CharSequence,
            listener: DialogInterface.OnClickListener
        ): Builder {
            this.positive = positive
            this.positiveButtonClickListener = listener
            return this
        }

        fun setPositiveButton(
            positive: Int,
            listener: DialogInterface.OnClickListener
        ): Builder {
            this.positive = context.getText(positive)
            this.positiveButtonClickListener = listener
            return this
        }

        fun setOnCancelListener(
            listener: DialogInterface.OnCancelListener
        ): Builder {
            this.cancelListener = listener
            return this
        }

        fun setNegativeButton(
            negative: CharSequence,
            listener: DialogInterface.OnClickListener
        ): Builder {
            this.negative = negative
            this.negativeButtonClickListener = listener
            return this
        }

        fun setNegativeButton(
            negative: Int,
            listener: DialogInterface.OnClickListener
        ): Builder {
            this.negative = context.getText(negative)
            this.negativeButtonClickListener = listener
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun addExpandView(v: View): Builder {
            this.expand = v
            return this
        }

        /**
         * Create the custom dialog
         */
        fun create(): ConfirmDialog {

            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialog = ConfirmDialog(this.context)
            layout = inflater.inflate(getLayoutRes(), null)

            // 设置标题
            if (title != null) {
                val view = layout?.findViewById<View>(R.id.dialog_title) as TextView
                view.text = title
                view.visibility = View.VISIBLE
            }
            // 设置信息内容
            if (message != null) {
                val view = layout?.findViewById<View>(R.id.dialog_msg) as TextView
                view.text = message
                view.visibility = View.VISIBLE
            }


            //设置扩展
            if (expand != null) {
                val linearLayout = layout?.findViewById<View>(R.id.expand) as LinearLayout
                linearLayout.addView(
                    expand,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                )
                linearLayout.visibility = View.VISIBLE
            }

            // 设置listener
            val ok = layout?.findViewById<View>(R.id.positive) as TextView
            val x = layout?.findViewById<View>(R.id.negative) as TextView
            var btnNum = 0
            if (positiveButtonClickListener != null) {
                btnNum++
                ok.text = positive
                x.setBackgroundResource(R.drawable.dw_bg_dialog_btn_l)
                ok.setOnClickListener {
                    if (positiveButtonClickListener != null) {
                        positiveButtonClickListener?.onClick(
                            dialog,
                            DialogInterface.BUTTON_POSITIVE
                        )
                    }
                }
            } else {
                ok.visibility = View.GONE
                x.setBackgroundResource(R.drawable.dw_bg_dialog_btn)
            }
            if (negativeButtonClickListener != null) {
                btnNum++
                x.text = negative
                ok.setBackgroundResource(R.drawable.dw_bg_dialog_btn_r)
                x.setOnClickListener {
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener?.onClick(
                            dialog,
                            DialogInterface.BUTTON_NEGATIVE
                        )
                    }
                }
            } else {
                x.visibility = View.GONE
                ok.setBackgroundResource(R.drawable.dw_bg_dialog_btn)
            }

            if (btnNum >= 2) {
                layout?.findViewById<View>(R.id.line)?.visibility = View.VISIBLE
            } else {
                layout?.findViewById<View>(R.id.line)?.visibility = View.GONE
            }

            // 设置cancleable
            dialog.setCancelable(cancelable)
            if (cancelListener != null) {
                dialog.setOnCancelListener(cancelListener)
            }
            dialog.setContentView(
                layout!!, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            )
            return dialog
        }

        protected fun getLayoutRes(): Int {
            return R.layout.dialog_confirm
        }

    }

}