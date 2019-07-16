package com.dycm.base2app.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.FloatRange
import com.dycm.base2app.network.Transactions
import com.dycm.base2app.rxbus.IRxBusEvent
import com.dycm.base2app.rxbus.RxBus
import me.yokeyword.fragmentation.SwipeBackLayout
import me.yokeyword.fragmentation_swipeback.core.ISwipeBackFragment
import me.yokeyword.fragmentation_swipeback.core.SwipeBackFragmentDelegate

/**
 *    author : Pengxianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019-05-20 14:13
 *    desc   : 定义基础侧滑返回的Fragment
 *             适用场景：二级页面（如查看详情）
 *             在此基础上增加了RxBus
 */
abstract class AbsSwipeBackEventFragment : AbsSwipeBackFragment(), IRxBusEvent {
    protected var transactions = Transactions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerRxBus()
    }

    override fun registerRxBus() {
        val registered = RxBus.getDefault().isRegistered(this)
        if (!registered) {
            RxBus.getDefault().register(this)
        }
    }

    override fun unregisterRxBus() {
        val registered = RxBus.getDefault().isRegistered(this)
        if (registered) {
            RxBus.getDefault().unregister(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        transactions.clear()
        unregisterRxBus()
    }
}