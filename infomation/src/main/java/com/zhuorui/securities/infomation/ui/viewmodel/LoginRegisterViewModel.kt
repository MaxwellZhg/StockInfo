package com.zhuorui.securities.infomation.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.infomation.R

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/20
 * Desc:
 */
class LoginRegisterViewModel : ViewModel() {
    var state = ObservableField<Int>()
    var str=ObservableField<String>()
    init {
        state.set(1)
        str.set(ResUtil.getString(R.string.send_verification_code))
    }
}