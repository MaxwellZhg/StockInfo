package com.dycm.applib1.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dycm.applib1.R
import com.dycm.applib1.model.StockMarketInfo
import com.dycm.applib1.model.StockTopic
import com.dycm.applib1.model.StockTopicDataTypeEnum
import com.dycm.applib1.model.StockTsEnum
import com.dycm.applib1.net.IStockNet
import com.dycm.applib1.net.request.RecommendStocklistRequest
import com.dycm.applib1.net.response.RecommendStocklistResponse
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.socket.response.StocksTopicMarketResponse
import com.dycm.applib1.ui.detail.StockDetailLandActivity
import com.dycm.applib1.util.MathUtil
import com.dycm.base2app.Cache
import com.dycm.base2app.adapter.BaseListAdapter
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.network.Network
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment
import com.dycm.base2app.ui.fragment.AbsFragment
import kotlinx.android.synthetic.main.fragment_all_choose_stock.*
import kotlin.math.abs

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc: 自选股列表界面
 */
@Suppress("NAME_SHADOWING")
class TopicStockListFragment : AbsBackFinishNetFragment(), BaseListAdapter.OnClickItemCallback<StockMarketInfo> {

    private var type: StockTsEnum? = null
    private var mAdapter: TopicStocksAdapter? = null
    private var currentPage = 0
    private var pageSize = 20

    companion object {
        fun newInstance(type: StockTsEnum?): TopicStockListFragment {
            val fragment = TopicStockListFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_all_choose_stock

    override fun init() {
        type = arguments?.getSerializable("type") as StockTsEnum?
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        // 设置列表数据适配器
        rv_stock.layoutManager = LinearLayoutManager(context)
        mAdapter = TopicStocksAdapter()
        mAdapter?.setClickItemCallback(this)
        rv_stock.adapter = mAdapter

        requestStocks()
    }

    override fun onClickItem(pos: Int, item: StockMarketInfo?, v: View?) {
        if (item != null) {
            // TODO 跳转到详情页
            startActivity(Intent(context, StockDetailLandActivity::class.java))
        } else {
            // 跳转到搜索
            (parentFragment as AbsFragment).start(TopicStockSearchFragment.newInstance(1))
        }
    }

    /**
     * 加载推荐自选股列表
     */
    private fun requestStocks() {
        val request = RecommendStocklistRequest(type, currentPage, pageSize, transactions.createTransaction())
        Cache[IStockNet::class.java]?.list(request)
            ?.enqueue(Network.IHCallBack<RecommendStocklistResponse>(request))
    }

    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onRecommendStocklistResponse(response: RecommendStocklistResponse) {
        if (mAdapter == null) return

        val datas = response.data?.datas
        if (datas.isNullOrEmpty()) return

        // 显示列表数据
        mAdapter?.addItems(datas)

        // 发起行情订阅
        for (item in datas) {
            val stockTopic = item.ts?.let {
                item.code?.let { it1 ->
                    item.type?.let { it2 ->
                        StockTopic(
                            StockTopicDataTypeEnum.market, it,
                            it1, it2
                        )
                    }
                }
            }
            SocketClient.getInstance().bindTopic(stockTopic)
        }
    }


    @RxSubscribe(observeOnThread = EventThread.MAIN)
    fun onStocksResponse(response: StocksTopicMarketResponse) {
        if (response.body.isNullOrEmpty()) return

        val responseList = response.body
        LogInfra.Log.d(TAG, "onStocksResponse: " + responseList.size)

        if (mAdapter?.items?.isNotEmpty()!!) {
            for (index in mAdapter?.items?.indices!!) {
                val item = mAdapter?.items!![index]
                for (sub in responseList) {
                    if (item.ts == sub.ts && item.code == sub.code) {
                        // 更新数据
                        val item = mAdapter?.items!![index]
                        item.price = sub.price!!
                        item.diffRate =
                            MathUtil.division(abs(sub.price!! - sub.openPrice!!) * 100, sub.openPrice!!)
                        mAdapter?.notifyItemChanged(index)

                        responseList.remove(sub)
                        break
                    }
                }
            }
        }
    }
}