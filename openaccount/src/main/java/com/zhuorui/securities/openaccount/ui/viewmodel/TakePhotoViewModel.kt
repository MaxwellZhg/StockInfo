package com.zhuorui.securities.openaccount.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/9/2 18:20
 *    desc   :
 */
class TakePhotoViewModel : ViewModel() {

    // 是否拍摄了照片
    val takePhotoed: MutableLiveData<Boolean> = MutableLiveData()

    init {
        takePhotoed.value = false
    }
}