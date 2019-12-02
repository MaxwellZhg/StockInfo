package com.zhuorui.securities.openaccount.ui.presenter

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.zhuorui.commonwidget.common.CommonEnum
import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.ui.view.OAPersonalInformationView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAPersonalInformationViewModel
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAPersonalInformationPresenter : AbsPresenter<OAPersonalInformationView, OAPersonalInformationViewModel>() {

    var occupationPickerData: MutableList<String>? = null//就业状态数据
    var occupationCode: MutableList<Int>? = null//就业状态code
    var taxTypePickerData: MutableList<String>? = null//
    var taxTypeCode: MutableList<Int>? = null//
    var mOccupation: String? = null
    var mTaxType: String? = null


    override fun init() {
        super.init()
        occupationPickerData = ResUtil.getStringArray(R.array.occupation)?.asList()?.toMutableList()
        occupationCode = ResUtil.getIntArray(R.array.occupation_code)?.asList()?.toMutableList()
        taxTypePickerData = ResUtil.getStringArray(R.array.tax_type)?.asList()?.toMutableList()
        taxTypeCode = ResUtil.getIntArray(R.array.tax_type_code)?.asList()?.toMutableList()
    }

    fun setDefData() {
        mOccupation = occupationPickerData?.get(1)
        view?.setOccupation(mOccupation)
        mTaxType = taxTypePickerData?.get(0)
        view?.setTaxType(mTaxType)
        view?.setTaxState(ResUtil.getString(R.string.str_china))
        view?.setTaxNo(OpenInfoManager.getInstance()?.info?.cardNo)
    }

    /**
     * 就业状态选择监听
     *
     */
    fun getOccupationPickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mOccupation = data?.get(0)
            view?.setOccupation(mOccupation)
        }
    }

    /**
     * 税务信息选择监听
     *
     */
    fun getTaxTypePickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mTaxType = data?.get(0)
            view?.setTaxType(mTaxType)
        }
    }

    fun checkData(): Boolean {
        var msg: String? = ""
        if (TextUtils.isEmpty(view?.getEmail())) {
            msg = ResUtil.getString(R.string.str_email_address) + ResUtil.getString(R.string.str_not_empty)
        } else if (TextUtils.isEmpty(view?.getTaxNo())) {
            msg = ResUtil.getString(R.string.str_tax_number) + ResUtil.getString(R.string.str_not_empty)
        } else if (TextUtils.isEmpty(view?.getTaxState())) {
            msg =
                ResUtil.getString(R.string.str_please_select) + ResUtil.getString(R.string.str_tax_paying_countries_regions)
        }
        if (TextUtils.isEmpty(msg)) {
            return true
        }
        view?.showToast(msg)
        return false
    }

    fun getDialogSpanned(): Spannable? {
        val email = view?.getEmail()
        val txt = ResUtil.getString(R.string.email_tips)
        val spannable = SpannableString(txt + email)
        spannable.setSpan(
            ForegroundColorSpan(ResUtil.getColor(R.color.color_1A6ED2)!!),
            txt?.length!!,
            spannable.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    fun sub() {
        val occupation = occupationPickerData?.indexOf(mOccupation)?.let { occupationCode?.get(it) }
        val taxType = taxTypePickerData?.indexOf(mTaxType)?.let { taxTypeCode?.get(it) }
        OpenInfoManager.getInstance().info?.occupation = occupation
        OpenInfoManager.getInstance().info?.taxType = taxType
        OpenInfoManager.getInstance().info?.taxState = "86"//中国内地
        OpenInfoManager.getInstance().info?.mailbox = view?.getEmail()
        OpenInfoManager.getInstance().info?.taxNumber = view?.getTaxNo()
        view?.toNext()
    }

    fun getCommonType(): CommonEnum? {
        return if (taxTypePickerData?.indexOf(mTaxType) == 0) CommonEnum.SINGLE else CommonEnum.ALL
    }


}