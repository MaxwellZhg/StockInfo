package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zhuorui.securities.base2app.infra.LogInfra
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketStockConsBinding
import com.zhuorui.securities.market.event.StockConsPointStateEvent
import com.zhuorui.securities.market.event.StockConsStateEvent
import com.zhuorui.securities.market.model.StockConsInfoModel
import com.zhuorui.securities.market.net.response.StockConsInfoResponse
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.presenter.MarketStockConsPresenter
import com.zhuorui.securities.market.ui.view.MarketStockConsView
import com.zhuorui.securities.market.ui.viewmodel.MarketStockConsViewModel
import kotlinx.android.synthetic.main.fragment_all_choose_stock.*
import kotlinx.android.synthetic.main.fragment_market_point.*
import kotlinx.android.synthetic.main.fragment_market_point_cons_info.*
import kotlinx.android.synthetic.main.fragment_market_stock_cons.*
import kotlinx.android.synthetic.main.fragment_market_stock_cons.empty_view
import kotlinx.android.synthetic.main.layout_market_point_view_tips.*
import kotlinx.android.synthetic.main.layout_market_point_view_tips.tv_up_down_count
import kotlinx.android.synthetic.main.layout_market_point_view_tips.tv_up_down_rate

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/11/20
 * Desc:
 */
class MarketStockConsFragment :AbsSwipeBackNetFragment<FragmentMarketStockConsBinding,MarketStockConsViewModel,MarketStockConsView,MarketStockConsPresenter>(),
    MarketStockConsView,MarketPointConsInfoAdapter.OnCombineInfoClickListener,View.OnClickListener{
    private var infoadapter: MarketPointConsInfoAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_market_stock_cons
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketStockConsPresenter
        get() = MarketStockConsPresenter()
    override val createViewModel: MarketStockConsViewModel?
        get() = ViewModelProviders.of(this).get(MarketStockConsViewModel::class.java)
    override val getView: MarketStockConsView
        get() = this
    private var priceSelect = false
    private var rateSelect = false
    private var countSelect = false
    companion object {
        fun newInstance(): MarketStockConsFragment {
            return MarketStockConsFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
       // presenter?.getStockConsInfo()
        presenter?.setLifecycleOwner(this)
        infoadapter = presenter?.getMarketInfoAdapter()
        infoadapter?.onCombineInfoClickListener=this
        rv_point_stock.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        (rv_point_stock.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        rv_point_stock.adapter = infoadapter
        //解决数据加载不完的问题
        infoadapter?.notifyDataSetChanged()
        tv_up_down_price.setOnClickListener(this)
        tv_up_down_rate.setOnClickListener(this)
        tv_up_down_count.setOnClickListener(this)

    }

    override fun addInfoToAdapter(list: List<StockConsInfoModel>) {
        empty_view.visibility=View.INVISIBLE
        infoadapter?.clearItems()
        if (infoadapter?.items == null) {
            infoadapter?.items = ArrayList()
        }
        infoadapter?.addItems(list)
        infoadapter?.notifyDataSetChanged()
    }
    override fun onCombineClick() {
        ToastUtil.instance.toastCenter("成分股")
    }
    override fun showStateInfo(state: Int) {
       when(state){
           1->{
               priceSelect=true
               rateSelect = false
               countSelect = false
               detailViewState(priceSelect,tv_up_down_price)
           }
           2->{
               priceSelect=false
               rateSelect = false
               countSelect = false
               detailViewState(priceSelect,tv_up_down_price)
           }
           3->{
               priceSelect=false
               rateSelect = true
               countSelect = false
               detailViewState(rateSelect,tv_up_down_rate)
           }
           4->{
               priceSelect=false
               rateSelect = false
               countSelect = false
               detailViewState(rateSelect,tv_up_down_rate)
           }
           5->{
               priceSelect=false
               rateSelect = false
               countSelect = true
               detailViewState(countSelect,tv_up_down_count)
           }
           6->{
               priceSelect=false
               rateSelect = false
               countSelect = false
               detailViewState(countSelect,tv_up_down_count)
           }
       }
    }

    private fun detailViewState(selectState:Boolean, view:View){
        when (view) {
            tv_up_down_price -> {
                if(selectState) {
                    tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                }else {
                    tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                }
                tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
                tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
            }
            tv_up_down_rate -> {
                if(selectState) {
                    tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                }else {
                    tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                }
                tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
                tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
            }
            else -> {
                if(selectState) {
                    tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                }else {
                    tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                }
                tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
                tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
            }
        }

    }

    override fun onClick(p0: View?) {
       when(p0){
           tv_up_down_price->{
               detailShowTypeInfo(priceSelect,1,tv_up_down_price)
               priceSelect =!priceSelect
           }
           tv_up_down_rate->{
               detailShowTypeInfo(rateSelect,2,tv_up_down_rate)
               rateSelect =!rateSelect
           }
           tv_up_down_count->{
               detailShowTypeInfo(countSelect,3,tv_up_down_count)
               countSelect =!countSelect
           }
       }
    }

    private fun detailShowTypeInfo(selectInfo:Boolean, type:Int, view:View){
        when(type){
            1->{
                rateSelect=false
                countSelect=false
                showViewInfo(selectInfo,view)
            }
            2->{
                priceSelect=false
                countSelect=false
                showViewInfo(selectInfo,view)
            }
            3->{
                priceSelect=false
                rateSelect=false
                showViewInfo(selectInfo,view)
            }
        }
    }

    private fun showViewInfo(selectInfo :Boolean, view:View){
        detailViewInfo(view,selectInfo)
    }

    private fun detailViewInfo(view:View, infoState:Boolean){
        when (view) {
            tv_up_down_price -> {
                if(infoState) {
                    tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                    RxBus.getDefault().post(StockConsPointStateEvent(1))
                }else {
                    tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                    RxBus.getDefault().post(StockConsPointStateEvent(2))
                }
                tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
                tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
            }
            tv_up_down_rate -> {
                if(infoState) {
                    tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                    RxBus.getDefault().post(StockConsPointStateEvent(3))
                }else {
                    tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                    RxBus.getDefault().post(StockConsPointStateEvent(4))
                }
                tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
                tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
            }
            else -> {
                if(infoState) {
                    tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                    RxBus.getDefault().post(StockConsPointStateEvent(5))

                }else {
                    tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                    RxBus.getDefault().post(StockConsPointStateEvent(6))
                }
                tv_up_down_price.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
                tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_up_rate,0)
            }
        }

    }

    override fun showErrorState() {
        if(infoadapter?.itemCount!! ==0) {
            empty_view.visibility = View.VISIBLE
        }
    }

    override fun notifyItemChanged(index: Int) {
        _mActivity?.runOnUiThread { infoadapter?.notifyItemChanged(index) }
    }





}