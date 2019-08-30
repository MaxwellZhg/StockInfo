package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.LiveCodeRequest
import com.zhuorui.securities.openaccount.net.request.LiveRecognRequest
import com.zhuorui.securities.openaccount.net.request.OpenInfoRequest
import com.zhuorui.securities.openaccount.net.response.LiveCodeResponse
import com.zhuorui.securities.openaccount.net.response.LiveRecognResponse
import com.zhuorui.securities.openaccount.net.response.OpenInfoResponse
import com.zhuorui.securities.openaccount.ui.view.OAVedioRecordView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAVedioRecordViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/27
 * Desc:
 */
class OAVedioRecordPresenter : AbsNetPresenter<OAVedioRecordView, OAVedioRecordViewModel>() {
    override fun init() {
        super.init()
        view?.init()
    }
    /*   fun requestOpenInfo() {
           val request = OpenInfoRequest( transactions.createTransaction())
           Cache[IOpenAccountNet::class.java]?.getOpenInfo(request)
               ?.enqueue(Network.IHCallBack<OpenInfoResponse>(request))
       }
       @RxSubscribe(observeOnThread = EventThread.MAIN)
       fun onVerifyOpenInfo(response: OpenInfoResponse) {
           if(response.request is OpenInfoRequest) {
               viewModel?.idstr?.set(response.data.id)
               requestVedioVerifyCode(OpenInfoManager.get().info?.id)
           }
       }*/
//
//    fun requestVedioVerifyCode(id:String ?) {
//        id?.let {
//            val request =     LiveCodeRequest(it, transactions.createTransaction())
//            Cache[IOpenAccountNet::class.java]?.getLiveCode(request)
//                ?.enqueue(Network.IHCallBack<LiveCodeResponse>(request))
//        }
//
//    }

    fun requestVedioVerifyCode() {
        // TODO 假数据
        viewModel?.str?.set("1234")
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onVerifyLiveCode(response: LiveCodeResponse) {
        if (response.request is LiveCodeRequest) {
            viewModel?.str?.set(response.data.validateCode)
        }
    }

    fun requestCode() {
        view?.requestCode()
    }

    fun uploadVedio(vedio: String) {
        val request = LiveRecognRequest(
            vedio,
            viewModel?.str?.get(),
            "0e1e0da346b7fb71dff9c3c3a863e075",
            transactions.createTransaction()
        )
        Cache[IOpenAccountNet::class.java]?.getLiveRecogn(request)
            ?.enqueue(Network.IHCallBack<LiveRecognResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onLiveRecognCode(response: LiveRecognResponse) {
        if (response.request is LiveRecognRequest) {
            LogUtils.e("成功")
        }
    }


}