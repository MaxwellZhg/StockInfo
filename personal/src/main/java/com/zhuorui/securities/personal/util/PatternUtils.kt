package com.zhuorui.securities.personal.util

import java.util.regex.Pattern

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/28
 * Desc:
 */
object PatternUtils {
    val patternZh = "^1\\d{10}\$"
    val patternOther = "^[0-9]*\$"
    val patternCode="^[0-9]\\d{5}\$"
    val patternPsw = "^(?![\\d]+\$)(?![a-zA-Z]+\$)(?![^\\da-zA-Z]+\$).{6,20}\$"
    fun patternZhPhone(phone:String):Boolean{
        val matcher = Pattern.compile(patternZh).matcher(phone)
        return  matcher.find()
    }
    fun patternOtherPhone(phone:String):Boolean{
        val matcher = Pattern.compile(patternOther).matcher(phone)
        return  matcher.find()
    }
    fun patternPhoneCode(phoneCode:String):Boolean{
        val matcher = Pattern.compile(patternCode).matcher(phoneCode)
        return  matcher.find()
    }
    fun patternLoginPassWord(pass:String):Boolean{
        val pattern = "^(?![\\d]+\$)(?![a-zA-Z]+\$)(?![^\\da-zA-Z]+\$).{6,20}\$"
        //用正则式匹配文本获取匹配器
        val matcher = Pattern.compile(pattern).matcher(pass)

        return  matcher.find()
    }
}