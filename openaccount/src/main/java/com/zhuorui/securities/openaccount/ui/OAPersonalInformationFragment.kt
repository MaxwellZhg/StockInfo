package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.common.CommonCountryCodeFragment
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.databinding.FragmentOaPersonalInformationBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAPersonalInformationPresenter
import com.zhuorui.securities.openaccount.ui.view.OAPersonalInformationView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPersonalInformationViewModel
import kotlinx.android.synthetic.main.fragment_oa_personal_information.*
import me.yokeyword.fragmentation.ISupportActivity
import me.yokeyword.fragmentation.ISupportFragment

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-29 15:13
 *    desc   : 风险测评-个人信息
 */
class OAPersonalInformationFragment :
    AbsSwipeBackFragment<FragmentOaPersonalInformationBinding, OAPersonalInformationViewModel, OAPersonalInformationView, OAPersonalInformationPresenter>(),
    OAPersonalInformationView, View.OnClickListener {

    var mOptionsPicker: OptionsPickerDialog<String>? = null

    companion object {
        fun newInstance(): OAPersonalInformationFragment {
            val fragment = OAPersonalInformationFragment()
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_personal_information

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAPersonalInformationPresenter
        get() = OAPersonalInformationPresenter()

    override val createViewModel: OAPersonalInformationViewModel?
        get() = ViewModelProviders.of(this).get(OAPersonalInformationViewModel::class.java)

    override val getView: OAPersonalInformationView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mOptionsPicker = context?.let { OptionsPickerDialog(it) }
        tv_employment_status.setOnClickListener(this)
        tv_tax.setOnClickListener(this)
        tv_tax_paying.setOnClickListener(this)
        btn_per.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        emiltips?.bindEditText(et_email)
        presenter?.setDefData()
    }

    override fun setOccupation(txt: String?) {
        tv_employment_status.text = txt
    }

    override fun setTaxType(txt: String?) {
        tv_tax.text = txt
    }

    override fun setTaxState(txt: String?) {
        tv_tax_paying.text = txt
    }

    override fun setTaxNo(cardNo: String?) {
        tv_tax_number.text = cardNo
    }

    override fun getTaxState(): String? {
        return tv_tax_paying.text
    }

    override fun getTaxNo(): String? {
        return tv_tax_number.text
    }

    override fun getEmail(): String? {
        return et_email.text
    }

    override fun toNext() {
        start(OAPropertyStatusFragment.newInstance())
    }

    override fun showToast(msg: String?) {
        msg?.let { ToastUtil.instance.toast(it) }

    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_per -> {
                pop()
            }
            R.id.btn_next -> {
                if (!presenter?.checkData()!!) return
                context?.let {
                    ConfirmToCancelDialog.createWidth265Dialog(it, true, true)
                        .setMsgText(presenter?.getDialogSpanned()!!)
                        .setCancelText(R.string.cancle)
                        .setConfirmText(R.string.ensure)
                        .setCallBack(object : ConfirmToCancelDialog.CallBack {
                            override fun onCancel() {

                            }

                            override fun onConfirm() {
                                presenter?.sub()
                            }

                        }).show()
                }
            }
            R.id.tv_employment_status -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getOccupationPickerListener())
                mOptionsPicker?.setData(presenter?.occupationPickerData)
                mOptionsPicker?.setCurrentData(tv_employment_status.text)
                mOptionsPicker?.show()
            }
            R.id.tv_tax -> {
                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getTaxTypePickerListener())
                mOptionsPicker?.setData(presenter?.taxTypePickerData)
                mOptionsPicker?.setCurrentData(tv_tax.text)
                mOptionsPicker?.show()
            }
            R.id.tv_tax_paying -> {
                startForResult(CommonCountryCodeFragment.newInstance(presenter?.getCommonType()), 100)
            }
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK && requestCode == 100) {
            tv_tax_paying.text = data?.getString("str")
        }
    }

}