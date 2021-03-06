package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaPropertyStatusBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAPropertyStatusPresenter
import com.zhuorui.securities.openaccount.ui.view.OAPropertyStatusView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPropertyStatusViewModel
import kotlinx.android.synthetic.main.fragment_oa_personal_information.*
import kotlinx.android.synthetic.main.fragment_oa_property_status.*
import kotlinx.android.synthetic.main.fragment_oa_property_status.btn_next
import kotlinx.android.synthetic.main.fragment_oa_property_status.btn_per
import kotlinx.android.synthetic.main.layout_oa_property_status_checkbox.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-29 15:13
 *    desc   : 风险测评-财产状况
 */
class OAPropertyStatusFragment :
    AbsSwipeBackFragment<FragmentOaPropertyStatusBinding, OAPropertyStatusViewModel, OAPropertyStatusView, OAPropertyStatusPresenter>(),
    OAPropertyStatusView, View.OnClickListener {

    var mOptionsPicker: OptionsPickerDialog<String>? = null

    companion object {
        fun newInstance(): OAPropertyStatusFragment {
            return OAPropertyStatusFragment()
        }
    }


    override val layout: Int
        get() = R.layout.fragment_oa_property_status

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAPropertyStatusPresenter
        get() = OAPropertyStatusPresenter()

    override val createViewModel: OAPropertyStatusViewModel?
        get() = ViewModelProviders.of(this).get(OAPropertyStatusViewModel::class.java)

    override val getView: OAPropertyStatusView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mOptionsPicker = context?.let { OptionsPickerDialog(it) }
        tv_income.setOnClickListener(this)
        tv_frequency.setOnClickListener(this)
        tv_risk.setOnClickListener(this)
        btn_per.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        presenter?.setDefData()
    }

    override fun showToast(s: String) {
        ToastUtil.instance.toast(s)
    }

    override fun setIncomeText(txt: String?) {
        tv_income.text = txt
    }

    override fun setRateText(txt: String?) {
        tv_frequency.text = txt
    }

    override fun setRiskText(txt: String?) {
        tv_risk.text = txt
    }

    /**
     *  获取薪金box状态
     */
    override fun getSalaryBoxStatus(): Boolean? {
        return cb_salary?.isChecked
    }

    /**
     *  获取存款box状态
     */
    override fun getDepositBoxStatus(): Boolean? {
        return cb_deposit.isChecked
    }

    /**
     *  获取租金box状态
     */
    override fun getRentBoxStatus(): Boolean? {
        return cb_rent.isChecked
    }

    /**
     *  获取投资box状态
     */
    override fun getInvestmentBoxStatus(): Boolean? {
        return cb_investment.isChecked
    }

    /**
     *  获取借贷box状态
     */
    override fun getToLoanBoxStatus(): Boolean? {
        return cb_to_loan.isChecked
    }

    /**
     *  获取其他box状态
     */
    override fun getOtherBoxStatus(): Boolean? {
        return cb_other.isChecked
    }

    override fun toNext() {
        start(OAInvestmentExperienceFragment.newInstance())
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_per -> {
                pop()
            }
            R.id.btn_next -> {
                presenter?.sub()
            }
            R.id.tv_income -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getIncomePickerListener())
                mOptionsPicker?.setData(presenter?.incomePickerData)
                mOptionsPicker?.setCurrentData(tv_income.text)
                mOptionsPicker?.show()
            }
            R.id.tv_frequency -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getRatePickerListener())
                mOptionsPicker?.setData(presenter?.ratePickerData)
                mOptionsPicker?.setCurrentData(tv_frequency.text)
                mOptionsPicker?.show()
            }
            R.id.tv_risk -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getRiskPickerListener())
                mOptionsPicker?.setData(presenter?.riskPickerData)
                mOptionsPicker?.setCurrentData(tv_risk.text)
                mOptionsPicker?.show()
            }

        }
    }
}