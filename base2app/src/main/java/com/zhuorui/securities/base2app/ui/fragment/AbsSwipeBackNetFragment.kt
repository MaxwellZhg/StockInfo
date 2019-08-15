package com.zhuorui.securities.base2app.ui.fragment

import com.zhuorui.securities.base2app.R
import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.base2app.network.ErrorResponse
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe

/**
 *    author : Pengxianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019-05-20 14:13
 *    desc   : 定义基础侧滑返回并带有RxBus的Fragment
 *             适用场景：二级页面（如查看详情）
 *             在此基础上增加了网络通用处理
 */
abstract class AbsSwipeBackNetFragment : AbsSwipeBackEventFragment() {

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onSuccessful(response: BaseResponse) {
        if (!transactions.isMyTransaction(response)) return
        onBaseResponse(response)
    }

    /***
     * 针对默认的请求成功，进行回调接收，具体处理由子类去实现
     */
    protected open fun onBaseResponse(response: BaseResponse){

    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onError(response: ErrorResponse) {
        if (!transactions.isMyTransaction(response)) return
        onErrorResponse(response)
    }

    /**
     * 默认针对于请求失败，进行toast提示
     *
     * @param response ErrorResponse
     */
    protected open fun onErrorResponse(response: ErrorResponse) {
        toast(if (response.isNetworkBroken) getString(R.string.network_anomaly) else response.msg)
    }
}