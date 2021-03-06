package com.zhuorui.securities.openaccount.ui.presenter

import android.text.TextUtils
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseRequest
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.OpenInfoRequest
import com.zhuorui.securities.openaccount.net.response.BucketResponse
import com.zhuorui.securities.openaccount.net.response.OpenInfoResponse
import com.zhuorui.securities.openaccount.ui.view.OpenAccountTabView
import com.zhuorui.securities.openaccount.ui.viewmodel.OpenAccountTabViewModel
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.JumpToOpenAccountEvent
import com.zhuorui.securities.personal.event.LoginStateChangeEvent

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/14
 * Desc:
 */
class OpenAccountTabPresenter : AbsNetPresenter<OpenAccountTabView, OpenAccountTabViewModel>() {
    override fun init() {
        super.init()
        view?.init()
        if (getLoginStatus()) {
            requestOpenInfo()
            requestOpenAccOssInfo()
        }
    }

    fun getLoginStatus(): Boolean {
        return LocalAccountConfig.getInstance().isLogin()
    }

    /**
     * 获取开户信息
     */
    fun requestOpenInfo() {
        val request = OpenInfoRequest(transactions.createTransaction())
        Cache[IOpenAccountNet::class.java]?.getOpenInfo(request)
            ?.enqueue(Network.IHCallBack<OpenInfoResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onOpenInfoResponse(response: OpenInfoResponse) {
        // 记录开户信息
        OpenInfoManager.getInstance()?.info = response.data
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onJumpToOpenAccountStepsEvent(event: JumpToOpenAccountEvent) {
        view?.onJumpToOpenAccountPage()
    }


    private fun requestOpenAccOssInfo() {
        val request = BaseRequest(transactions.createTransaction())
        request.generateSign()
        Cache[IOpenAccountNet::class.java]?.bucketName(request)
            ?.enqueue(Network.IHCallBack<BucketResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onBucketResponse(response: BucketResponse) {
        // 记录开户信息
        for (data in response.data) {
            if (TextUtils.equals("1", data.type)) {
                OpenInfoManager.getInstance()?.bucket = data
                break
            }
        }
    }


}