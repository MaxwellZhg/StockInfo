package com.zhuorui.securities.market.util

import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
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
     * 加法保留两位小数
     */
    fun add2(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        return rounded2(number1.add(number2))
    }


    /**
     * 减法保留三位小数
     */
    fun subtract3(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        return rounded3(number1.subtract(number2))
    }

    /**
     * 减法保留两位小数
     */
    fun subtract2(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        return rounded2(number1.subtract(number2))
    }

    /**
     * 乘法保留两位小数
     */
    fun multiply2(number1: BigDecimal, number2: BigDecimal): BigDecimal {
        return rounded2(number1.multiply(number2))
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

    /**
     * 除法保留指定小数位
     */
    fun divide(number1: BigDecimal, number2: BigDecimal, scale: Int): BigDecimal {
        if (rounded(number1).toInt() == 0 || rounded(number2).toInt() == 0) {
            return rounded3(BigDecimal.ZERO)
        }
        return number1.divide(number2, scale, RoundingMode.DOWN)
    }


    /**
     * 转换成带有逗号的字符串
     */
    fun convertToString(number: BigDecimal): String {
        val scale = number.scale()
        val format = "%" + ",." + scale + "f"
        return format.format(number)
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    fun subZeroAndDot(s: String): String {
        var s = s
        if (s.indexOf(".") > 0) {
            s = s.replace("0+?$".toRegex(), "")//去掉多余的0
            s = s.replace("[.]$".toRegex(), "")//如最后一位是.则去掉
        }
        return s
    }

    /**
     * k代表千，M代表百万，B代表十亿
     */
    private val K = BigDecimal.valueOf(1000)
    private val M = BigDecimal.valueOf(1000000)
    private val B = BigDecimal.valueOf(1000000000)
    /**
     * 格式化数字加单位
     * @return 返回示例 10、10K、10M、10B
     */
    fun convertToUnitString(number: BigDecimal): String {
        return when {
            // 是否大于十亿
            number.compareTo(B) == 1 -> divide2(number, B).toString() + "B"
            // 是否大于百万
            number.compareTo(M) == 1 -> divide2(number, M).toString() + "M"
            // 是否大于一千
            number.compareTo(K) == 1 -> divide2(number, K).toString() + "K"
            else -> number.toString()
        }
    }

    /**
     * W代表万，Y代表亿
     */
    private val W = BigDecimal.valueOf(100000)
    private val Y = BigDecimal.valueOf(100000000)
    /**
     * 格式化数字加单位
     * @param formatType 1金额 2持股
     * @return 返回示例 金额：10、10万、10亿 持股：10、10万股、10亿股
     */
    fun convertToUnitString(number: BigDecimal, formatType: Int): String? {
        if (formatType == 1) {
            return when {
                // 是否大于亿
                number.compareTo(Y) == 1 -> divide2(number, Y).toString() + ResUtil.getString(R.string.unit_y)
                // 是否大于万
                number.compareTo(W) == 1 -> divide2(number, W).toString() + ResUtil.getString(R.string.unit_w)
                else -> rounded(number).toString()
            }
        } else if (formatType == 2) {
            return when {
                // 是否大于亿
                number.compareTo(Y) == 1 -> divide2(number, Y).toString() + ResUtil.getString(R.string.unit_stock_y)
                // 是否大于万
                number.compareTo(W) == 1 -> divide2(number, W).toString() + ResUtil.getString(R.string.unit_stock_w)
                else -> rounded(number).toString()
            }
        }

        return null
    }

    /**
     * 格式化数字为亿float
     * @param formatType 1金额 2持股
     * @return 返回示例 金额：10、10万、10亿 持股：10、10万股、10亿股
     */
    fun convertToUnitFloat(number: BigDecimal): Float? {
        if(number> BigDecimal.ZERO) {
            return when {
                // 是否大于亿
                number.compareTo(Y) == 1 -> divide2(number, Y).toFloat()
                // 是否大于万
                number.compareTo(W) == 1 -> divide2(number, W).toFloat()
                else -> rounded(number).toFloat()
            }
        }else{
            return when {
                // 是否大于亿
                number.compareTo(Y) == -1 -> divide2(number, Y).toFloat()
                // 是否大于万
                number.compareTo(W) == -1 -> divide2(number, W).toFloat()
                else -> rounded(number).toFloat()
            }
        }
        return null
    }
}
