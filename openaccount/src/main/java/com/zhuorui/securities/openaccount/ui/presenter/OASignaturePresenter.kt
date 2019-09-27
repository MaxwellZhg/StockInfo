package com.zhuorui.securities.openaccount.ui.presenter

import android.graphics.Bitmap
import com.zhuorui.securities.alioss.service.OssService
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.SubSignatureRequest
import com.zhuorui.securities.openaccount.net.response.SubSignatureResponse
import com.zhuorui.securities.openaccount.ui.view.OASignatureView
import com.zhuorui.securities.openaccount.ui.viewmodel.OASignatureViewModel
import com.zhuorui.securities.base2app.util.Base64Enum
import com.zhuorui.securities.base2app.util.FileToBase64Util
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/28 11:28
 *    desc   :
 */
class OASignaturePresenter : AbsNetPresenter<OASignatureView, OASignatureViewModel>() {

    private val disposables = LinkedList<Disposable>()

    override fun init() {
        super.init()
    }

    /**
     * 同意开户
     */
    fun clickAgreement(agreement: Boolean) {
        viewModel?.agreement?.value = agreement
    }

    /**
     * 把签名位图转换成base64后上传
     */
    fun uploadSignature(bitmap: Bitmap?) {
        view?.showLoading()
        // 转码后发起请求
        val disposable = Observable.create(ObservableOnSubscribe<ByteArray> { emitter ->
            emitter.onNext(FileToBase64Util.getSmallBitmap(bitmap))
            emitter.onComplete()
        }).flatMap {
            val oss = OpenInfoManager.getInstance().getOssService(view?.getContext()!!)
            oss?.getPutObjectObservable(oss!!.createUpImageName(".jpg"), it)
        }.subscribeOn(Schedulers.io())
            .subscribe {
                val request = SubSignatureRequest(
                    "2",
                    it,
                    OpenInfoManager.getInstance()?.info?.id.toString(),
                    transactions.createTransaction()
                )
                Cache[IOpenAccountNet::class.java]?.subSignature(request)
                    ?.enqueue(Network.IHCallBack<SubSignatureResponse>(request))
            }
        disposables.add(disposable)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSubSignatureResponse(response: SubSignatureResponse) {
        // 记录开户信息
        OpenInfoManager.getInstance()?.info?.openStatus = response.data.openStatus
        view?.hideLoading()
        // 跳转到下一步
        view?.jumpToNext()
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        if (response.request is SubSignatureRequest) {
            view?.hideLoading()
        }
    }

    override fun destroy() {
        super.destroy()

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
}