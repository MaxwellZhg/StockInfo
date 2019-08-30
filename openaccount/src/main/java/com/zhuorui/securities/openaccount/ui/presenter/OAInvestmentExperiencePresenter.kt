package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.ui.view.OAInvestmentExperienceView
import com.zhuorui.securities.openaccount.ui.view.OAPersonalInformationView
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAInvestmentExperienceViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPersonalInformationViewModel
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAInvestmentExperiencePresenter : AbsPresenter<OAInvestmentExperienceView, OAInvestmentExperienceViewModel>() {
    var experienceTimePickerData: MutableList<String>? = null
    override fun init() {
        super.init()
        experienceTimePickerData = ResUtil.getStringArray(R.array.experience_time)?.asList()?.toMutableList()
    }

    fun setDefData(){
        val time = experienceTimePickerData?.get(2)
        view?.setInvestSharesText(time)
        view?.setInvestBondText(time)
        view?.setInvestGoldForeignText(time)
        view?.setInvestFundText(time)
    }

    fun getInvestSharesPickerListener(): OnOptionSelectedListener<String> {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setInvestSharesText(data?.get(0))
            }
        }
    }

    fun getInvestBondPickerListener(): OnOptionSelectedListener<String> {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setInvestBondText(data?.get(0))
            }
        }
    }
    fun getInvestGoldForeignPickerListener(): OnOptionSelectedListener<String> {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setInvestGoldForeignText(data?.get(0))
            }
        }
    }
    fun getInvestFundPickerListener(): OnOptionSelectedListener<String> {
        return object : OnOptionSelectedListener<String> {
            override fun onOptionSelected(data: MutableList<String>?) {
                view?.setInvestFundText(data?.get(0))
            }
        }
    }

}