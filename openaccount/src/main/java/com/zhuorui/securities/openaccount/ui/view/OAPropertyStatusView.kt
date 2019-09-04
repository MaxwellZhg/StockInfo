package com.zhuorui.securities.openaccount.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/26
 * Desc:
 */
interface OAPropertyStatusView :AbsView{
    fun setIncomeText(get: String?)
    fun setRateText(get: String?)
    fun setRiskText(get: String?)
    fun toNext()
    /**
     *  获取薪金box状态
     */
    fun getSalaryBoxStatus():Boolean?
    /**
     *  获取存款box状态
     */
    fun getDepositBoxStatus():Boolean?
    /**
     *  获取租金box状态
     */
    fun getRentBoxStatus():Boolean?
    /**
     *  获取投资box状态
     */
    fun getInvestmentBoxStatus():Boolean?
    /**
     *  获取借贷box状态
     */
    fun getToLoanBoxStatus():Boolean?
    /**
     *  获取其他box状态
     */
    fun getOtherBoxStatus():Boolean?

    fun showToast(s: String)

}