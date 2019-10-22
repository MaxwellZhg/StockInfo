package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsBackFinishFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketTabBinding
import com.zhuorui.securities.market.event.SelectsSearchTabEvent
import com.zhuorui.securities.market.event.TabPositionEvent
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.model.StockPageInfo
import com.zhuorui.securities.market.ui.presenter.MarketTabPresenter
import com.zhuorui.securities.market.ui.view.MarketTabVierw
import com.zhuorui.securities.market.ui.viewmodel.MarketTabVierwModel
import com.zhuorui.securities.personal.ui.MessageFragment
import kotlinx.android.synthetic.main.fragment_search_info.*
import kotlinx.android.synthetic.main.fragment_search_info.magic_indicator
import kotlinx.android.synthetic.main.fragment_search_info.viewpager
import kotlinx.android.synthetic.main.fragment_stock_tab.*
import kotlinx.android.synthetic.main.layout_simulation_trading_main_top.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/27 13:40
 *    desc   : 主页行情Tab页面
 */
class MarketTabFragment :
    AbsBackFinishFragment<FragmentMarketTabBinding, MarketTabVierwModel, MarketTabVierw, MarketTabPresenter>(),
    MarketTabVierw {
    private var mfragment:ArrayList<String?> = ArrayList()
    private val mFragments:ArrayList<HkStockDetailFragment> = ArrayList()
    private var mIndex = 0
    companion object {
        fun newInstance(): MarketTabFragment {
            return MarketTabFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_market_tab

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MarketTabPresenter
        get() = MarketTabPresenter()

    override val createViewModel: MarketTabVierwModel?
        get() = ViewModelProviders.of(this).get(MarketTabVierwModel::class.java)

    override val getView: MarketTabVierw
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        top_bar.setRightClickListener {
            // 消息
            (parentFragment as AbsFragment<*, *, *, *>).start(MessageFragment.newInstance())
        }
        top_bar.setRight2ClickListener {
            // 搜索
            (parentFragment as AbsFragment<*, *, *, *>).start(SearchInfoFragment.newInstance())
        }
        mfragment.add(ResUtil.getString(R.string.hk_stock))
        mfragment.add(ResUtil.getString(R.string.sh_sz_stock))
        mfragment.add(ResUtil.getString(R.string.all_hk_stock))
        mfragment.add(ResUtil.getString(R.string.gloab_stock))
        mFragments.add(HkStockDetailFragment.newInstance())
        mFragments.add(HkStockDetailFragment.newInstance())
        mFragments.add(HkStockDetailFragment.newInstance())
        mFragments.add(HkStockDetailFragment.newInstance())
        initViewPager()
    }

    private fun initViewPager() {
        if (viewpager.adapter == null) {
            // 设置viewpager指示器
            val commonNavigator = CommonNavigator(requireContext())
            commonNavigator.isAdjustMode = true
            commonNavigator.adapter = object : CommonNavigatorAdapter() {

                override fun getCount(): Int {
                    return mfragment!!.size
                }

                override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                    val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                    colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.un_tab_select)!!
                    colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                    colorTransitionPagerTitleView.text = mfragment!![index]
                    colorTransitionPagerTitleView.textSize = 18f
                    colorTransitionPagerTitleView.setOnClickListener {
                        viewpager.currentItem = index
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

            // 设置viewpager页面缓存数量
            viewpager.offscreenPageLimit =mFragments.size
            // 设置viewpager适配器
            viewpager.adapter = childFragmentManager?.let { ViewPagerAdapter(it) }
            viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    magic_indicator.onPageScrollStateChanged(state)
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    magic_indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    magic_indicator.onPageSelected(position)
                }
            })

            // 指示器绑定viewpager
            magic_indicator.navigator = commonNavigator
            ViewPagerHelper.bind(magic_indicator, viewpager)
        }
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): HkStockDetailFragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mfragment?.size!!
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mfragment[position]
        }
    }


}