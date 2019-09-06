package com.zhuorui.securities.openaccount.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.openaccount.adapter.HelpCenterInfoAdapter

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/5
 * Desc:
 */
class OAHelpCenterInfoViewModel :ViewModel(){
    var adapter: MutableLiveData<HelpCenterInfoAdapter> = MutableLiveData()
}