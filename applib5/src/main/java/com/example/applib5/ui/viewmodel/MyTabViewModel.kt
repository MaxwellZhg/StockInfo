package com.example.applib5.ui.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class MyTabViewModel :BaseObservable(){
    var str=ObservableField<String>()
    init {
        str.set("我的")
    }
}