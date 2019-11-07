package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.net.response.F10BrieResponse

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/6 13:54
 *    desc   :
 */
interface MarketDetailF10BriefView : AbsView {

    /**
     * 更新简况信息
     * @param company 公司简介
     * @param manager 高管信息
     * @param shareHolderChange 股东变动
     * @param dividend 分红送配
     * @param repo 公司回购
     */
    fun updateBrieInfo(
        company: F10BrieResponse.Company?,
        manager: List<F10BrieResponse.Manager>?,
        shareHolderChange: List<F10BrieResponse.ShareHolderChange>?,
        dividend: List<F10BrieResponse.Dividend>?,
        repo: List<F10BrieResponse.Repo>?
    )
}