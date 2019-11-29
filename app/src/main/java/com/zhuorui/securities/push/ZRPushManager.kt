package com.zhuorui.securities.push

import android.os.Handler
import com.zhuorui.commonwidget.push.AbsPushManager
import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/27 16:43
 *    desc   : 管理App各模块的推送管理器
 */
class ZRPushManager private constructor() {

    private val handler = Handler()

    /**
     * 存放各模块的推送管理器
     */
    private val pushManagers = LinkedList<AbsPushManager>()

    /**
     * 缓存单例
     */
    private object Builder {
        val instance = ZRPushManager()
    }

    /**
     * 获取单例
     */
    companion object {
        fun getInstance(): ZRPushManager {
            return Builder.instance
        }
    }

    /**
     * 添加推送管理器
     */
    fun addPushManager(vararg manager: AbsPushManager) {
        pushManagers.addAll(manager)
    }

    /**
     * 接收到推送后，下发到对应的模块中
     */
    fun onReceiveMessageData(data: String) {
        for (manager in pushManagers) {
            if (manager.checkPushData(data)) {
                handler.post { manager.onReceivePushData(data) }
                break
            }
        }
    }
}