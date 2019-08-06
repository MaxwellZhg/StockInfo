package com.dycm.applib1.ui.detail

import androidx.fragment.app.Fragment
import com.dycm.applib1.R
import com.dycm.base2app.ui.fragment.AbsSwipeBackNetFragment
import kotlinx.android.synthetic.main.fragment_stockdetail.*

/**
 * 股票详情页-横屏
 */
class StockDetailLandFragment : AbsSwipeBackNetFragment() {

    override val layout: Int
        get() = R.layout.fragment_stockdetail

    override fun init() {
        val fragments = arrayOf<Fragment>(
            ChartOneDayFragment.newInstance(true),
            ChartFiveDayFragment.newInstance(true),
            ChartKLineFragment.newInstance(1, true),
            ChartKLineFragment.newInstance(7, true),
            ChartKLineFragment.newInstance(30, true)
        )
        val titles = arrayOf("分时", "五日", "日K", "周K", "月K")
        view_pager!!.offscreenPageLimit = fragments.size
        view_pager!!.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
        tab!!.setupWithViewPager(view_pager)
    }
}