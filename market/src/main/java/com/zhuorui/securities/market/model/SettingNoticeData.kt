package com.zhuorui.securities.market.model

import androidx.annotation.DrawableRes

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/24
 * Desc:
 */
class SettingNoticeData (@DrawableRes res: Int, str: String?, content: String, isSelect: Boolean,showtips:Boolean,stockInfo: StockMarketInfo?):ISettingNotice {

    var res: Int? = res
    var str: String? = str
    var content: String? = content
    var isSelect: Boolean? = isSelect
    var showtips: Boolean? = showtips
    var stockInfo:StockMarketInfo?=stockInfo

    override fun setShowTips(showtips: Boolean) {
        this.showtips=showtips
    }
    override fun setEtContent(str: String?) {
        this.content=str
    }
    override fun setIsSelect(isSelect: Boolean) {
       this.isSelect=isSelect
    }

    override fun toString(): String {
        return "SettingNoticeData(res=$res, str=$str, content=$content, isSelect=$isSelect)"
    }


}