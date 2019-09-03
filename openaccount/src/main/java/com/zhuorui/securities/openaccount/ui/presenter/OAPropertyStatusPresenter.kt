package com.zhuorui.securities.openaccount.ui.presenter

import android.text.TextUtils
import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.R2.array.occupation
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
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
    var mIncome: String? = null
    var mRate: String? = null
    var mRisk: String? = null


    override fun init() {
        super.init()
        incomePickerData = ResUtil.getStringArray(R.array.income_data)?.asList()?.toMutableList()
        incomeCode = ResUtil.getIntArray(R.array.income_code)?.asList()?.toMutableList()
        ratePickerData = ResUtil.getStringArray(R.array.rate_data)?.asList()?.toMutableList()
        rateCode = ResUtil.getIntArray(R.array.rate_code)?.asList()?.toMutableList()
        riskPickerData = ResUtil.getStringArray(R.array.risk_data)?.asList()?.toMutableList()
        riskCode = ResUtil.getIntArray(R.array.risk_code)?.asList()?.toMutableList()
    }

    fun setDefData() {
        mIncome = incomePickerData?.get(2)
        view?.setIncomeText(mIncome)
        mRate = ratePickerData?.get(0)
        view?.setRateText(mRate)
        mRisk = riskPickerData?.get(1)
        view?.setRiskText(mRisk)
    }

    fun getIncomePickerListener(): OnOptionSelectedListener<String>? {
        return OnOptionSelectedListener { data ->
            mIncome = data?.get(0)
            view?.setIncomeText(mIncome)
        }
    }

    fun getRatePickerListener(): OnOptionSelectedListener<String>? {
        return OnOptionSelectedListener { data ->
            mRate = data?.get(0)
            view?.setRateText(mRate)
        }
    }

    fun getRiskPickerListener(): OnOptionSelectedListener<String>? {
        return OnOptionSelectedListener { data ->
            mRisk = data?.get(0)
            view?.setRiskText(mRisk)
        }
    }

    fun sub() {
        val capitalSource = getCapitalSource()
        if (TextUtils.isEmpty(capitalSource)){
            view?.showToast(ResUtil.getString(R.string.str_please_select)+ResUtil.getString(R.string.str_sources_funds))
            return
        }
        val income = incomePickerData?.indexOf(mIncome)?.let { incomeCode?.get(it) }
        val rate = ratePickerData?.indexOf(mRate)?.let { rateCode?.get(it) }
        val risk = riskPickerData?.indexOf(mRisk)?.let { riskCode?.get(it) }
        OpenInfoManager.getInstance()?.info?.income = income
        OpenInfoManager.getInstance()?.info?.rate = rate
        OpenInfoManager.getInstance()?.info?.risk = risk
        OpenInfoManager.getInstance()?.info?.capitalSource = getCapitalSource()
        view?.toNext()
    }

    private fun getCapitalSource(): String? {
        var capitalSource = StringBuffer()
        if (view?.getSalaryBoxStatus()!!) {
            capitalSource.append(",")
            capitalSource.append("1")
        }
        if (view?.getDepositBoxStatus()!!) {
            capitalSource.append(",")
            capitalSource.append("2")
        }
        if (view?.getRentBoxStatus()!!) {
            capitalSource.append(",")
            capitalSource.append("3")
        }
        if (view?.getInvestmentBoxStatus()!!) {
            capitalSource.append(",")
            capitalSource.append("4")
        }
        if (view?.getToLoanBoxStatus()!!) {
            capitalSource.append(",")
            capitalSource.append("5")
        }
        if (view?.getOtherBoxStatus()!!) {
            capitalSource.append(",")
            capitalSource.append("6")
        }
        val s = capitalSource.toString()
        return if (s.length > 1) s.substring(1) else s
    }


}