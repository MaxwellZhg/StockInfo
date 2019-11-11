package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import com.zhuorui.securities.market.net.response.F10ShareHolderListResponse

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/7 19:15
 *    desc   :
 */
interface CompanyBrieViewMoreView : AbsView {

    fun updateShareHolderList(data: F10ShareHolderListResponse.Data?)
}