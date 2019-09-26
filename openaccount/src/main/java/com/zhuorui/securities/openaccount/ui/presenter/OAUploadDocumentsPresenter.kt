package com.zhuorui.securities.openaccount.ui.presenter

import android.graphics.Bitmap
import com.zhuorui.commonwidget.impl.IImageUploader
import com.zhuorui.commonwidget.impl.OnImageUploaderListener
import com.zhuorui.securities.alioss.service.OssService
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.IdCardOrcRequest
import com.zhuorui.securities.openaccount.net.response.IdCardOrcResponse
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
import com.zhuorui.securities.base2app.util.Base64Enum
import com.zhuorui.securities.base2app.util.FileToBase64Util
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAUploadDocumentsPresenter : AbsNetPresenter<OAUploadDocumentsView, OAUploadDocumentsViewModel>() {

    var oss:OssService? = null

    override fun init() {
        super.init()
        oss = view?.getContext()?.let { OssService(it,OpenInfoManager.getInstance()!!.bucket.endpoint,OpenInfoManager.getInstance()!!.bucket.bucketName) }
    }

    fun setDefData() {
        val data: OpenAccountInfo? = OpenInfoManager.getInstance()?.info
        view?.setCardFrontUrl(data?.cardFrontPhoto)
        view?.cardBackPhotoUrl(data?.cardBackPhoto)

    }

    /**
     * 上传图片
     * @param tp 类型 0 人像面 1 国徽面
     */
    fun getUploader(tp: Int): IImageUploader? {
        return object : IImageUploader {

            var listener: OnImageUploaderListener? = null
            override fun setOnUploaderListener(l: OnImageUploaderListener?) {
                listener = l
            }

            override fun upLoad(path: String?) {
                Observable.create(ObservableOnSubscribe<ByteArray> { emitter ->
                    emitter.onNext(FileToBase64Util.getSmallBitmap(FileToBase64Util.getSmallBitmap(path)))
                    emitter.onComplete()
                }).flatMap {
                    oss?.getPutObjectObservable(oss!!.createUpImageName(".jpg"), it)
                }.flatMap { t ->
                    getIdCardOcrObservable(t)
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSuccessConsumer(), getErrorConsumer())
            }

            override fun upLoad(bitmap: Bitmap?) {
                Observable.create(ObservableOnSubscribe<ByteArray> { emitter ->
                    emitter.onNext(FileToBase64Util.getSmallBitmap(bitmap))
                    emitter.onComplete()
                }).flatMap {
                    oss?.getPutObjectObservable(oss!!.createUpImageName(".jpg"), it)
                }.flatMap { t ->
                    getIdCardOcrObservable(t)
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSuccessConsumer(), getErrorConsumer())
            }

            fun getIdCardOcrObservable(url: String): Observable<IdCardOrcResponse> {
                val id: String = OpenInfoManager.getInstance()?.info?.id.toString()
                val request = IdCardOrcRequest(tp, "2", url, id, transactions.createTransaction())
                return Cache[IOpenAccountNet::class.java]?.idCardOcr(request)!!
            }

            fun getSuccessConsumer(): io.reactivex.functions.Consumer<IdCardOrcResponse> {
                return io.reactivex.functions.Consumer {
                    if (it.isSuccess()) {
                        listener?.onSuccess(if (tp == 0) it.data.cardFrontPhoto else it.data.cardBackPhoto)
                    } else {
                        listener?.onFail(it.code, it.msg)
                    }
                }
            }

            fun getErrorConsumer(): io.reactivex.functions.Consumer<Throwable> {
                return io.reactivex.functions.Consumer {
                    listener?.onFail("-1", it.message)

                }
            }

        }
    }


}



