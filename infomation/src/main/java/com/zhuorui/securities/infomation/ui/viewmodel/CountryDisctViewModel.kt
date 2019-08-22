package com.zhuorui.securities.infomation.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.infomation.ui.adapter.SortAdapter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class CountryDisctViewModel :ViewModel(){
    var adapter: MutableLiveData<SortAdapter> = MutableLiveData()
}