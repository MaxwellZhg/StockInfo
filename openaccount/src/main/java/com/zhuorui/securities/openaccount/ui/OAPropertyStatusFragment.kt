package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
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
            val fragment = OAPropertyStatusFragment()
            return fragment
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

    override fun setIncomeText(txt: String?) {
        tv_income.text = txt
    }

    override fun setRateText(txt: String?) {
        tv_frequency.text = txt
    }

    override fun setRiskText(txt: String?) {
        tv_risk.text = txt
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_per -> {
                pop()
            }
            R.id.btn_next -> {
                start(OAInvestmentExperienceFragment.newInstance())
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