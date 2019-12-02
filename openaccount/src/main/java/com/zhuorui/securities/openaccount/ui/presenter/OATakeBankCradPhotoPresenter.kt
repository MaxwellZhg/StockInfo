package com.zhuorui.securities.openaccount.ui.presenter

import android.graphics.Bitmap
import android.text.TextUtils
import com.zhuorui.securities.alioss.service.OssService
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.GetJsonDataUtil
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.BankCardVerificationRequest
import com.zhuorui.securities.openaccount.net.request.BankOrcRequest
import com.zhuorui.securities.openaccount.net.response.BankCardVerificationResponse
import com.zhuorui.securities.openaccount.net.response.BankOrcResponse
import com.zhuorui.securities.openaccount.ui.view.OATakeBankCradPhotoView
import com.zhuorui.securities.openaccount.ui.viewmodel.OATakeBankCradPhotoViewModel
import com.zhuorui.securities.base2app.util.Base64Enum
import com.zhuorui.securities.base2app.util.FileToBase64Util
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/26 17:14
 *    desc   :
 */
class OATakeBankCradPhotoPresenter : AbsNetPresenter<OATakeBankCradPhotoView, OATakeBankCradPhotoViewModel>() {


    override fun init() {
        super.init()
    }

    var bankList: MutableList<String>? = null

    /**
     * 银行卡OCR识别
     */
    fun bankOcr(path: String?) {
        Observable.create(ObservableOnSubscribe<ByteArray> { emitter ->
            emitter.onNext(FileToBase64Util.getSmallBitmap(FileToBase64Util.getSmallBitmap(path)))
            emitter.onComplete()
        }).flatMap {
            val oss = OpenInfoManager.getInstance().getOssService(view?.getContext()!!)
            oss.getPutObjectObservable(oss.createUpImageName(".jpg"), it)
        }.flatMap { t -> getBankOcrObservable(t) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view?.showUpLoading() }
            .doFinally { view?.hideUpLoading() }
            .subscribe(getSuccessConsumer(), getErrorConsumer())
    }

    /**
     * 银行卡OCR识别
     */
    fun bankOcr(bitmap: Bitmap?) {
        Observable.create(ObservableOnSubscribe<ByteArray> { emitter ->
            emitter.onNext(FileToBase64Util.getSmallBitmap(bitmap))
            emitter.onComplete()
        }).flatMap {
            val oss = OpenInfoManager.getInstance().getOssService(view?.getContext()!!)
            oss.getPutObjectObservable(oss.createUpImageName(".jpg"), it)
        }.flatMap { t -> getBankOcrObservable(t) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view?.showUpLoading() }
            .doFinally { view?.hideUpLoading() }
            .subscribe(getSuccessConsumer(), getErrorConsumer())
    }

    /**
     * 银行卡OCR识别请求
     */
    fun getBankOcrObservable(bankCardPhotoUrl: String): Observable<BankOrcResponse> {
        val id: String = OpenInfoManager.getInstance().info?.id.toString()
        val request = BankOrcRequest("2",bankCardPhotoUrl, id, transactions.createTransaction())
        return Cache[IOpenAccountNet::class.java]?.bankOcr(request)!!
    }

    /**
     * 银行卡OCR识别响应成功返回
     */
    fun getSuccessConsumer(): io.reactivex.functions.Consumer<BankOrcResponse> {
        return io.reactivex.functions.Consumer {
            if (it.isSuccess()) {
                view?.onBankOcrSuccess(it.data.bankCardNo, it.data.bankCardName)
            } else {
                view?.showToast(it.msg)
            }
        }
    }

    /**
     * 银行卡OCR识别请求发生异常
     */
    fun getErrorConsumer(): io.reactivex.functions.Consumer<Throwable> {
        return io.reactivex.functions.Consumer {
            view?.showToast(it.message)

        }
    }

    /**
     * 银行卡三要素认证+一类卡认证
     */
    fun bankCardVerification() {
        val bankCardNo = view?.getBankCardNo()
        val bankName = view?.getBankName()
        if (!checkData(bankCardNo, bankName)) return
        val request = BankCardVerificationRequest(
            bankCardNo.toString(),
            bankName.toString(),
            OpenInfoManager.getInstance().info?.id.toString(),
            transactions.createTransaction()
        )
        view?.showUpLoading()
        Cache[IOpenAccountNet::class.java]?.bankCardVerification(request)
            ?.enqueue(Network.IHCallBack<BankCardVerificationResponse>(request))
    }

    private fun checkData(bankCardNo: String?, bankName: String?): Boolean {
        var t = ""
        if (TextUtils.isEmpty(bankCardNo)) {
            t = ResUtil.getString(R.string.bank_card_code) + ResUtil.getString(R.string.str_not_empty)
        } else if (TextUtils.isEmpty(bankName)) {
            t = ResUtil.getString(R.string.str_please_select) + ResUtil.getString(R.string.open_account_bank)
        }
        return if (TextUtils.isEmpty(t)) {
            true
        } else {
            view?.showToast(t)
            false
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onBankCardVerificationResponse(response: BankCardVerificationResponse) {
        view?.hideUpLoading()
        // 记录开户信息
        OpenInfoManager.getInstance()?.readBankCardVerificationResponse(response.data)
        view?.toNext()
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        if (response.request is BankCardVerificationRequest) {
            view?.hideUpLoading()
            view?.showToast(response.msg)
        }
    }

    fun initBankList() {
        val jsonData = GetJsonDataUtil().getJson(view?.getContext(), "banks.json")//获取assets目录下的json文件数据
        val arr = JSONArray(jsonData)
        bankList = mutableListOf()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            bankList?.add(obj.getString("name"))
        }

    }

    fun getBankData(): MutableList<String>? {
        return bankList
    }
}