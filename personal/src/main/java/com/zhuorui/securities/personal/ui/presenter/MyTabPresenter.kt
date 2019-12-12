package com.zhuorui.securities.personal.ui.presenter

import com.zhuorui.commonwidget.config.AppLanguage
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.commonwidget.config.StocksThemeColor
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.event.LoginStateChangeEvent
import com.zhuorui.securities.personal.event.MyTabInfoEvent
import com.zhuorui.securities.personal.event.SettingChooseEvent
import com.zhuorui.securities.personal.net.IPersonalNet
import com.zhuorui.securities.personal.net.request.UserLoginOutRequest
import com.zhuorui.securities.personal.ui.view.MyTabVierw
import com.zhuorui.securities.personal.ui.viewmodel.MyTabVierwModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/27 14:13
 *    desc   :
 */
class MyTabPresenter : AbsNetPresenter<MyTabVierw, MyTabVierwModel>() {
    fun requestUserLoginOut() {
        val request = UserLoginOutRequest(transactions.createTransaction())
        Cache[IPersonalNet::class.java]?.userLoginOut(request)
            ?.enqueue(Network.IHCallBack<BaseResponse>(request))
    }


    override fun onBaseResponse(response: BaseResponse) {
        super.onBaseResponse(response)
        if (response.request is UserLoginOutRequest) {
            // 通知登录状态发生改变
            RxBus.getDefault().post(LoginStateChangeEvent(false))
            LocalAccountConfig.getInstance().loginOut()
            LocalAccountConfig.getInstance().setZrNo(0)
            view?.gotomain()

        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onLoginState(event: LoginStateChangeEvent) {
        view?.loginStateChange()
        if (event.isLogin&&event.transaction!=null&&  transactions.isMyTransaction(event.transaction!!)) {
            view?.gotoClientService()
        }
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onMyTabInfo(evnt: MyTabInfoEvent) {
        view?.changeMyTabInfoView()
    }

    fun setConfigValue(type: Int): String? {
        when (type) {
            1 -> {
                return when (LocalSettingsConfig.getInstance().stocksThemeColor) {
                    StocksThemeColor.redUpGreenDown -> {
                        ResUtil.getString(R.string.red_up_green_down)
                    }
                    StocksThemeColor.greenUpRedDown -> {
                        ResUtil.getString(R.string.green_up_red_down)
                    }
                }
            }
            2 -> {
                return when (LocalSettingsConfig.getInstance().appLanguage) {
                    AppLanguage.auto -> {
                        ResUtil.getString(R.string.auto)
                    }
                    AppLanguage.zh_CN -> {
                        ResUtil.getString(R.string.simple_cn)
                    }
                    AppLanguage.zh_TW -> {
                        ResUtil.getString(R.string.unsimple_cn)
                    }
                    AppLanguage.zh_HK -> {
                        ResUtil.getString(R.string.unsimple_cn)
                    }
                    AppLanguage.en_US -> {
                        ResUtil.getString(R.string.english)
                    }
                }
            }
        }
        return null
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSettingChangeEvent(event: SettingChooseEvent) {
        view?.changeSetChooseSet(event.type, event.str)
    }

    fun getTranscation(): String {
        return transactions.createTransaction()
    }

}