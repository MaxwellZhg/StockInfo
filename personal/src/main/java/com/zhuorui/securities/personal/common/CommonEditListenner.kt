package com.zhuorui.securities.personal.common

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.zhuorui.commonwidget.StateButton
import com.zhuorui.securities.personal.util.PatternUtils
import kotlinx.android.synthetic.main.forget_psw_fragment.*
import kotlinx.android.synthetic.main.login_and_register_fragment.*
import kotlinx.android.synthetic.main.rest_psw_fragment.*
import java.util.regex.Pattern

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/12/6
 * Desc:
 */
class CommonEditListenner  constructor(editpass:EditText,editphone:EditText,tv_btn:StateButton,code:String,type:Int): TextWatcher{
    private var pre:String =""
    private var current:String=""
    private var editpass =editpass
    private var editphone= editphone
    private var tv_btn = tv_btn
    private var code = code
    private var type = type
    override fun afterTextChanged(s: Editable?) {
        if(patternLoginChar(s.toString().split(pre)[0])){
            editpass.setText(s.toString())
        }else{
            editpass.setText(pre)
        }
        if (s.toString().isNotEmpty()) {
            s?.toString()?.trim()?.let {
                if(type==1) {
                    if (!TextUtils.isEmpty(s.toString()) && !TextUtils.isEmpty(editphone.text.toString())) {
                        if(code  == "+86"){
                            tv_btn.isEnabled = PatternUtils.patternZhPhone(editphone.text.toString())&&PatternUtils.patternPhoneCode(editpass.text.toString())
                        }else{
                            tv_btn.isEnabled == PatternUtils.patternOtherPhone(editphone.text.toString())&& PatternUtils.patternPhoneCode(editpass.text.toString())
                        }
                    } else {
                        tv_btn.isEnabled = false
                    }
                }else{
                    if(!TextUtils.isEmpty(s.toString())&&!TextUtils.isEmpty(editphone.text.toString())){
                        tv_btn.isEnabled = PatternUtils.patternLoginPassWord(editphone.text.toString())&&PatternUtils.patternLoginPassWord(editpass.text.toString())
                    }else{
                        tv_btn.isEnabled=false
                    }
                }
            }
        } else {
            tv_btn.isEnabled=false
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
         pre=s.toString()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    fun patternLoginChar(pass:String):Boolean{
        val pattern = "[\\da-zA-Z !\"#\$%&'()*+,-./:;<=>?@\\^_`{|}~]+\$"
        //用正则式匹配文本获取匹配器
        val matcher = Pattern.compile(pattern).matcher(pass)

        return  matcher.find()
    }

}