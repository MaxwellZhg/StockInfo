package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.LiveCodeRequest
import com.zhuorui.securities.openaccount.net.response.LiveCodeResponse
import com.zhuorui.securities.openaccount.ui.view.OABiopsyView
import com.zhuorui.securities.openaccount.ui.viewmodel.OABiopsyViewModel

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/26
 * Desc:
 */
class OABiopsyPresenter : AbsNetPresenter<OABiopsyView, OABiopsyViewModel>() {

    override fun init() {
        super.init()
        requestVedioVerifyCode()
    }

    /**
     * 请求活体检查数字码
     */
    fun requestVedioVerifyCode() {
        OpenInfoManager.getInstance()?.info?.id?.let {
            val request = LiveCodeRequest(it, transactions.createTransaction())
            Cache[IOpenAccountNet::class.java]?.getLiveCode(request)
                ?.enqueue(Network.IHCallBack<LiveCodeResponse>(request))
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onVerifyLiveCode(response: LiveCodeResponse) {
        if (response.request is LiveCodeRequest) {
            OpenInfoManager.getInstance()?.info?.validateCode = response.data.validateCode
            viewModel?.verifyCode?.value = response.data.validateCode
        }
    }

    fun getVerifyCode(): String? {
        return viewModel?.verifyCode?.value
    }
}