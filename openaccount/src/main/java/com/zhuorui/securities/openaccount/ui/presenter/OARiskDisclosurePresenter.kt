package com.zhuorui.securities.openaccount.ui.presenter

import android.media.MediaPlayer
import com.zhuorui.securities.base2app.BaseApplication.Companion.context
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.ui.fragment.AbsPresenter
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.manager.OpenInfoManager
import com.zhuorui.securities.openaccount.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.net.request.SubIdentityRequest
import com.zhuorui.securities.openaccount.net.request.SubRiskDisclosureRequest
import com.zhuorui.securities.openaccount.net.response.SubIdentityResponse
import com.zhuorui.securities.openaccount.net.response.SubRiskDisclosureResponse
import com.zhuorui.securities.openaccount.ui.view.OARiskDisclosureView
import com.zhuorui.securities.openaccount.ui.viewmodel.OARiskDisclosureViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/28 10:00
 *    desc   :
 */
class OARiskDisclosurePresenter : AbsNetPresenter<OARiskDisclosureView, OARiskDisclosureViewModel>() {

    var mediaPlayer: MediaPlayer? = null

    fun speechRisk() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.release()
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.risk_txt)
        mediaPlayer?.setOnCompletionListener {
            viewModel?.playingRisk?.value = false
            // 播放完成，释放资源
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer?.setOnPreparedListener {
            // 开始播放
            mediaPlayer?.start()
            viewModel?.playingRisk?.value = true
        }
    }

    fun subRiskDisclosure() {
        val request = SubRiskDisclosureRequest(OpenInfoManager.getInstance()?.info?.id.toString(),transactions.createTransaction())
        Cache[IOpenAccountNet::class.java]?.subRiskDisclosure(request)?.enqueue(Network.IHCallBack<SubRiskDisclosureResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSubRiskDisclosureResponse(response: SubRiskDisclosureResponse) {
        // 记录开户信息
        OpenInfoManager.getInstance()?.readSubRiskDisclosureResponse(response.data)
        view?.toNext()
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        if (response.request is SubRiskDisclosureRequest) {
            view?.showToast(response.msg)
        }
    }
}