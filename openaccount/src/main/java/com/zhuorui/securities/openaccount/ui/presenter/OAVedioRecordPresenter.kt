package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.LiveRecognRequest
import com.zhuorui.securities.openaccount.net.response.LiveRecognResponse
import com.zhuorui.securities.openaccount.ui.view.OAVedioRecordView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAVedioRecordViewModel
import io.reactivex.Observable
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

    override fun init() {
        super.init()
    }

    fun setVerifyCode() {
        viewModel?.verifyCode?.value = OpenInfoManager.getInstance()?.info?.validateCode
    }

    fun uploadVedio(vedioBytes: ByteArray) {
        view?.showUploading()
        view?.setProgressText(ResUtil.getString(R.string.str_encrypting_video))
        val oss = OpenInfoManager.getInstance().getOssService(view?.getContext()!!)
        val disposable = oss?.getPutObjectObservable(oss.createUpImageName(".mp4"), vedioBytes)
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                intervalTime()
                val request = LiveRecognRequest(
                    "2",
                    it,
                    viewModel?.verifyCode?.value,
                    OpenInfoManager.getInstance()?.info?.id,
                    transactions.createTransaction()
                )
                Cache[IOpenAccountNet::class.java]?.getLiveRecogn(request)
                    ?.enqueue(Network.IHCallBack<LiveRecognResponse>(request))
            }
        disposables.add(disposable!!)
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