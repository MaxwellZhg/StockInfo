package com.zhuorui.securities.openaccount.ui.presenter

import android.graphics.Bitmap
import android.util.Log
import com.zhuorui.commonwidget.impl.IImageUploader
import com.zhuorui.commonwidget.impl.OnImageUploaderListener
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.JsonUtil
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.IdCardOrcRequest
import com.zhuorui.securities.openaccount.ui.view.OAUploadDocumentsView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAUploadDocumentsViewModel
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
     */
    fun getUploader(tpye: Int): IImageUploader? {
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
                    val id: String = OpenInfoManager.getInstance()?.info?.id.toString()
                    val request = IdCardOrcRequest(tpye, t, id, transactions.createTransaction())
                    Cache[IOpenAccountNet::class.java]?.idCardOcr(request)!!
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(io.reactivex.functions.Consumer<BaseResponse> { t ->
                        if (t.isSuccess()) {
                            listener?.onSuccess()
                        } else {
                            listener?.onFail(t.msg)
                        }
                    }, io.reactivex.functions.Consumer<Throwable> {
                        listener?.onFail(it.message)

                    })
            }

        }
    }


}



