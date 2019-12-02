package com.zhuorui.securities.openaccount.ui.presenter

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
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
import com.zhuorui.securities.openaccount.net.request.SubBasicsInfoRequest
import com.zhuorui.securities.openaccount.net.response.SubBasicsInfoResponse
import com.zhuorui.securities.openaccount.ui.view.OAOhterNotesView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAOhterNotesViewModel

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-08-20 14:19
 *    desc   :
 */
class OAOhterNotesPresenter : AbsNetPresenter<OAOhterNotesView, OAOhterNotesViewModel>() {

    fun checkData(): Boolean {
        if (view != null){
            val list = view!!.getSwitchStatus()
            return list[0] && !list[1] && !list[2] && !list[3]
        }
        return false
    }

    fun subBasicsInfo() {
        val request = SubBasicsInfoRequest(
            OpenInfoManager.getInstance().info?.mailbox!!,
            OpenInfoManager.getInstance().info?.occupation!!,
            OpenInfoManager.getInstance().info?.taxType!!,
            OpenInfoManager.getInstance().info?.taxState!!,
            OpenInfoManager.getInstance().info?.taxNumber!!,
            OpenInfoManager.getInstance().info?.income!!,
            OpenInfoManager.getInstance().info?.rate!!,
            OpenInfoManager.getInstance().info?.risk!!,
            OpenInfoManager.getInstance().info?.capitalSource!!,
            OpenInfoManager.getInstance().info?.investShares!!,
            OpenInfoManager.getInstance().info?.investBond!!,
            OpenInfoManager.getInstance().info?.investGoldForeign!!,
            OpenInfoManager.getInstance().info?.investFund!!,
            OpenInfoManager.getInstance().info?.id!!,
            transactions.createTransaction()
        )
        Cache[IOpenAccountNet::class.java]?.subBasicsInfo(request)
            ?.enqueue(Network.IHCallBack<SubBasicsInfoResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSubBasicsInfoResponse(response: SubBasicsInfoResponse) {
        // 记录开户信息
        OpenInfoManager.getInstance().readSubBasicsInfoResponse(response.data)
        view?.toNext()
    }

    override fun onErrorResponse(response: ErrorResponse) {
        super.onErrorResponse(response)
        if (response.request is SubBasicsInfoRequest) {

        }
    }

    fun getDialogTextSpannable(): Spannable {
        val txt = ResUtil.getString(R.string.ohter_notes_tips)!!
        val tel = ResUtil.getString(R.string.tel)!!
        val spannable = SpannableString(String.format(txt, tel))
        val start = spannable.indexOf(tel)
        spannable.setSpan(
            ForegroundColorSpan(ResUtil.getColor(R.color.color_1A6ED2)!!),
            start,
            start + tel.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        return spannable
    }


}