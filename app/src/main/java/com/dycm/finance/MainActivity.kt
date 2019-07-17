package com.dycm.finance

import android.view.View
import butterknife.OnClick
import com.dycm.base2app.Cache
import com.dycm.base2app.network.Network
import com.dycm.base2app.ui.activity.AbsNetActivity
import com.dycm.finance.net.ITestNet
import com.dycm.finance.net.request.TestDotradeRequset
import com.dycm.finance.net.response.TestDotradeResponse

class MainActivity : AbsNetActivity() {

    override val acContentRootViewId: Int
        get() = R.id.root_view

    override val layout: Int
        get() = R.layout.activity_main

    @OnClick(R.id.button)
    fun onClick(v: View) {
        val request = TestDotradeRequset(transactions.createTransaction())
        Cache[ITestNet::class.java]?.dotrade(request)?.enqueue(Network.IHCallBack<TestDotradeResponse>(request))
    }
}
