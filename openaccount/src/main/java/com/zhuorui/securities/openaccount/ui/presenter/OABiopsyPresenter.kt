package com.zhuorui.securities.openaccount.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.openaccount.ui.net.IOpenAccountNet
import com.zhuorui.securities.openaccount.ui.net.request.LiveCodeRequest
import com.zhuorui.securities.openaccount.ui.net.request.OpenInfoRequest
import com.zhuorui.securities.openaccount.ui.net.response.LiveCodeResponse
import com.zhuorui.securities.openaccount.ui.net.response.OpenInfoResponse
import com.zhuorui.securities.openaccount.ui.view.OABiopsyView
import com.zhuorui.securities.openaccount.ui.viewmodel.OABiopsyViewModel
import me.jessyan.autosize.utils.LogUtils

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/26
 * Desc:
 */
class OABiopsyPresenter : AbsNetPresenter<OABiopsyView,OABiopsyViewModel>(){
    override fun init() {
        super.init()
        view?.init()
    }

}