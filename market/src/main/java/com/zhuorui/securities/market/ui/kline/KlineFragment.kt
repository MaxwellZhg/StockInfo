package com.zhuorui.securities.market.ui.kline

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.R
import kotlinx.android.synthetic.main.fragment_stockdetail.*
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * 股票K线图
 */
class KlineFragment : SupportFragment() {

    private val tabTitle: Array<String> = arrayOf("分时", "五日", "日K", "周K", "月K", "季K", "年K")
    private val mFragments = arrayOfNulls<AbsFragment<*, *, *, *>>(7)
    private var mIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_stockdetail, null)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

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
                colorTransitionPagerTitleView.textSize = 16f
                colorTransitionPagerTitleView.text = tabTitle[index]
                colorTransitionPagerTitleView.setOnClickListener {
                    kline_indicator.onPageSelected(index)
                    kline_indicator.onPageScrolled(index, 0.0F, 0)
                    onSelect(index)
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ResUtil.getColor(R.color.tab_select))
                indicator.lineHeight = ResUtil.getDimensionDp2Px(2f).toFloat()
                indicator.lineWidth = ResUtil.getDimensionDp2Px(31f).toFloat()
                return indicator
            }
        }
        kline_indicator.navigator = commonNavigator

        for (i in 0..6) {
            mFragments[i] = getFragment(i)
        }

        loadMultipleRootFragment(
            R.id.kline_container, mIndex,
            mFragments[0],
            mFragments[1],
            mFragments[2],
            mFragments[3],
            mFragments[4],
            mFragments[5],
            mFragments[6]
        )
    }

    private fun onSelect(index: Int) {
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }

    private fun getFragment(index: Int): AbsFragment<*, *, *, *>? {
        when (index) {
            0 -> {
                return ChartOneDayFragment.newInstance(false)
            }
            1 -> {
                return ChartFiveDayFragment.newInstance(false)
            }
            2 -> {
                return ChartKLineFragment.newInstance(1, false)
            }
            3 -> {
                return ChartKLineFragment.newInstance(7, false)
            }
            4 -> {
                return ChartKLineFragment.newInstance(30, false)
            }
            5 -> {
                return ChartKLineFragment.newInstance(90, false)
            }
            6 -> {
                return ChartKLineFragment.newInstance(365, false)
            }
        }
        return null
    }
}