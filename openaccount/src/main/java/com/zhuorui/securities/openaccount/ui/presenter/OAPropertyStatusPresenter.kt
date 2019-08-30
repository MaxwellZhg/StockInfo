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
    var ratePickerData: MutableList<String>? = null //交易频率
    var riskPickerData: MutableList<String>? = null//风险承受能力

    override fun init() {
        super.init()
        incomePickerData = ResUtil.getStringArray(R.array.income_data)?.asList()?.toMutableList()
        ratePickerData = ResUtil.getStringArray(R.array.rate_data)?.asList()?.toMutableList()
        riskPickerData = ResUtil.getStringArray(R.array.risk_data)?.asList()?.toMutableList()
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