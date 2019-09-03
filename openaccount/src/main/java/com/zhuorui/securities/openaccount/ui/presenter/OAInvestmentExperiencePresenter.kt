package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.ui.view.OAInvestmentExperienceView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAInvestmentExperienceViewModel
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAInvestmentExperiencePresenter : AbsPresenter<OAInvestmentExperienceView, OAInvestmentExperienceViewModel>() {
    var experienceTimePickerData: MutableList<String>? = null
    var experienceTimeCode: MutableList<Int>? = null
    var mShares:String ?= null
    var mBond:String ?= null
    var mGoldForeign:String ?= null
    var mFund:String ?= null

    override fun init() {
        super.init()
        experienceTimePickerData = ResUtil.getStringArray(R.array.experience_time)?.asList()?.toMutableList()
        experienceTimeCode = ResUtil.getIntArray(R.array.experience_time_code)?.asList()?.toMutableList()
    }

    fun setDefData(){
        val time = experienceTimePickerData?.get(2)
        mShares = time
        mBond = time
        mGoldForeign = time
        mFund = time
        view?.setInvestSharesText(mShares)
        view?.setInvestBondText(mBond)
        view?.setInvestGoldForeignText(mGoldForeign)
        view?.setInvestFundText(mFund)
    }

    fun getInvestSharesPickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mShares = data?.get(0)
            view?.setInvestSharesText(mShares)
        }
    }

    fun getInvestBondPickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mBond = data?.get(0)
            view?.setInvestBondText(mBond)
        }
    }

    fun getInvestGoldForeignPickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mGoldForeign = data?.get(0)
            view?.setInvestGoldForeignText(mGoldForeign)
        }
    }

    fun getInvestFundPickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mFund = data?.get(0)
            view?.setInvestFundText(mFund)
        }
    }

    fun sub(){
        var shares = experienceTimePickerData?.indexOf(mShares)?.let { experienceTimeCode?.get(it) }
        var bond = experienceTimePickerData?.indexOf(mBond)?.let { experienceTimeCode?.get(it) }
        var goldForeign = experienceTimePickerData?.indexOf(mGoldForeign)?.let { experienceTimeCode?.get(it) }
        var fund = experienceTimePickerData?.indexOf(mFund)?.let { experienceTimeCode?.get(it) }
        OpenInfoManager.getInstance()?.info?.investShares = shares
        OpenInfoManager.getInstance()?.info?.investBond = bond
        OpenInfoManager.getInstance()?.info?.investGoldForeign = goldForeign
        OpenInfoManager.getInstance()?.info?.investFund = fund
        view?.toNext()
    }


}