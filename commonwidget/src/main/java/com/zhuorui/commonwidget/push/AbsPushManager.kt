package com.zhuorui.commonwidget.push

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/27 17:09
 *    desc   : 通知管理类
 */
interface AbsPushManager {

    /**
     * 判断消息类型，是否需要处理
     */
    fun checkPushData(data: String): Boolean

    /**
     * 接收推送内容进行相应的处理，如：显示推送内容在app内弹窗，或者推送到通知栏
     */
    fun onReceivePushData(data: String)
}