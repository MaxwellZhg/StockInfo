package com.zhuorui.securities.market.util

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/25 15:28
 * desc   :
 */
object MathUtil {

//    /**
//     * 除法四舍五入保留两位小数
//     */
//    fun division(divisor: Double, dividend: Double): Double {
//        if (dividend <= 0) return 0.0
//        val b = BigDecimal(divisor / dividend)
//        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
//    }

    /**
     * 向上取整数
     */
    fun rounded(number: BigDecimal): BigDecimal {
        return number.setScale(0, RoundingMode.UP)
    }

    /**
     * 不做四舍五入，直接保留两位小数
     */
    fun rounded2(number: BigDecimal): BigDecimal {
        return number.setScale(2, RoundingMode.DOWN)
    }

    /**
     * 不做四舍五入，直接保留三位小数
     */
    fun rounded3(number: BigDecimal): BigDecimal {
        return number.setScale(3, RoundingMode.DOWN)
    }

    /**
     * 加法保留三位小数
     */
    fun add3(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        return rounded3(number1.add(number2))
    }

    /**
     * 减法保留三位小数
     */
    fun subtract3(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        return rounded3(number1.subtract(number2))
    }

    /**
     * 乘法保留三位小数
     */
    fun multiply3(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        return rounded3(number1.multiply(number2))
    }

    /**
     * 除法保留两位小数
     */
    fun divide2(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        if (rounded(number1).toInt() == 0 || rounded(number2).toInt() == 0) {
            return rounded2(BigDecimal.ZERO)
        }
        return number1.divide(number2, 2, RoundingMode.DOWN)
    }

    /**
     * 除法保留三位小数
     */
    fun divide3(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        if (rounded(number1).toInt() == 0 || rounded(number2).toInt() == 0) {
            return rounded3(BigDecimal.ZERO)
        }
        return number1.divide(number2, 3, RoundingMode.DOWN)
    }
}
