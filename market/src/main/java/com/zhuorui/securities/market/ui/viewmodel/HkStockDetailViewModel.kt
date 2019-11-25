package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData
import com.zhuorui.securities.market.socket.vo.StockHandicapData

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:
 */
class HkStockDetailViewModel :ViewModel(){
    var infos: MutableLiveData<MutableList<Int>> = MutableLiveData()
    var mIndexHandicapData: MutableLiveData<MutableList<IndexPonitHandicapData?>> = MutableLiveData()
}