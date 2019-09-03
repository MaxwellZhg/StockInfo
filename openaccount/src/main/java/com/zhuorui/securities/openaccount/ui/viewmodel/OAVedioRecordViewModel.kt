package com.zhuorui.securities.openaccount.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/27
 * Desc:
 */
class OAVedioRecordViewModel : ViewModel() {

    val verifyCode: MutableLiveData<String> = MutableLiveData()

    init {
        verifyCode.value = "----"
    }
}