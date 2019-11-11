package com.zhuorui.securities.market.ui.presenter

import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.F10BrieListRequest
import com.zhuorui.securities.market.net.request.F10BrieRequest
import com.zhuorui.securities.market.net.response.F10DividentListResponse
import com.zhuorui.securities.market.net.response.F10RepoListResponse
import com.zhuorui.securities.market.net.response.F10ShareHolderListResponse
import com.zhuorui.securities.market.ui.view.CompanyBrieViewMoreView
import com.zhuorui.securities.market.ui.viewmodel.CompanyBrieViewMoreViewModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/7 19:16
 *    desc   :
 */
class CompanyBrieViewMorePresenter : AbsNetPresenter<CompanyBrieViewMoreView, CompanyBrieViewMoreViewModel>() {

    private var currentPage = 0

    /**
     * 加载数据
     */
    fun loadData(ts: String, code: String, type: Int) {
        when (type) {
            1 -> {
                // 公司高管

            }
            2 -> {
                // 持股变动
                val requset = F10BrieListRequest(ts, code, currentPage, transactions.createTransaction())
                Cache[IStockNet::class.java]?.getShareHolderList(requset)
                    ?.enqueue(Network.IHCallBack<F10ShareHolderListResponse>(requset))
            }
            3 -> {
                // 分红配送
                val requset = F10BrieRequest(ts, code, transactions.createTransaction())
                Cache[IStockNet::class.java]?.getDividentList(requset)
                    ?.enqueue(Network.IHCallBack<F10DividentListResponse>(requset))
            }
            4 -> {
                // 公司回购
                val requset = F10BrieListRequest(ts, code, currentPage, transactions.createTransaction())
                Cache[IStockNet::class.java]?.getRepoList(requset)
                    ?.enqueue(Network.IHCallBack<F10RepoListResponse>(requset))
            }
        }
    }

    fun loadMoreData(ts: String, code: String, type: Int) {
        currentPage++
        loadData(ts, code, type)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onF10ShareHolderListResponse(response: F10ShareHolderListResponse) {
        val data = response.data
        view?.updateShareHolderList(data)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onF10DividentListResponse(response: F10DividentListResponse) {
        val data = response.data
        view?.updateDividentList(data)
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onF10RepoListResponse(response: F10RepoListResponse) {
        val data = response.data
        view?.updateRepoList(data)
    }
}