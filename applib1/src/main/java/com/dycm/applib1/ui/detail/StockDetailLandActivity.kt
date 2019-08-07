package com.dycm.applib1.ui.detail

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.dycm.applib1.R
import com.dycm.base2app.ui.activity.AbsActivity
import kotlinx.android.synthetic.main.fragment_stockdetail.*

/**
 * 股票详情页-横屏
 */
class StockDetailLandActivity : AbsActivity() {

    override val layout: Int
        get() = R.layout.fragment_stockdetail

    override val acContentRootViewId: Int
        get() = R.id.root_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val fragments = arrayOf<Fragment>(
            ChartOneDayFragment.newInstance(true),
            ChartFiveDayFragment.newInstance(true),
            ChartKLineFragment.newInstance(1, true),
            ChartKLineFragment.newInstance(7, true),
            ChartKLineFragment.newInstance(30, true),
            ChartKLineFragment.newInstance(90, true),
            ChartKLineFragment.newInstance(365, true)
        )
        val titles = arrayOf("分时", "五日", "日K", "周K", "月K", "季K", "年K")
        view_pager!!.offscreenPageLimit = fragments.size
        view_pager!!.adapter = SimpleFragmentPagerAdapter(supportFragmentManager, fragments, titles)
        tab!!.setupWithViewPager(view_pager)
    }
}