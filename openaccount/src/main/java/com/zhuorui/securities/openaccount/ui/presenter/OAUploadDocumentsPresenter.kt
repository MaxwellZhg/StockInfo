package com.zhuorui.securities.openaccount.ui.presenter

import android.graphics.Bitmap
import com.zhuorui.commonwidget.impl.IImageUploader
import com.zhuorui.commonwidget.impl.OnImageUploaderListener
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.IdCardOrcRequest
import com.zhuorui.securities.openaccount.net.response.IdCardOrcResponse
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
import com.zhuorui.securities.openaccount.utils.Base64Enum
import com.zhuorui.securities.openaccount.utils.FileToBase64Util
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAUploadDocumentsPresenter : AbsNetPresenter<OAUploadDocumentsView, OAUploadDocumentsViewModel>() {


    override fun init() {
        super.init()
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
                Observable.create(ObservableOnSubscribe<String> { emitter ->
                    emitter.onNext(FileToBase64Util.bitmapBase64String(path))
                    emitter.onComplete()
                }).flatMap { t ->
                    getIdCardOcrObservable(t)
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSuccessConsumer(), getErrorConsumer())
            }

            override fun upLoad(bitmap: Bitmap?) {
                Observable.create(ObservableOnSubscribe<String> { emitter ->
                    emitter.onNext(FileToBase64Util.bitMapBase64String(Base64Enum.PNG, bitmap))
                    emitter.onComplete()
                }).flatMap { t ->
                    getIdCardOcrObservable(t)
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getSuccessConsumer(), getErrorConsumer())
            }

            fun getIdCardOcrObservable(ba64Data: String): Observable<IdCardOrcResponse> {
                val id: String = OpenInfoManager.getInstance()?.info?.id.toString()
                val request = IdCardOrcRequest(tp, ba64Data, id, transactions.createTransaction())
                return Cache[IOpenAccountNet::class.java]?.idCardOcr(request)!!
            }

            fun getSuccessConsumer(): io.reactivex.functions.Consumer<IdCardOrcResponse> {
                return io.reactivex.functions.Consumer {
                    if (it.isSuccess()) {
                        listener?.onSuccess(if (tp == 0) it.data.cardFrontPhoto else it.data.cardBackPhoto)
                    } else {
                        listener?.onFail(it.msg)
                    }
                }
            }

            fun getErrorConsumer(): io.reactivex.functions.Consumer<Throwable> {
                return io.reactivex.functions.Consumer {
                    listener?.onFail(it.message)

                }
            }

        }
    }


}



