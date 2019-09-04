package com.zhuorui.securities.openaccount.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/26
 * Desc:
 */
interface OAPersonalInformationView : AbsView {
    fun setOccupation(get: String?)
    fun setTaxType(get: String?)
    fun setTaxState(string: String?)
    fun toNext()
    fun getEmail(): String?
    fun getTaxNo(): String?
    fun showToast(msg: String?)
    fun setTaxNo(cardNo: String?)
}