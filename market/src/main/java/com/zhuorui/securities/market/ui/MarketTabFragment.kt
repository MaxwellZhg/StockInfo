package com.zhuorui.securities.market.ui

import android.content.Context
import android.graphics.Color
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
import kotlinx.android.synthetic.main.fragment_market_detail.*
import kotlinx.android.synthetic.main.fragment_search_info.*
import kotlinx.android.synthetic.main.fragment_search_info.magic_indicator
import kotlinx.android.synthetic.main.fragment_search_info.viewpager
import kotlinx.android.synthetic.main.fragment_stock_tab.*
import kotlinx.android.synthetic.main.fragment_stock_tab.top_bar
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
    private val mFragments: Array<AbsFragment<*,*,*,*>?> = arrayOfNulls<AbsFragment<*,*,*,*>>(4)
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
        magic_indicator.navigator = getNavigator()
    }

    private fun onSelect(index: Int) {
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val firstFragment = findChildFragment(HkStockDetailFragment::class.java)
        if (firstFragment == null) {
            mFragments[0] = HkStockDetailFragment.newInstance(1)
            mFragments[1] = HkStockDetailFragment.newInstance(2)
            mFragments[2] = ChinaHkStockFragment.newInstance()
            mFragments[3] = HkStockDetailFragment.newInstance(1)
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
            mFragments[1] = findChildFragment(HkStockDetailFragment::class.java)
            mFragments[2] = findChildFragment(ChinaHkStockFragment::class.java)
            mFragments[3] = findChildFragment(HkStockDetailFragment::class.java)
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
                return mfragment?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.parseColor("#C0CCE0")
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = mfragment!![index]
                colorTransitionPagerTitleView.setOnClickListener {
                    magic_indicator.onPageSelected(index)
                    magic_indicator.onPageScrolled(index, 0.0F, 0)
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


}