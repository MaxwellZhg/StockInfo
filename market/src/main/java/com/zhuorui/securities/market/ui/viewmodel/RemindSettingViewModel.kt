package com.zhuorui.securities.market.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.market.ui.adapter.SettingNoticeAdapter

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:26
 *    desc   :
 */
class RemindSettingViewModel :ViewModel() {
    var adapter: MutableLiveData<SettingNoticeAdapter> = MutableLiveData()
}