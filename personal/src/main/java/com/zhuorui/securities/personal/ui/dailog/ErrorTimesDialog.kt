package com.zhuorui.securities.personal.ui.dailog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import com.zhuorui.securities.personal.R
import kotlinx.android.synthetic.main.item_error_dialog_info.*
import kotlinx.android.synthetic.main.item_vrf_dialog_info.*
import kotlinx.android.synthetic.main.item_error_dialog_info.tv_attention

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/22
 * Desc:错误次数多的 弹框
 * */
class ErrorTimesDialog (context: Context,type:Int,str:String?):Dialog(context, R.style.dialog){
    init {
        when(type){
            1-> {
                setContentView(R.layout.item_vrf_dialog_info)
            }
            2->{
                setContentView(R.layout.item_error_dialog_info)
                setTipsValue(str)
            }

        }
        // 设置居中
        window!!.attributes.gravity = Gravity.CENTER
        val lp = window!!.attributes
        // 设置背景层透明度
        lp.dimAmount = 0.6f
        window!!.attributes = lp
    }

    public fun setOnclickListener(listener: View.OnClickListener){
        if (rl_complete_verify !== null){
            rl_complete_verify.setOnClickListener(listener)
        }
        if (rl_complete_psw !== null){
            rl_complete_psw.setOnClickListener(listener)
        }
    }

    fun setTipsValue(str:String?){
        tv_attention_tips.text=str
    }

}