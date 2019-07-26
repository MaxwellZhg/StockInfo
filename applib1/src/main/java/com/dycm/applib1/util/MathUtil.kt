package com.dycm.applib1.util

import java.math.BigDecimal

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/25 15:28
 * desc   :
 */
object MathUtil {

    /**
     * 除法四舍五入保留两位小数
     */
    fun division(divisor: Double, dividend: Double): Double {
        val b = BigDecimal(divisor / dividend)
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }
}
