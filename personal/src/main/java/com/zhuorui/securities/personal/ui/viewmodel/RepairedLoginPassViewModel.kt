package com.zhuorui.securities.personal.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
class RepairedLoginPassViewModel :ViewModel(){
    var strnew= ObservableField<String>()
    var type=ObservableField<Int>()
    init {
        strnew.set("")
        type.set(0)
    }
}