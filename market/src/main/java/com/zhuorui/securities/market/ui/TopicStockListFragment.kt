package com.zhuorui.securities.market.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.event.AddTopicStockEvent
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.model.StockTopic
import com.zhuorui.securities.market.model.StockTopicDataTypeEnum
import com.zhuorui.securities.market.model.StockTsEnum
import com.zhuorui.securities.market.net.IStockNet
import com.zhuorui.securities.market.net.request.RecommendStocklistRequest
import com.zhuorui.securities.market.net.response.RecommendStocklistResponse
import com.zhuorui.securities.market.socket.SocketClient
import com.zhuorui.securities.market.socket.push.StocksTopicMarketResponse
import com.zhuorui.securities.market.ui.detail.StockDetailLandActivity
import com.zhuorui.securities.market.util.MathUtil
import com.zhuorui.securities.base2app.Cache
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.network.Network
import com.zhuorui.securities.base2app.rxbus.EventThread
import com.zhuorui.securities.base2app.rxbus.RxSubscribe
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_all_choose_stock.*
import java.util.*
import kotlin.math.abs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zhuorui.securities.market.databinding.FragmentAllChooseStockBinding
import com.zhuorui.securities.market.ui.contract.TopicStockListContract
import com.zhuorui.securities.market.ui.mvp.TopicListStockVmWarpper
import com.zhuorui.securities.market.ui.mvp.TopicStockListPresenter
import com.zhuorui.securities.base2app.mvp.wrapper.BaseMvpNetVmFragment

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc: 自选股列表界面
 */
@Suppress("NAME_SHADOWING")
class TopicStockListFragment : BaseMvpNetVmFragment<TopicStockListPresenter,TopicListStockVmWarpper,FragmentAllChooseStockBinding>(), BaseListAdapter.OnClickItemCallback<StockMarketInfo>,
    View.OnClickListener,TopicStockListContract.View {
    private var type: StockTsEnum? = null
    private var mAdapter: TopicStocksAdapter? = null
    private var currentPage = 0
    private var pageSize = 20
    private var disposables = LinkedList<Disposable>()
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
        rl_updown.setOnClickListener(this)
        rl_arrows.setOnClickListener(this)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        // 设置列表数据适配器
        (dataBinding.rvStock.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        dataBinding.rvStock.layoutManager = LinearLayoutManager(context)
        mAdapter = TopicStocksAdapter()
        mAdapter?.setClickItemCallback(this)
        dataBinding.rvStock.adapter = mAdapter

        requestStocks()
    }

    override fun onClickItem(pos: Int, item: StockMarketInfo?, v: View?) {
        if (item != null) {
            // TODO 跳转到详情页
            startActivity(Intent(context, StockDetailLandActivity::class.java))
        } else {
            // 跳转到搜索
            (parentFragment as AbsFragment).start(StockSearchFragment.newInstance(1))
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
        if (mAdapter == null) return

        val adapterData = mAdapter?.items
        if (adapterData?.isNullOrEmpty()!!) return

        val stockMarketData = response.body

        for (index in mAdapter?.items?.indices!!) {
            val item = mAdapter?.items!![index]
            if (item.ts == stockMarketData.ts && item.code == stockMarketData.code) {
                // 更新数据
                val item = mAdapter?.items!![index]
                item.price = stockMarketData.price!!
                item.diffRate =
                    MathUtil.division(
                        abs(stockMarketData.price!! - stockMarketData.openPrice!!) * 100,
                        stockMarketData.openPrice!!
                    )
                mAdapter?.notifyItemChanged(index)
                break
            }
        }
    }

    /**
     * 添加自选股
     */
    @RxSubscribe(observeOnThread = EventThread.COMPUTATION)
    fun onAddTopicStockEvent(event: AddTopicStockEvent) {
        if (type != null && !event.stock.ts.equals(type?.name)) return

//        for (item in mAdapter?.items!!) {
//            if (item.ts.equals(event.stock.ts) && item.code.equals(event.stock.ts)) return
//        }

        // 发起订阅行情
        val stockTopic = event.stock.ts?.let {
            event.stock.code?.let { it1 ->
                event.stock.type?.let { it2 ->
                    StockTopic(
                        StockTopicDataTypeEnum.market,
                        it, it1, it2
                    )
                }
            }
        }
        SocketClient.getInstance().bindTopic(stockTopic)

        // 显示新添加的自选股
        val stock = StockMarketInfo()
        stock.ts = event.stock.ts
        stock.code = event.stock.code
        stock.name = event.stock.name
        stock.type = event.stock.type
        stock.tsCode = event.stock.tsCode

        val disposable = Observable.create(ObservableOnSubscribe<Boolean> { emitter ->
            mAdapter?.addItem(stock)
            emitter.onNext(true)
            emitter.onComplete()
        }).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe()
        disposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()

        // 释放disposable
        if (disposables.isNullOrEmpty()) return
        for (disposable in disposables) {
            disposable.dispose()
        }
        disposables.clear()
    }
    override fun onClick(p0: View?) {
      when(p0?.id){
          R.id.rl_updown->{

          }
          R.id.rl_arrows->{

          }
      }
    }

    override fun createPresenter(): TopicStockListPresenter {
        return TopicStockListPresenter(this)
    }

    override fun isDestroyed(): Boolean {
        return  false
    }

    override fun createWrapper(): TopicListStockVmWarpper {
        return TopicListStockVmWarpper(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = generateDataBinding(inflater,container,layout)
        if (viewWrapper != null) {
            viewWrapper.setBinding(dataBinding)
        }
        presenter.fetchData()
        return dataBinding.root
    }


}