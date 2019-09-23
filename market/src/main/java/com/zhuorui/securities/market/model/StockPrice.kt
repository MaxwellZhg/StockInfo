package com.zhuorui.securities.market.model

import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/23 11:33
 *    desc   : 股票最小变动价位
 */
object StockPrice {


    fun getMinChangesPrice(number: BigDecimal): BigDecimal {
        // 0.01 - 0.25
        if ((number.compareTo(BigDecimal.valueOf(0.01)) == 0 || number.compareTo(BigDecimal.valueOf(0.01)) == 1) && number.compareTo(
                BigDecimal.valueOf(0.25)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.001)
        }
        // 0.25 - 0.50
        else if ((number.compareTo(BigDecimal.valueOf(0.25)) == 0 || number.compareTo(BigDecimal.valueOf(0.25)) == 1) && number.compareTo(
                BigDecimal.valueOf(0.50)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.005)
        }
        // 0.50 - 10.00
        else if ((number.compareTo(BigDecimal.valueOf(0.50)) == 0 || number.compareTo(BigDecimal.valueOf(0.50)) == 1) && number.compareTo(
                BigDecimal.valueOf(10.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.010)
        }
        // 10.00 - 20.00
        else if ((number.compareTo(BigDecimal.valueOf(10.00)) == 0 || number.compareTo(BigDecimal.valueOf(10.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(20.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.020)
        }
        // 20.00 - 100.00
        else if ((number.compareTo(BigDecimal.valueOf(20.00)) == 0 || number.compareTo(BigDecimal.valueOf(20.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(100.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.050)
        }
        // 100.00 - 200.00
        else if ((number.compareTo(BigDecimal.valueOf(100.00)) == 0 || number.compareTo(BigDecimal.valueOf(100.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(200.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.100)
        }
        // 200.00 - 500.00
        else if ((number.compareTo(BigDecimal.valueOf(200.00)) == 0 || number.compareTo(BigDecimal.valueOf(200.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(500.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.200)
        }
        // 500.00 - 1000.00
        else if ((number.compareTo(BigDecimal.valueOf(500.00)) == 0 || number.compareTo(BigDecimal.valueOf(500.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(1000.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(0.500)
        }
        // 1000.00 - 2000.00
        else if ((number.compareTo(BigDecimal.valueOf(1000.00)) == 0 || number.compareTo(BigDecimal.valueOf(1000.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(2000.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(1.000)
        }
        // 2000.00 - 5000.00
        else if ((number.compareTo(BigDecimal.valueOf(2000.00)) == 0 || number.compareTo(BigDecimal.valueOf(2000.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(5000.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(2.000)
        }
        // 5000.00 - 9995.00
        else if ((number.compareTo(BigDecimal.valueOf(5000.00)) == 0 || number.compareTo(BigDecimal.valueOf(5000.00)) == 1) && number.compareTo(
                BigDecimal.valueOf(9995.00)
            ) == -1
        ) {
            return BigDecimal.valueOf(5.000)
        }
        return number
    }
}