package com.zhuorui.securities.infomation.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LoginRegisterViewModel : ViewModel() {
    var state = ObservableField<Int>()

    init {
        state.set(1)
    }
}