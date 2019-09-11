package com.zhuorui.securities.personal.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/10
 * Desc:
 */
class ChangePhoneNumViewModel :ViewModel(){
 var str=ObservableField<String>()
 init {
     str.set(ResUtil.getString(R.string.get_verify_code))
 }
}