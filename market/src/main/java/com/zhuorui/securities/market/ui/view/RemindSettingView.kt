package com.zhuorui.securities.market.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:27
 *    desc   :
 */
interface RemindSettingView : AbsView {

    /**
     * 更新股价
     */
    fun updateStockPrice(price: BigDecimal?, diffPrice: BigDecimal?, diffRate: BigDecimal?, diffState: Int)

}