package com.zhuorui.securities.market.manager

import com.zhuorui.commonwidget.model.Observer
import com.zhuorui.commonwidget.model.Subject
import com.zhuorui.securities.base2app.rxbus.RxBus
import java.util.ArrayList

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/22 15:03
 *    desc   : 实现接口方法的共用数据缓存管理类
 */
abstract class BaseDataManager : Subject<Observer> {

    val TAG = this::class.java.simpleName

    // 存储订阅者
    private val observerList = ArrayList<Observer>()
    protected val requestIds = ArrayList<String>()

    init {
        RxBus.getDefault().register(this)
    }

    override fun registerObserver(obs: Observer) {
        synchronized(observerList){
            observerList.add(obs)
        }

    }

    override fun removeObserver(obs: Observer) {
        synchronized(observerList){
            observerList.remove(obs)
            // 当没有观察者了，清除当前实例
            if (observerList.isNullOrEmpty()) {
                destroy()
            }
        }

    }

    override fun notifyAllObservers() {
        synchronized(observerList){
            for (obs in observerList) {
                // 更新每一个观察者中的信息
                obs.update(this)
            }
        }
    }

    protected open fun destroy() {
        if (RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().unregister(this)
        }
    }
}