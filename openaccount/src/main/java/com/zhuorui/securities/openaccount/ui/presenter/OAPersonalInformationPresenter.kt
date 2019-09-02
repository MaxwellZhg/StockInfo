package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.R2.array.gender
import com.zhuorui.securities.openaccount.ui.view.OAPersonalInformationView
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPersonalInformationViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAPersonalInformationPresenter : AbsPresenter<OAPersonalInformationView, OAPersonalInformationViewModel>() {

    var occupationPickerData: MutableList<String>? = null//就业状态数据
    var occupationCode: MutableList<Int>? = null//就业状态code
    var taxTypePickerData: MutableList<String>? = null//
    var taxTypeCode: MutableList<Int>? = null//


    override fun init() {
        super.init()
        occupationPickerData = ResUtil.getStringArray(R.array.occupation)?.asList()?.toMutableList()
        occupationCode = ResUtil.getIntArray(R.array.occupation_code)?.asList()?.toMutableList()
        taxTypePickerData = ResUtil.getStringArray(R.array.tax_type)?.asList()?.toMutableList()
        taxTypeCode = ResUtil.getIntArray(R.array.tax_type_code)?.asList()?.toMutableList()
    }

    fun setDefData() {
        view?.setOccupation(occupationPickerData?.get(1))
        view?.setTaxType(taxTypePickerData?.get(0))
        view?.setTaxState(ResUtil.getString(R.string.str_china))
    }

    /**
     * 就业状态选择监听
     *
     */
    fun getOccupationPickerListener(): OnOptionSelectedListener<String> {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setOccupation(data?.get(0))
            }
        }
    }

    /**
     * 税务信息选择监听
     *
     */
    fun getTaxTypePickerListener(): OnOptionSelectedListener<String> {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setTaxType(data?.get(0))
            }
        }
    }



}