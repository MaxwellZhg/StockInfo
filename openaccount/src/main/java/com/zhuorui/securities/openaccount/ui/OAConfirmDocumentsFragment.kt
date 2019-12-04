package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.AndroidBug5497Workaround
import com.zhuorui.commonwidget.dialog.DatePickerDialog
import com.zhuorui.commonwidget.dialog.OptionsPickerDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.R2.id.*
import com.zhuorui.securities.openaccount.databinding.FragmentOaConfirmDocumentsBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAConfirmDocumentsPresenter
import com.zhuorui.securities.openaccount.ui.view.OAConfirmDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAConfirmDocumentsViewModel
import kotlinx.android.synthetic.main.fragment_oa_confirm_documents.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-23 14:09
 *    desc   : 确认身份信息
 */
class OAConfirmDocumentsFragment :
    AbsSwipeBackNetFragment<FragmentOaConfirmDocumentsBinding, OAConfirmDocumentsViewModel, OAConfirmDocumentsView, OAConfirmDocumentsPresenter>(),
    OAConfirmDocumentsView, View.OnClickListener {

    var mDatePicker: DatePickerDialog? = null
    var mOptionsPicker: OptionsPickerDialog<String>? = null
    var mAndroidBug5497Workaround: AndroidBug5497Workaround? = null


    companion object {
        fun newInstance(): OAConfirmDocumentsFragment {
            return OAConfirmDocumentsFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_oa_confirm_documents

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: OAConfirmDocumentsPresenter
        get() = OAConfirmDocumentsPresenter()

    override val createViewModel: OAConfirmDocumentsViewModel?
        get() = ViewModelProviders.of(this).get(OAConfirmDocumentsViewModel::class.java)

    override val getView: OAConfirmDocumentsView
        get() = this

    override fun setBirthday(date: String?) {
//        et_birthday.text = date
    }

    override fun setName(name: String?) {
        et_cn_name.text = name
    }

    override fun setGender(gender: String?) {
        //et_gender.text = gender
    }

    override fun setCardNo(cardNo: String?) {
        et_idcard_no.text = cardNo
    }

    override fun setCardValidStartDate(date: String?) {
//        et_s_expiry.text = date
    }

    override fun setCardValidEndDate(date: String?) {
//        et_e_expiry.text = date
    }

    override fun setCardAddress(address: String?) {
        et_address.text = address
    }

    override fun getCardName(): String? {
        return et_cn_name.text
    }

    override fun getIdCardNo(): String? {
        return et_idcard_no.text
    }

    override fun getCardAddress(): String? {
        return et_address.text
    }

    override fun showToast(t: String) {
        ToastUtil.instance.toast(t)
    }


    override fun init() {
        btn_per.setOnClickListener(this)
        btn_next.setOnClickListener(this)
//        et_gender.setOnClickListener(this)
//        et_birthday.setOnClickListener(this)
//        et_s_expiry.setOnClickListener(this)
//        et_e_expiry.setOnClickListener(this)
        mDatePicker = context?.let { DatePickerDialog(it) }
        mOptionsPicker = context?.let { OptionsPickerDialog(it) }
        mAndroidBug5497Workaround = AndroidBug5497Workaround(root_layout)
        mAndroidBug5497Workaround?.addOnGlobalLayoutListener()
    }

    /**
     * 下一步
     */
    override fun toNext() {
//        start(OATakeBankCradPhotoFragment.newInstance())
        start(OABiopsyFragment.newInstance())
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter?.requestOpenInfo()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btn_per -> {
                pop()
            }
            btn_next -> {
                presenter?.subIdentity()
            }
//            et_birthday -> {
//                mDatePicker?.setOnDateSelectedListener(presenter?.getBirthdayPickerListener())
//                mDatePicker?.setCurrentData(et_birthday.text, presenter?.BIRTHDAY_DATE_FORMAT)
//                mDatePicker?.show()
//            }
//            et_gender -> {
//                mOptionsPicker?.setData(presenter?.genderPickerData)
//                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getGenderPickerListener())
//                mOptionsPicker?.setCurrentData(et_gender.text)
//                mOptionsPicker?.show()
//            }
//            et_s_expiry -> {
//                mDatePicker?.setOnDateSelectedListener(presenter?.getValidStartDatePickerListener())
//                mDatePicker?.setCurrentData(et_s_expiry.text, presenter?.BIRTHDAY_DATE_FORMAT)
//                mDatePicker?.show()
//            }
//            et_e_expiry -> {
//                if (presenter?.endValidPickerData == null) {
//                    ToastUtil.instance.toast(R.string.str_please_select_start_date)
//                    return
//                }
//                mOptionsPicker?.setData(presenter?.endValidPickerData)
//                mOptionsPicker?.setOnOptionSelectedListener(presenter?.getValidEndDatePickerListener())
//                mOptionsPicker?.setCurrentData(et_e_expiry.text)
//                mOptionsPicker?.show()
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAndroidBug5497Workaround?.removeOnGlobalLayoutListener()
    }
}