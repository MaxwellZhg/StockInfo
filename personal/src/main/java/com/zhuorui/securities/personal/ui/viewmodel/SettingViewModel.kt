package com.zhuorui.securities.personal.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.personal.ui.adapter.SettingDataAdapter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:
 */
class SettingViewModel :ViewModel(){
    var adapter: MutableLiveData<SettingDataAdapter> = MutableLiveData()
}