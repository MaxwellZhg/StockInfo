package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.ui.view.OAPersonalInformationView
import com.zhuorui.securities.openaccount.ui.view.OAPropertyStatusView
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPersonalInformationViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPropertyStatusViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAPropertyStatusPresenter : AbsPresenter<OAPropertyStatusView, OAPropertyStatusViewModel>() {

    var incomePickerData: MutableList<String>? = null //财产状况
    var incomeCode: MutableList<Int>? = null //财产状况Code
    var ratePickerData: MutableList<String>? = null //交易频率
    var rateCode: MutableList<Int>? = null //交易频率Code
    var riskPickerData: MutableList<String>? = null//风险承受能力
    var riskCode: MutableList<Int>? = null //风险承受能力Code

    override fun init() {
        super.init()
        incomePickerData = ResUtil.getStringArray(R.array.income_data)?.asList()?.toMutableList()
        incomeCode = ResUtil.getIntArray(R.array.income_code)?.asList()?.toMutableList()
        ratePickerData = ResUtil.getStringArray(R.array.rate_data)?.asList()?.toMutableList()
        rateCode = ResUtil.getIntArray(R.array.rate_code)?.asList()?.toMutableList()
        riskPickerData = ResUtil.getStringArray(R.array.risk_data)?.asList()?.toMutableList()
        riskCode = ResUtil.getIntArray(R.array.risk_code)?.asList()?.toMutableList()
    }

    fun setDefData(){
        view?.setIncomeText(incomePickerData?.get(2))
        view?.setRateText(ratePickerData?.get(0))
        view?.setRiskText(ratePickerData?.get(1))
    }


    fun getIncomePickerListener(): OnOptionSelectedListener<String>? {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setIncomeText(data?.get(0))
            }
        }
    }

    fun getRatePickerListener(): OnOptionSelectedListener<String>? {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setRateText(data?.get(0))
            }
        }
    }

    fun getRiskPickerListener(): OnOptionSelectedListener<String>? {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setRiskText(data?.get(0))
            }
        }
    }


}