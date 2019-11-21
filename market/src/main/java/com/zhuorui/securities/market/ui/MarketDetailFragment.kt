package com.zhuorui.securities.market.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.StockDetailView
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.model.OrderBrokerModel
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.socket.vo.OrderData
import com.zhuorui.securities.market.socket.vo.StockHandicapData
import com.zhuorui.securities.market.ui.kline.KlineFragment
import com.zhuorui.securities.market.ui.kline.KlineLandFragment
import com.zhuorui.securities.market.ui.presenter.MarketDetailPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailViewModel
import com.zhuorui.securities.market.util.MarketUtil
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.ui.LoginRegisterFragment
import kotlinx.android.synthetic.main.fragment_market_detail.*
import kotlinx.android.synthetic.main.layout_hk_bmp_tip.*
import kotlinx.android.synthetic.main.layout_market_detail_bottom.*
import kotlinx.android.synthetic.main.layout_network_unavailable_tips.*
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-11 15:46
 *    desc   : 股票行情页面
 */
class MarketDetailFragment :
    AbsSwipeBackNetFragment<FragmentMarketDetailBinding, MarketDetailViewModel, MarketDetailView, MarketDetailPresenter>(),
    MarketDetailView, View.OnClickListener, NestedScrollView.OnScrollChangeListener, OnRefreshListener,
    AbsActivity.OnOrientationChangedListener {

    private var mStock: SearchStockInfo = SearchStockInfo()
    private var tabTitle: Array<String>? = null
    private val mFragments = arrayOfNulls<SupportFragment>(6)
    private var mIndexFragment: SupportFragment? = null //指数Fragment
    private var loading: ProgressDialog? = null
    private var mIndex = 0
    private var inNum = 0
    private var mBMP = false
    private var lazyInit = false

    companion object {
        fun newInstance(stock: SearchStockInfo): MarketDetailFragment {
            val b = Bundle()
            b.putParcelable(SearchStockInfo::class.java.simpleName, stock)
            val fragment = MarketDetailFragment()
            fragment.arguments = b
            return fragment
        }
    }

    override val layout: Int
        get() = R.layout.fragment_market_detail

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MarketDetailPresenter
        get() = MarketDetailPresenter()

    override val createViewModel: MarketDetailViewModel?
        get() = ViewModelProviders.of(this).get(MarketDetailViewModel::class.java)

    override val getView: MarketDetailView
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStock = arguments?.getParcelable(SearchStockInfo::class.java.simpleName)!!
        tabTitle = ResUtil.getStringArray(R.array.stock_detail_tab)
        mBMP = MarketUtil.isBMP(mStock?.ts)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initView()
        top_bar.setStockInfo(mStock.ts, mStock.name, mStock.code)
        presenter?.setStockInfo(mStock.ts!!, mStock.code!!, mStock.type!!)
        presenter?.getTopBarStockStatusInfo()
        presenter?.setLifecycleOwner(this)
        toggleScreenOrientation(true)
        lazyInit = true
    }

    private fun initView() {
        if (mBMP) {
            buyingSellingFiles.visibility = View.GONE
            orderBroker.visibility = View.GONE
            bmp_tip.visibility = View.VISIBLE
            sm_refrsh.setOnRefreshListener(this)
            sm_refrsh.setEnableRefresh(true)
        }
        top_bar.setRefreshButton(mBMP)
        tv_simulation_trading.setOnClickListener(this)
        tv_remind.setOnClickListener(this)
        tv_follow.setOnClickListener(this)
        btn_check_netwoke.setOnClickListener(this)
        close_bmp_tip.setOnClickListener(this)
        scroll_view.setOnScrollChangeListener(this)
        top_bar.setRefreshClickListener { onRefresh(sm_refrsh) }
        top_bar.setSearchClickListener { start(SearchInfoFragment.newInstance()) }
    }

    override fun onClick(v: View?) {
        if (v == tv_follow) {
            presenter?.collectionStock(mStock)
        } else if (v == btn_check_netwoke) {
            context!!.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        } else if (v == close_bmp_tip) {
            bmp_tip.visibility = View.GONE
        } else if (!LocalAccountConfig.getInstance().isLogin()) {
            start(LoginRegisterFragment.newInstance(1))
        } else {
            when (v) {
                tv_simulation_trading -> {
                    start(SimulationTradingMainFragment.newInstance(mStock))
                }
                tv_remind -> {
                    val sm = StockMarketInfo()
                    sm.name = mStock.name
                    sm.code = mStock.code
//                    sm.id = mStock.id
                    sm.ts = mStock.ts
                    sm.tsCode = mStock.tsCode
                    start(RemindSettingFragment.newInstance(sm))
                }
            }
        }

    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        loadFragment()
        getData()
        inNum = 1
    }

    override fun onChange(landscape: Boolean) {
        if (landscape && topFragment == this) {
            start(
                KlineLandFragment.newInstance(
                    mStock.ts ?: "",
                    mStock.code ?: "",
                    mStock.tsCode ?: mStock.code + "." + mStock.ts,
                    mStock.type ?: 2
                )
            )
        }
    }

    /**
     * 加载界面中的fragment
     */
    private fun loadFragment() {
        //加载K线Fragment
        loadRootFragment(
            R.id.kline_view,
            KlineFragment.newInstance(
                mStock.ts ?: "",
                mStock.code ?: "",
                mStock.tsCode ?: mStock.code + "." + mStock.ts,
                mStock.type ?: 2,
                false
            )
        )
        //加载指数Fragment
        if (!mBMP) {
            loadIndexFragment()
        }
        initTabFragment()
        tab_magic_indicator.navigator = getNavigator()
        top_magic_indicator.navigator = getNavigator()
    }

    /**
     * 加载指数Fragment
     */
    private fun loadIndexFragment() {
        mIndexFragment = MarketIndexFragment.newInstance()
        loadRootFragment(R.id.index_view, mIndexFragment)

    }

    fun showLoading() {
        if (loading == null) {
            loading = context?.let { ProgressDialog(it) }
        }
        if (!loading!!.isShowing) {
            loading?.show()
        }
    }

    fun hideLoading() {
        if (loading != null && loading!!.isShowing)
            loading?.dismiss()
    }

    private fun getData() {
        //加载数据
        presenter?.getData(mBMP)
        //资金加载数据
        val fragment = mFragments[0]
        if (fragment != null) {
            (fragment as MarketDetailCapitalFragment).getData()
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        showLoading()
        getData()
    }

    /**
     * 长连接连接状态
     */
    override fun updateNetworkState(connected: Boolean) {
        network_unavailable_tips.visibility = if (connected) View.GONE else View.VISIBLE
    }

    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        //topTab
        if (tab_magic_indicator != null) {
            top_magic_indicator.visibility = if (scrollY < tab_magic_indicator.top) View.GONE else View.VISIBLE
            top_magic_indicator_line.visibility = top_magic_indicator.visibility
        }
        //title
        if (scrollY > 10 && presenter?.getTopBarOnfoType() != 1) {
            presenter?.getTopBarPriceInfo()
        } else if (scrollY < 10 && presenter?.getTopBarOnfoType() != 0) {
            presenter?.getTopBarStockStatusInfo()
        }
    }


    override fun setData(data: StockHandicapData?) {
        stock_detail?.setData(data)
        buyingSellingFiles.setPreClosePrice(data?.preClose ?: 0f)
        hideLoading()
        if (!sm_refrsh.state.isFinishing) {
            sm_refrsh.finishRefresh()
        }
    }

    /**
     * 更新数据
     */
    override fun upData(data: StockHandicapData?) {
        stock_detail?.upData(data)
    }

    /**
     * 更新关注icon
     */
    override fun upFollow(collected: Boolean) {
        val top = if (collected) R.mipmap.ic_heart_ff8e1b else R.mipmap.ic_heart_c3cde3
        tv_follow.setCompoundDrawablesWithIntrinsicBounds(0, top, 0, 0)
    }

    /**
     * 更新TopBar数据
     */
    override fun upTopBarInfo(info: String, color: Int) {
        top_bar.setStatus(info, color)
    }

    /**
     * 更新买卖盘档数据
     */
    override fun upBuyingSellingFilesData(
        asklist: List<OrderData.AskBidModel>?,
        bidlist: List<OrderData.AskBidModel>?
    ) {
        buyingSellingFiles.setData(asklist, bidlist)
    }

    /**
     * 更新买卖经纪数据
     */
    override fun upOrderBrokerData(buyData: MutableList<OrderBrokerModel>, sellData: MutableList<OrderBrokerModel>) {
        orderBroker.setData(buyData, sellData)
    }


    /**
     * 切换资金，资讯等信息
     */
    private fun onSelect(index: Int) {
        tab_magic_indicator.onPageSelected(index)
        tab_magic_indicator.onPageScrolled(index, 0.0F, 0)
        top_magic_indicator.onPageSelected(index)
        top_magic_indicator.onPageScrolled(index, 0.0F, 0)
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }

    private fun initTabFragment() {
        val firstFragment = findChildFragment(MarketDetailCapitalFragment::class.java)
        if (firstFragment == null) {
            mFragments[0] = MarketDetailCapitalFragment.newInstance(mStock.ts.toString(),mStock.code.toString())
            mFragments[1] = mStock?.code?.let { MarketDetailInformationFragment.newInstance(it) }
            mFragments[2] = mStock?.code?.let { MarketDetailInformationFragment.newInstance(it) }
            mFragments[3] = mStock?.code?.let { MarketDetailNoticeFragment.newInstance(it) }
            mFragments[4] = MarketDetailF10BriefFragment.newInstance(mStock)
            mFragments[5] = MarketDetailF10FinancialFragment.newInstance(mStock)
            loadMultipleRootFragment(
                R.id.fl_tab_container, mIndex,
                mFragments[0],
                mFragments[1],
                mFragments[2],
                mFragments[3],
                mFragments[4],
                mFragments[5]
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用、
            mFragments[0] = firstFragment
            mFragments[1] = findChildFragment(MarketDetailInformationFragment::class.java)
            mFragments[2] = findChildFragment(MarketDetailInformationFragment::class.java)
            mFragments[3] = findChildFragment(MarketDetailNoticeFragment::class.java)
            mFragments[4] = findChildFragment(MarketDetailF10BriefFragment::class.java)
            mFragments[5] = findChildFragment(MarketDetailF10FinancialFragment::class.java)
        }
    }


    /**
     * 获取指示器适配器
     */
    private fun getNavigator(): IPagerNavigator? {
        // 设置viewpager指示器
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return if (tabTitle != null) tabTitle!!.size else 0
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.parseColor("#C0CCE0")
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = tabTitle?.get(index) ?: ""
                colorTransitionPagerTitleView.setOnClickListener {
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

    override fun onSupportVisible() {
        super.onSupportVisible()

        if (lazyInit) {
            // 当前界面每次重新可见时，需设置activity允许旋屏
            toggleScreenOrientation(true)
        }

        if (inNum > 0) {
            //每次重新进入界面检查bmp状态
            inNum++
            val bmp = MarketUtil.isBMP(mStock.ts)
            if (mBMP != bmp) {
                mBMP = bmp
                getData()
                cahngeBMP()
            }
        }
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()

        // 当前界面不可见时，获取栈顶的fragment若不是当前界面也不是横屏K线界面时，说明已跳到其他界面，需设置activity不允许旋屏
        if (topFragment != this && topFragment.javaClass.name != KlineLandFragment::class.java.name) {
            toggleScreenOrientation(false)
        }
    }

    /**
     * 切换是否允许横屏
     */
    private fun toggleScreenOrientation(allowLandscape: Boolean) {
        if (allowLandscape) {
            // 添加setRequestedOrientation方法实现屏幕允许旋转
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            (activity as AbsActivity).addOrientationChangedListener(this)
        } else {
            // 添加setRequestedOrientation方法实现屏幕不允许旋转
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            (activity as AbsActivity).removeOrientationChangedListener(this)
        }
    }

    /**
     * bmp 状态发生改变 更新界面
     */
    private fun cahngeBMP() {
        if (mBMP) {
            if (mIndexFragment != null)
                childFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(mIndexFragment!!)
            index_view.removeAllViews()
            buyingSellingFiles.visibility = View.GONE
            orderBroker.visibility = View.GONE
            bmp_tip.visibility = View.VISIBLE
        } else {
            loadIndexFragment()
            buyingSellingFiles.visibility = View.VISIBLE
            orderBroker.visibility = View.VISIBLE
            bmp_tip.visibility = View.GONE
        }
        top_bar.setRefreshButton(mBMP)
        sm_refrsh.setEnableRefresh(mBMP)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleScreenOrientation(false)
    }
}