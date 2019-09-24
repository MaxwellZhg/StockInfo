package com.zhuorui.securities.market.model

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-16 16:30
 *    desc   :
 */
interface IStocks {
    fun getIName():String
    fun getICode():String
    fun getITs():String
    fun getITsCode():String
    fun getIID():String
    fun getIType():Int
}