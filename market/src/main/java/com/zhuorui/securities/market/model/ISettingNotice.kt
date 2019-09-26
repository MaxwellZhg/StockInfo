package com.zhuorui.securities.market.model

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/25
 * Desc:
 */
interface ISettingNotice{
    fun setEtContent(str:String?)
    fun setShowTips(showtips:Boolean)
    fun setIsSelect(isSelect:Boolean)
}