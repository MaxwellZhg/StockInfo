package com.zhuorui.securities.personal.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class SettingPswViewModel :ViewModel(){
    var strnews=ObservableField<String>()
    var strensure=ObservableField<String>()
    init {
        strnews.set("")
        strensure.set("")
    }
}