package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketPointBinding
import com.zhuorui.securities.market.event.StockConsStateEvent
import com.zhuorui.securities.market.manager.StockIndexHandicapDataManager
import com.zhuorui.securities.market.model.PushIndexHandicapData
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.kline.KlineFragment
import com.zhuorui.securities.market.ui.presenter.MarketPointPresenter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel
import kotlinx.android.synthetic.main.fragment_market_point.*
import kotlinx.android.synthetic.main.layout_market_point_topbar.*
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:指数模块
 * */
class MarketPointFragment :
    AbsSwipeBackNetFragment<FragmentMarketPointBinding, MarketPointViewModel, MarketPointView, MarketPointPresenter>(),
    MarketPointView, View.OnClickListener, OnRefreshLoadMoreListener {
    private var type: Int = -1
    private var infoadapter: MarketPointConsInfoAdapter? = null
    private var pointInfoAdapter: MarketPointInfoAdapter? = null
    private var tabTitle: ArrayList<String> = ArrayList()
    private val mFragments = arrayOfNulls<SupportFragment>(2)
    private var mIndex = 0
    private var consStockPage = 20
    private var allCount = 0
    private var consInfoPage = 0
    private var priceSelect = false
    private var rateSelect = false
    private var countSelect = false
    private var isInfo = false
    private var sort = 3
    private var sortType = 1
    private var code: String = ""
    override val layout: Int
        get() = R.layout.fragment_market_point
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketPointPresenter
        get() = MarketPointPresenter()
    override val createViewModel: MarketPointViewModel?
        get() = ViewModelProviders.of(this).get(MarketPointViewModel::class.java)
    override val getView: MarketPointView
        get() = this

    companion object {
        fun newInstance(type: Int): MarketPointFragment {
            val fragment = MarketPointFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putInt("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getInt("type") ?:type
        when (type) {
            1 -> {
                code = "HSI"
                tv_title.text = "恒生指数（HSI）"
            }
            2 -> {
                code = "HSCEI"
                tv_title.text = "国企指数（HSCEI）"
            }
            3 -> {
                code = "HSCCI"
                tv_title.text = "红筹指数（HSCCI）"
            }
        }
        tabTitle.add("成分股")
        tabTitle.add("资讯")
        iv_back.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        tv_newly_price.setOnClickListener(this)
        tv_up_down_rate.setOnClickListener(this)
        tv_up_down_count.setOnClickListener(this)
        // loadRootFragment(R.id.kline_view, MarketPointKlineFragment.newInstance("","","",false))
        scroll_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (magic_indicator != null) {
                top_magic_indicator?.visibility = if (scrollY < magic_indicator.top) View.GONE else View.VISIBLE
                if (mIndex == 0) {
                    ll_selcet_info.visibility = if (scrollY < magic_indicator.top) View.GONE else View.VISIBLE
                } else {
                    ll_selcet_info.visibility = View.GONE
                }
            }
        }
        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnLoadMoreListener(this)
        refresh_layout.setOnRefreshListener(this)
        if (StockIndexHandicapDataManager.getInstance(code, "HK", 1).indexData!=null) {
            market_point_view.setData(StockIndexHandicapDataManager.getInstance(code, "HK", 1).indexData)
            presenter?.bindMarketPointhandicap("HK",code)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_back -> {
                pop()
            }
            iv_search -> {
                start(SearchInfoFragment.newInstance())
            }
            tv_newly_price -> {
                consStockPage = 20
                refresh_layout.finishLoadMore(true)
                refresh_layout.setEnableLoadMore(true)
                refresh_layout.setNoMoreData(false)
                detailShowTypeInfo(priceSelect, 1, tv_newly_price)
                priceSelect = !priceSelect
            }
            tv_up_down_rate -> {
                consStockPage = 20
                refresh_layout.finishLoadMore(true)
                refresh_layout.setEnableLoadMore(true)
                refresh_layout.setNoMoreData(false)
                detailShowTypeInfo(rateSelect, 2, tv_up_down_rate)
                rateSelect = !rateSelect
            }
            tv_up_down_count -> {
                consStockPage = 20
                refresh_layout.finishLoadMore(true)
                refresh_layout.setEnableLoadMore(true)
                refresh_layout.setNoMoreData(false)
                detailShowTypeInfo(countSelect, 3, tv_up_down_count)
                countSelect = !countSelect
            }
        }

    }

    private fun detailShowTypeInfo(selectInfo: Boolean, type: Int, view: View) {
        when (type) {
            1 -> {
                rateSelect = false
                countSelect = false
                showViewInfo(selectInfo, view)
            }
            2 -> {
                priceSelect = false
                countSelect = false
                showViewInfo(selectInfo, view)
            }
            3 -> {
                priceSelect = false
                rateSelect = false
                showViewInfo(selectInfo, view)
            }
        }
    }

    private fun showViewInfo(selectInfo: Boolean, view: View) {
        detailViewInfo(view, selectInfo)
    }

    private fun detailViewInfo(view: View, infoState: Boolean) {
        when (view) {
            tv_newly_price -> {
                if (infoState) {
                    sort = 1
                    sortType = 1
                    tv_newly_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                    RxBus.getDefault().post(StockConsStateEvent(1))
                } else {
                    sort = 1
                    sortType = 2
                    tv_newly_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                    RxBus.getDefault().post(StockConsStateEvent(2))
                }
                tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_rate, 0)
                tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_rate, 0)
                presenter?.getStockConsInfo(code, consStockPage, sort, sortType, true, false,isInfo)
            }
            tv_up_down_rate -> {
                if (infoState) {
                    sort = 3
                    sortType = 1
                    tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                    RxBus.getDefault().post(StockConsStateEvent(3))
                } else {
                    sort = 3
                    sortType = 2
                    tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                    RxBus.getDefault().post(StockConsStateEvent(4))
                }
                tv_newly_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_rate, 0)
                tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_rate, 0)
                presenter?.getStockConsInfo(code, consStockPage, sort, sortType, true, false,isInfo)
            }
            else -> {
                if (infoState) {
                    sort = 2
                    sortType = 1
                    tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_price, 0)
                    RxBus.getDefault().post(StockConsStateEvent(5))

                } else {
                    sort = 2
                    sortType = 2
                    tv_up_down_count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_down_price, 0)
                    RxBus.getDefault().post(StockConsStateEvent(6))
                }
                tv_newly_price.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_rate, 0)
                tv_up_down_rate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_up_rate, 0)
                presenter?.getStockConsInfo(code, consStockPage, sort, sortType, true, false,isInfo)
            }
        }

    }


    /**
     * 获取指示器适配器
     */
    private fun getNavigator(): IPagerNavigator? {
        // 设置viewpager指示器
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.isAdjustMode = false
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return tabTitle?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor =
                    ResUtil.getColor(R.color.color_B3BCD0)!!
                colorTransitionPagerTitleView.selectedColor =
                    ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = tabTitle!![index]
                colorTransitionPagerTitleView.setOnClickListener {
                    magic_indicator.onPageSelected(index)
                    magic_indicator.onPageScrolled(index, 0.0F, 0)
                    top_magic_indicator.onPageSelected(index)
                    top_magic_indicator.onPageScrolled(index, 0.0F, 0)
                    onSelect(index)
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ResUtil.getColor(R.color.tab_select))
                indicator.lineHeight = ResUtil.getDimensionDp2Px(2f).toFloat()
                indicator.lineWidth = ResUtil.getDimensionDp2Px(33f).toFloat()
                return indicator
            }
        }
        return commonNavigator
    }

    private fun onSelect(index: Int) {
        if (index == 0) {
            ll_selcet_info.visibility = View.GONE
            refresh_layout.setEnableLoadMore(true)
            isInfo =false
        } else {
            ll_selcet_info.visibility = if (scroll_view.scrollY < magic_indicator.top) View.GONE else View.VISIBLE
            refresh_layout.setEnableLoadMore(false)
            isInfo=true
        }
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }


    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        loadFragment()
    }

    /**
     * 加载界面中的fragment
     */
    private fun loadFragment() {
        //加载K线Fragment
        initTabFragment()
        loadRootFragment(
            R.id.kline_view,
            KlineFragment.newInstance(
                "HK",
                code,
                "$code.HK",
                1,
                false
            )
        )

        magic_indicator.navigator = getNavigator()
        top_magic_indicator.navigator = getNavigator()
        showHideFragment(mFragments[0], mFragments[1])
        presenter?.getStockConsInfo(code, 20, 3, 1, false, true,isInfo)
    }

    private fun initTabFragment() {
        val firstFragment = findChildFragment(MarketStockConsFragment::class.java)
        if (firstFragment == null) {
            mFragments[0] = MarketStockConsFragment.newInstance()
            mFragments[1] = MarketPointConsInfoFragment.newInstance(code)

            loadMultipleRootFragment(
                R.id.fl_tab_container, mIndex,
                mFragments[0],
                mFragments[1]
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用、
            mFragments[0] = firstFragment
            mFragments[1] = findChildFragment(MarketPointConsInfoFragment::class.java)
        }
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        consStockPage += 20
        if (allCount != 0 && consStockPage > allCount) {
            consStockPage = allCount
            presenter?.getStockConsInfo(code, consStockPage, sort, sortType, false, false,isInfo)
        } else {
            presenter?.getStockConsInfo(code, consStockPage, sort, sortType, false, false,isInfo)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        consStockPage = 20
        presenter?.getStockConsInfo(code, consStockPage, sort, sortType, true, false,isInfo)
    }

    override fun loadMoreSuccess() {
        if (consStockPage == allCount) {
            refresh_layout.setNoMoreData(true)
        } else {
            refresh_layout.finishLoadMore(true)
        }
    }

    override fun showAllCount(count: Int) {
        allCount = count
    }

    override fun refreshSuccess() {
        refresh_layout.finishRefresh(true)
        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setNoMoreData(false)
    }

    override fun showStateChangeEvent(state: Int) {
        refresh_layout.setEnableLoadMore(true)
        refresh_layout.setNoMoreData(false)
        consStockPage = 20
        when (state) {
            1 -> {
                sort = 1
                sortType = 1
            }
            2 -> {
                sort = 1
                sortType = 2
            }
            3 -> {
                sort = 3
                sortType = 1
            }
            4 -> {
                sort = 3
                sortType = 2
            }
            5 -> {
                sort = 2
                sortType = 1
            }
            6 -> {
                sort = 2
                sortType = 2
            }

        }
        presenter?.getStockConsInfo(code, consStockPage, sort, sortType, false, false,isInfo)
    }

    override fun setLoadMoreState() {
        refresh_layout.setNoMoreData(false)
        refresh_layout.setEnableLoadMore(true)
    }

    override fun loadConsStockFail() {
        refresh_layout.finishLoadMore(false)
    }


    override fun getpushData(data: PushIndexHandicapData) {
        market_point_view.upPushData(data)
    }
    override fun loadConsFreshFail() {
        refresh_layout.finishRefresh(false)
    }

    override fun detailInfoState() {
        refresh_layout.finishRefresh(true)
        refresh_layout.setEnableLoadMore(false)
    }

}