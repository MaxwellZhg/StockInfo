package com.zhuorui.securities.openaccount.ui.presenter

import android.text.TextUtils
import com.zhuorui.commonwidget.dialog.DatePickerDialog
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.TimeZoneUtil.*
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.OpenInfoRequest
import com.zhuorui.securities.openaccount.net.request.SubIdentityRequest
import com.zhuorui.securities.openaccount.net.response.OpenInfoResponse
import com.zhuorui.securities.openaccount.net.response.SubIdentityResponse
import com.zhuorui.securities.openaccount.ui.view.OAConfirmDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAConfirmDocumentsViewModel
import com.zhuorui.securities.pickerview.option.OnOptionSelectedListener
import java.text.SimpleDateFormat
import java.util.*

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAConfirmDocumentsPresenter : AbsNetPresenter<OAConfirmDocumentsView, OAConfirmDocumentsViewModel>() {

    var BIRTHDAY_DATE_FORMAT: String = "yyyy年MM月dd日"
    var SERVICE_DATE_FORMAT: String = "yyyy-MM-dd"
    var genderPickerData: MutableList<String>? = null
    var genderCodeData: MutableList<Int>? = null
    var idCardValidData: MutableList<String>? = null
    var idCardValidNumDate: MutableList<Int>? = null
    var endValidPickerData: MutableList<String>? = null
    var mCardValidYear: Int = 0
    var mCardBirth: String = ""
    var mCardSex: Int = -1
    var mCardValidStartDate: String? = null
    var mCardValidEndDate: String? = null


    override fun init() {
        super.init()
        genderPickerData = ResUtil.getStringArray(R.array.gender)?.asList()?.toMutableList()
        genderCodeData = ResUtil.getIntArray(R.array.gender_code)?.asList()?.toMutableList()!!
        idCardValidData = ResUtil.getStringArray(R.array.id_card_valid)?.asList()?.toMutableList()
        idCardValidNumDate = ResUtil.getIntArray(R.array.id_card_valid_num)?.asList()?.toMutableList()
        view?.init()
    }

    /**
     * 获取开户信息
     */
    fun requestOpenInfo() {
        val request = OpenInfoRequest(transactions.createTransaction())
        Cache[IOpenAccountNet::class.java]?.getOpenInfo(request)
            ?.enqueue(Network.IHCallBack<OpenInfoResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onOpenInfoResponse(response: OpenInfoResponse) {
        // 记录开户信息
        OpenInfoManager.getInstance()?.info = response.data
        setIdCardData()
    }

    fun setIdCardData() {
        if (OpenInfoManager.getInstance()?.info == null) return
        val data: OpenAccountInfo = OpenInfoManager.getInstance()?.info!!
        //名字
        view?.setName(data.cardName)
        //性别
        mCardSex = data.cardSex!!
        val genderPos: Int? = genderCodeData?.indexOf(mCardSex)
        val gender = if (genderPos == null) "" else genderPickerData?.get(genderPos)
        view?.setGender(gender)
        //生日
        mCardBirth = timeFormat(data?.cardBirth, SERVICE_DATE_FORMAT, BIRTHDAY_DATE_FORMAT)
        view?.setBirthday(mCardBirth)
        //身份证号
        view?.setCardNo(data?.cardNo)
        //开始日期
        mCardValidStartDate = timeFormat(data?.cardValidStartDate, SERVICE_DATE_FORMAT, BIRTHDAY_DATE_FORMAT)
        view?.setCardValidStartDate(mCardValidStartDate)
        //结束日期
        initCardValidEndData(mCardValidStartDate)
        mCardValidYear = if (data?.cardValidYear != null) data?.cardValidYear!! else 0
        val pos = idCardValidNumDate?.indexOf(mCardValidYear)
        mCardValidEndDate = if (pos != null && pos != -1) endValidPickerData?.get(pos) else ""
        view?.setCardValidEndDate(mCardValidEndDate)
        //地址
        view?.setCardAddress(data?.cardAddress)

    }

    /**
     * 初始化结束日期数据
     *
     */
    fun initCardValidEndData(date: String?) {
        if (idCardValidNumDate == null || date == null) return
        val list: MutableList<Int> = idCardValidNumDate as MutableList<Int>
        val tms = Calendar.getInstance()
        tms.timeInMillis = SimpleDateFormat(BIRTHDAY_DATE_FORMAT).parse(date).time
        val y: Int = tms.get(Calendar.YEAR)
        val m: Int = tms.get(Calendar.MONTH) + 1
        val d: Int = tms.get(Calendar.DAY_OF_MONTH)
        endValidPickerData = mutableListOf()
        for ((index, e) in list.withIndex()) {
            if (e == -1) {
                endValidPickerData?.add(idCardValidData?.get(index).toString())
            } else {
                val txt = "%d年%02d月%02d日（%s）"
                endValidPickerData?.add(String.format(txt, y + e, m, d, idCardValidData?.get(index)))
            }
        }
    }


    /**
     * 生日选择监听
     *
     */
    fun getBirthdayPickerListener(): DatePickerDialog.OnDateSelectedListener {
        return object : DatePickerDialog.OnDateSelectedListener {
            override fun onDateSelected(date: String) {
                mCardBirth = date
                view?.setBirthday(date)
            }
        }
    }

    /**
     * 开始日期选择监听
     *
     */
    fun getValidStartDatePickerListener(): DatePickerDialog.OnDateSelectedListener {
        return object : DatePickerDialog.OnDateSelectedListener {
            override fun onDateSelected(date: String) {
                mCardValidStartDate = date
                view?.setCardValidStartDate(date)
                initCardValidEndData(date)
                val pos = idCardValidNumDate?.indexOf(mCardValidYear)
                mCardValidEndDate = if (pos != null && pos != -1) endValidPickerData?.get(pos) else ""
                view?.setCardValidEndDate(mCardValidEndDate)
            }

        }
    }

    /**
     * 结束日期选择监听
     *
     */
    fun getValidEndDatePickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            mCardValidEndDate = data?.get(0)
            view?.setCardValidEndDate(mCardValidEndDate)
            val pos = endValidPickerData?.indexOf(mCardValidEndDate)
            mCardValidYear = if (pos != null && pos != -1) idCardValidNumDate?.get(pos)!! else 0
        }
    }

    /**
     * 性别选择监听
     *
     */
    fun getGenderPickerListener(): OnOptionSelectedListener<String> {
        return OnOptionSelectedListener { data ->
            val gender = data?.get(0)
            view?.setGender(data?.get(0))
            val pos = genderPickerData?.indexOf(gender)
            mCardSex = if (pos != null && pos != -1) genderCodeData?.get(pos)!! else -1
        }
    }

    fun subIdentity() {
        val cardName = view?.getCardName()
        val idCardNo = view?.getIdCardNo()
        val cardAddress = view?.getCardAddress()
        val cardValidStartDate = timeFormat(mCardValidStartDate, BIRTHDAY_DATE_FORMAT, SERVICE_DATE_FORMAT)
        val cardValidEndDate =
            if (mCardValidYear == -1) "-1" else timeFormat(mCardValidEndDate, BIRTHDAY_DATE_FORMAT, SERVICE_DATE_FORMAT)
        val cardBirth = timeFormat(mCardBirth, BIRTHDAY_DATE_FORMAT, SERVICE_DATE_FORMAT)
        if (!checkData(cardName, idCardNo, cardAddress, cardValidStartDate, cardValidEndDate, cardBirth)) return
        val request = SubIdentityRequest(
            idCardNo.toString(),
            cardName.toString(),
            mCardSex,
            OpenInfoManager.getInstance()?.info?.cardNation.toString(),
            cardAddress.toString(),
            OpenInfoManager.getInstance()?.info?.cardAuthority.toString(),
            cardValidStartDate,
            cardValidEndDate,
            mCardValidYear.toString(),
            cardBirth,
            OpenInfoManager.getInstance()?.info?.id.toString(),
            transactions.createTransaction()
        )
        Cache[IOpenAccountNet::class.java]?.subIdentity(request)
            ?.enqueue(Network.IHCallBack<SubIdentityResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSubIdentityResponse(response: SubIdentityResponse) {
        // 记录开户信息
        OpenInfoManager.getInstance()?.readSubIdentityResponse(response.data)
        view?.toNext()
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        if (response.request is SubIdentityRequest) {

        }
    }

    private fun checkData(
        cardName: String?,
        idCardNo: String?,
        cardAddress: String?,
        cardValidStartDate: String?,
        cardValidEndDate: String?,
        cardBirth: String?
    ): Boolean {
        var t = ""
        if (TextUtils.isEmpty(cardName)) {
            t = ResUtil.getString(R.string.str_chinese_name) + ResUtil.getString(R.string.str_not_empty)
        } else if (mCardSex == -1) {
            t = ResUtil.getString(R.string.str_please_select) + ResUtil.getString(R.string.str_gender)
        } else if (TextUtils.isEmpty(cardBirth)) {
            t = ResUtil.getString(R.string.str_please_select) + ResUtil.getString(R.string.str_date_of_birth)
        } else if (TextUtils.isEmpty(idCardNo)) {
            t = ResUtil.getString(R.string.str_id_card_no) + ResUtil.getString(R.string.str_not_empty)
        } else if (TextUtils.isEmpty(cardValidStartDate)) {
            t =
                ResUtil.getString(R.string.str_please_select) + ResUtil.getString(R.string.str_validity_period_beginning)
        } else if (TextUtils.isEmpty(cardValidEndDate)) {
            t = ResUtil.getString(R.string.str_please_select) + ResUtil.getString(R.string.str_validity_period_end)
        } else if (TextUtils.isEmpty(cardAddress)) {
            t = ResUtil.getString(R.string.str_document_address) + ResUtil.getString(R.string.str_not_empty)
        } else if (mCardValidYear != -1 && dateCompareToday(cardValidEndDate, SERVICE_DATE_FORMAT) < 1) {
            t = ResUtil.getString(R.string.str_documents_have_expired).toString()
        }
//        else if (!isAdulthood(cardBirth,SERVICE_DATE_FORMAT)){
//        }
        return if (TextUtils.isEmpty(t)) {
            true
        } else {
            view?.showToast(t)
            false
        }
    }


}