package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.StockSearchRequest
import com.zhuorui.securities.market.net.response.StockSearchResponse

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class StockSearchViewModel : ViewModel(){

    val keyWord = ""

    fun getTopicStockData(keyWord: String, count: Int, transaction: String) {
        val requset = StockSearchRequest(keyWord, 0, count, transaction)
        Cache[IStockNet::class.java]?.search(requset)
            ?.enqueue(Network.IHCallBack<StockSearchResponse>(requset))
    }
}