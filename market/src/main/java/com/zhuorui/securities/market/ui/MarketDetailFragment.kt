package com.zhuorui.securities.market.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.StatusBarUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.StockDetailView
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.ui.kline.KlineFragment
import com.zhuorui.securities.market.ui.presenter.MarketDetailPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailViewModel
import com.zhuorui.securities.market.util.MarketUtil
import kotlinx.android.synthetic.main.fragment_market_detail.*
import kotlinx.android.synthetic.main.fragment_market_detail.magic_indicator
import kotlinx.android.synthetic.main.layout_market_detail_bottom.*
import kotlinx.android.synthetic.main.layout_market_detail_topbar.*
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
    MarketDetailView, View.OnClickListener {

    private var mStock: SearchStockInfo? = null

    companion object {
        fun newInstance(stock: SearchStockInfo): MarketDetailFragment {
            val b = Bundle()
            b.putParcelable(SearchStockInfo::class.java.simpleName, stock)
            val fragment = MarketDetailFragment()
            fragment.arguments = b
            return fragment
        }
    }

    private var tabTitle: Array<String> = arrayOf("资金", "资讯", "公告", "F10")
    private val mFragments = arrayOfNulls<AbsFragment<*, *, *, *>>(4)
    private var mIndex = 0

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
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initView()
        setTopbarData()
        presenter?.setLifecycleOwner(this)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        loadRootFragment(R.id.kline_view, KlineFragment())
        presenter?.getData(mStock!!)
    }

    override fun upData(data: StockDetailView.IStockDatailData) {
        stock_detail.setData(data)
    }


    private fun setTopbarData() {
        tv_title.text = String.format("%s(%s)", mStock?.name, mStock?.code)
        var left = MarketUtil.getStockTSIcon(mStock?.ts)
        tv_title.setCompoundDrawablesWithIntrinsicBounds(left, 0, 0, 0)
    }

    override fun upFollow(collected: Boolean) {
        val top = if (collected) R.mipmap.ic_heart_ff8e1b else R.mipmap.ic_heart_c3cde3
        tv_follow.setCompoundDrawablesWithIntrinsicBounds(0,top,0,0)
    }

    /**
     * 更新TopBar数据
     */
    override fun upTopBarInfo(info: String, color: Int) {
        tv_status.text = info
        tv_status.setTextColor(color)
    }

    /**
     * 更新买卖盘档数据
     */
    override fun upBuyingSellingFilesData(
        buy: Float,
        sell: Float,
        buyData: MutableList<Int>,
        sellData: MutableList<Int>
    ) {
        buyingSellingFiles.setData(buy, sell, buyData, sellData)
    }

    /**
     * 更新买卖经纪数据
     */
    override fun upOrderBrokerData(buyData: MutableList<String>, sellData: MutableList<String>) {
        orderBroker.setData(buyData, sellData)
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_back -> {
                pop()
            }
            iv_search -> {
                start(SearchInfoFragment.newInstance())
            }
            tv_simulation_trading -> {
                start(SimulationTradingMainFragment.newInstance(mStock!!))
            }
            tv_remind->{
                val sm = StockMarketInfo()
                sm.name = mStock?.name
                sm.code = mStock?.code
                sm.id = mStock?.id
                sm.ts = mStock?.ts
                sm.tsCode = mStock?.tsCode
                start(RemindSettingFragment.newInstance(sm))
            }
            tv_follow->{
                presenter?.collectionStock(mStock!!)
            }
        }

    }

    private fun initView() {
        top_bar.setPadding(0, StatusBarUtil.getStatusBarHeight(context), 0, 0)
        iv_back.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        tv_simulation_trading.setOnClickListener(this)
        tv_remind.setOnClickListener(this)
        tv_follow.setOnClickListener(this)
        magic_indicator.navigator = getNavigator()
        top_magic_indicator.navigator = getNavigator()
        scroll_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            //topTab
            if (magic_indicator != null) {
                top_magic_indicator_group?.visibility = if (scrollY < magic_indicator.top) View.GONE else View.VISIBLE
            }
            //title
            if (scrollY > 10 && presenter?.getTopBarOnfoType() != 1) {
                presenter?.getTopBarPriceInfo()
            } else if (scrollY < 10 && presenter?.getTopBarOnfoType() != 0) {
                presenter?.getTopBarStockStatusInfo()
            }
        }
    }

    /**
     * 切换资金，资讯等信息
     */
    private fun onSelect(index: Int) {
        magic_indicator.onPageSelected(index)
        magic_indicator.onPageScrolled(index, 0.0F, 0)
        top_magic_indicator.onPageSelected(index)
        top_magic_indicator.onPageScrolled(index, 0.0F, 0)
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val firstFragment = findChildFragment(MarketDetailCapitalFragment::class.java)
        if (firstFragment == null) {
            mFragments[0] = MarketDetailCapitalFragment.newInstance()
            mFragments[1] = MarketDetailInformationFragment.newInstance()
            mFragments[2] = MarketDetailNoticeFragment.newInstance()
            mFragments[3] = MarketDetailF10Fragment.newInstance()
            loadMultipleRootFragment(
                R.id.fl_tab_container, mIndex,
                mFragments[0],
                mFragments[1],
                mFragments[2],
                mFragments[3]
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[0] = firstFragment
            mFragments[1] = findChildFragment(MarketDetailInformationFragment::class.java)
            mFragments[2] = findChildFragment(MarketDetailNoticeFragment::class.java)
            mFragments[3] = findChildFragment(MarketDetailF10Fragment::class.java)
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
                return tabTitle.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.parseColor("#C0CCE0")
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = tabTitle[index]
                colorTransitionPagerTitleView.setOnClickListener {
                    onSelect(index)
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ResUtil.getColor(com.zhuorui.securities.market.R.color.tab_select))
                indicator.lineHeight = ResUtil.getDimensionDp2Px(2f).toFloat()
                indicator.lineWidth = ResUtil.getDimensionDp2Px(33f).toFloat()
                return indicator
            }
        }
        return commonNavigator
    }

}