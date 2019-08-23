package com.zhuorui.securities.infomation.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.infomation.ui.adapter.SortAdapter
import com.zhuorui.securities.infomation.ui.model.JsonBean

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class CountryDisctViewModel :ViewModel(){
    var adapter: MutableLiveData<SortAdapter> = MutableLiveData()
}