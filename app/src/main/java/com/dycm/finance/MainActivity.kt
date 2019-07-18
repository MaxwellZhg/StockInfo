package com.dycm.finance

import android.os.Bundle
import android.view.View
import butterknife.OnClick
import com.dycm.applib1.TestFragment
import com.dycm.base2app.Cache
import com.dycm.base2app.network.Network
import com.dycm.base2app.ui.activity.AbsNetActivity
import com.dycm.finance.net.ITestNet
import com.dycm.finance.net.request.TestDotradeRequset
import com.dycm.finance.net.response.TestDotradeResponse
import com.dycm.finance.socket.SocketClient
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

class MainActivity : AbsNetActivity() {

    override val acContentRootViewId: Int
        get() = R.id.root_view

    override val layout: Int
        get() = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (findFragment(TestFragment::class.java) == null) {
            loadRootFragment(R.id.root_view, TestFragment())
        }
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置Fragment切换时默认动画为横向滑动，类似微信
        return DefaultHorizontalAnimator()
    }

//    @OnClick(R.id.button)
//    fun onClick(v: View) {
//        val request = TestDotradeRequset(transactions.createTransaction())
//        Cache[ITestNet::class.java]?.dotrade(request)?.enqueue(Network.IHCallBack<TestDotradeResponse>(request))
//    }
}
