package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.LiveRecognRequest
import com.zhuorui.securities.openaccount.net.response.LiveRecognResponse
import com.zhuorui.securities.openaccount.ui.view.OAVedioRecordView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAVedioRecordViewModel
import com.zhuorui.securities.base2app.util.Base64Enum
import com.zhuorui.securities.base2app.util.FileToBase64Util
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/27
 * Desc:
 */
class OAVedioRecordPresenter : AbsNetPresenter<OAVedioRecordView, OAVedioRecordViewModel>() {

    private val disposables = LinkedList<Disposable>()
    private var intervalTime: Disposable? = null

    fun setVerifyCode() {
        viewModel?.verifyCode?.value = OpenInfoManager.getInstance()?.info?.validateCode
    }

    fun uploadVedio(vedioBytes: ByteArray?) {
        view?.showUploading()
        view?.setProgressText(ResUtil.getString(R.string.str_encrypting_video))
        //  在子线程中计算视频流的Base64码，然后上传
        val disposable = Observable.create(ObservableOnSubscribe<String> { emitter ->
            emitter.onNext(FileToBase64Util.getBase64String(Base64Enum.MP4, vedioBytes))
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                intervalTime()
                val request = LiveRecognRequest(
                    it,
                    viewModel?.verifyCode?.value,
                    OpenInfoManager.getInstance()?.info?.id,
                    transactions.createTransaction()
                )
                Cache[IOpenAccountNet::class.java]?.getLiveRecogn(request)
                    ?.enqueue(Network.IHCallBack<LiveRecognResponse>(request))
            }
        disposables.add(disposable)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onLiveRecognResponse(response: LiveRecognResponse) {
        exitTntervalTime()
        // 设置数据
        val info = OpenInfoManager.getInstance()?.info
        info?.openStatus = response.data.openStatus
        info?.video = response.data.video
        info?.validateCode = response.data.validateCode
        view?.hideUploading()
        view?.uploadComplete(true)
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        if (response.request is LiveRecognRequest) {
            exitTntervalTime()
            view?.hideUploading()
            view?.uploadComplete(false)
        }
    }

    /**
     * 退出模拟上传进度，防止接口返回时间比倒计时快
     */
    private fun exitTntervalTime() {
        if (intervalTime != null && !intervalTime?.isDisposed!!) {
            intervalTime?.dispose()
            intervalTime = null
        }
    }

    /**
     * 模拟上传进度
     */
    private fun intervalTime() {
        //时间2秒，20ms一次，执行100次
        intervalTime = Observable.interval(20, TimeUnit.MILLISECONDS)
            .take(100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (intervalTime != null && !intervalTime?.isDisposed!!) {
                    val p = it + 1
                    if (p >= 100) {
                        view?.setProgressText(ResUtil.getString(R.string.str_identifying))
                    } else {
                        val txtUpload = ResUtil.getString(R.string.str_upload)
                        view?.setProgressText("$txtUpload$p%")
                    }
                }
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