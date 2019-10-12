package com.zhuorui.securities.market.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.StatusBarUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketDetailBinding
import com.zhuorui.securities.market.ui.presenter.MarketDetailPresenter
import com.zhuorui.securities.market.ui.view.MarketDetailView
import com.zhuorui.securities.market.ui.viewmodel.MarketDetailViewModel
import kotlinx.android.synthetic.main.fragment_market_detail.*
import kotlinx.android.synthetic.main.fragment_market_detail.magic_indicator
import kotlinx.android.synthetic.main.layout_market_detail_topbar.*
import kotlinx.android.synthetic.main.layout_simulation_trading_main_top.*
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

    private var tabTitle: Array<String> = arrayOf("资金", "资讯", "公告", "F10")

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

    companion object {
        fun newInstance(): MarketDetailFragment {
            return MarketDetailFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initView()
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_back -> {
                pop()
            }
            iv_search -> {
                start(SearchInfoFragment.newInstance())
            }
        }

    }

    private fun initView() {
        top_bar.setPadding(0, StatusBarUtil.getStatusBarHeight(context), 0, 0)
        iv_back.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        magic_indicator.navigator = getNavigator()
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
                return tabTitle?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.parseColor("#C0CCE0")
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = tabTitle!![index]
                colorTransitionPagerTitleView.setOnClickListener {
                    magic_indicator.onPageSelected(index)
                    magic_indicator.onPageScrolled(index, 0.0F, 0)
//                    onSelect(index)
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