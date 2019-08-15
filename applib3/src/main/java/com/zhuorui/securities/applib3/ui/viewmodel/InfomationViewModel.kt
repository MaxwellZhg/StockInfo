package com.zhuorui.securities.applib3.ui.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class InfomationViewModel :BaseObservable(){
    var str= ObservableField<String>()
    init {
        str.set("咨讯")
    }
}