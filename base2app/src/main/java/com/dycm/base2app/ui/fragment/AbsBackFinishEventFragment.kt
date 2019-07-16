package com.dycm.base2app.ui.fragment

import android.os.Bundle
import com.dycm.base2app.network.Transactions
import com.dycm.base2app.rxbus.IRxBusEvent
import com.dycm.base2app.rxbus.RxBus
import com.dycm.base2app.util.AppActivityLifecycleImp

/**
 *    author : Pengxianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019-05-20 14:13
 *    desc   : 双击返回键退出Activity的Fragment
 *             适用场景：一级页面（如主页中的Tab）
 *             在此基础上增加了RxBus
 */
abstract class AbsBackFinishEventFragment : AbsBackFinishFragment(), IRxBusEvent {
    protected var transactions = Transactions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerRxBus()
    }

    override fun registerRxBus() {
//        val rxBus = RxBus.getDefault(_mActivity)
        val rxBus = RxBus.getDefault()
        val registered = rxBus.isRegistered(this)
        if (!registered) {
            rxBus.register(this)
        }
    }

    override fun unregisterRxBus() {
//        val rxBus = RxBus.getDefault(_mActivity)
        val rxBus = RxBus.getDefault()
        val registered = rxBus.isRegistered(this)
        if (registered) {
            rxBus.unregister(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        transactions.clear()
        unregisterRxBus()
    }
}