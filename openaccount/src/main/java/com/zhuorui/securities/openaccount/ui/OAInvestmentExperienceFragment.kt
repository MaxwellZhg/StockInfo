package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaInvestmentExperienceBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAInvestmentExperiencePresenter
import com.zhuorui.securities.openaccount.ui.view.OAInvestmentExperienceView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAInvestmentExperienceViewModel
import kotlinx.android.synthetic.main.fragment_oa_investment_experience.*
import kotlinx.android.synthetic.main.fragment_oa_personal_information.*
import kotlinx.android.synthetic.main.fragment_oa_property_status.*
import kotlinx.android.synthetic.main.fragment_oa_property_status.btn_next
import kotlinx.android.synthetic.main.fragment_oa_property_status.btn_per

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-29 15:13
 *    desc   : 风险测评-投资经验
 */
class OAInvestmentExperienceFragment :
    AbsSwipeBackFragment<FragmentOaInvestmentExperienceBinding, OAInvestmentExperienceViewModel, OAInvestmentExperienceView, OAInvestmentExperiencePresenter>(),
    OAInvestmentExperienceView, View.OnClickListener {

    var mOptionsPicker: OptionsPickerDialog<String>? = null

    companion object {
        fun newInstance(): OAInvestmentExperienceFragment {
            val fragment = OAInvestmentExperienceFragment()
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_investment_experience

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAInvestmentExperiencePresenter
        get() = OAInvestmentExperiencePresenter()

    override val createViewModel: OAInvestmentExperienceViewModel?
        get() = ViewModelProviders.of(this).get(OAInvestmentExperienceViewModel::class.java)

    override val getView: OAInvestmentExperienceView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mOptionsPicker = context?.let { OptionsPickerDialog(it) }
        btn_per.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        tv_shares.setOnClickListener(this)
        tv_bond.setOnClickListener(this)
        tv_exchange_or_gold.setOnClickListener(this)
        tv_fund_or_financing.setOnClickListener(this)
        presenter?.setDefData()
    }

    override fun setInvestSharesText(time: String?) {
        tv_shares.text = time
    }

    override fun setInvestBondText(time: String?) {
        tv_bond.text = time
    }

    override fun setInvestGoldForeignText(time: String?) {
        tv_exchange_or_gold.text = time
    }

    override fun setInvestFundText(time: String?) {
        tv_fund_or_financing.text = time
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_per -> {
                pop()
            }
            R.id.btn_next -> {
                start(OAOhterNotesFragment.newInstance())
            }
            R.id.tv_shares -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getInvestSharesPickerListener())
                mOptionsPicker?.setData(presenter?.experienceTimePickerData)
                mOptionsPicker?.setCurrentData(tv_shares.text)
                mOptionsPicker?.show()
            }
            R.id.tv_bond -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getInvestBondPickerListener())
                mOptionsPicker?.setData(presenter?.experienceTimePickerData)
                mOptionsPicker?.setCurrentData(tv_bond.text)
                mOptionsPicker?.show()
            }
            R.id.tv_exchange_or_gold -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getInvestGoldForeignPickerListener())
                mOptionsPicker?.setData(presenter?.experienceTimePickerData)
                mOptionsPicker?.setCurrentData(tv_exchange_or_gold.text)
                mOptionsPicker?.show()
            }
            R.id.tv_fund_or_financing -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getInvestFundPickerListener())
                mOptionsPicker?.setData(presenter?.experienceTimePickerData)
                mOptionsPicker?.setCurrentData(tv_fund_or_financing.text)
                mOptionsPicker?.show()
            }

        }
    }
}