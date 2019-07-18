package com.dycm.finance

import android.view.View
import butterknife.OnClick
import com.dycm.applib1.TestFragment
import com.dycm.base2app.ui.activity.AbsNetActivity
import com.dycm.finance.socket.AppSocketClient
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

class MainActivity : AbsNetActivity() {

    override val acContentRootViewId: Int
        get() = R.id.root_view

    override val layout: Int
        get() = R.layout.activity_main

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置Fragment切换时默认动画为横向滑动，类似微信
        return DefaultHorizontalAnimator()
    }

    @OnClick(R.id.button1)
    fun onClick1(v: View) {
//        val request = TestDotradeRequset(transactions.createTransaction())
//        Cache[ITestNet::class.java]?.dotrade(request)?.enqueue(Network.IHCallBack<TestDotradeResponse>(request))

        val socketClient = AppSocketClient()
        socketClient.connect()
    }

    @OnClick(R.id.button2)
    fun onClick2(v: View) {
        if (findFragment(TestFragment::class.java) == null) {
            loadRootFragment(R.id.root_view, TestFragment())
        }
    }
}
