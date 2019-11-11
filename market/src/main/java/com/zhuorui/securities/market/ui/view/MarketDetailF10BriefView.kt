package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.model.F10DividendModel
import com.zhuorui.securities.market.model.F10ManagerModel
import com.zhuorui.securities.market.model.F10RepoModel
import com.zhuorui.securities.market.model.F10ShareHolderModel
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
        manager: List<F10ManagerModel>?,
        shareHolderChange: List<F10ShareHolderModel>?,
        dividend: List<F10DividendModel>?,
        repo: List<F10RepoModel>?
    )
}