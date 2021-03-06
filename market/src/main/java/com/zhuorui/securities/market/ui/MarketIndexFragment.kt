package com.zhuorui.securities.market.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.PaddingCommonNavigatorAdapter
import com.zhuorui.securities.market.customer.view.StockIndexSimpleView
import com.zhuorui.securities.market.databinding.FragmentMarketIndexBinding
import com.zhuorui.securities.market.ui.presenter.MarketIndexPresenter
import com.zhuorui.securities.market.ui.view.MarketIndexView
import com.zhuorui.securities.market.ui.viewmodel.MarketIndexViewModel
import kotlinx.android.synthetic.main.fragment_market_index.*
import kotlinx.android.synthetic.main.layout_market_detail_index_detailed.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-11-04 17:24
 *    desc   : 指数Fragment
 */
class MarketIndexFragment :
    AbsFragment<FragmentMarketIndexBinding, MarketIndexViewModel, MarketIndexView, MarketIndexPresenter>(),
    MarketIndexView {

    private var index = 0

    companion object {

        fun newInstance(): MarketIndexFragment {
            return MarketIndexFragment()
        }
    }

    val handler = Handler()

    val runnable = Runnable {
        nextText()
    }

    override val layout: Int
        get() = R.layout.fragment_market_index
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketIndexPresenter
        get() = MarketIndexPresenter()
    override val createViewModel: MarketIndexViewModel?
        get() = ViewModelProviders.of(this).get(MarketIndexViewModel::class.java)
    override val getView: MarketIndexView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initView()
        presenter?.getData()
    }

    private fun initView() {
        change_btn.setOnClickListener {
            if (simple_rootview.visibility == View.VISIBLE) {
                setDetailedView()
            } else {
                setSimpleView()
            }
        }
    }

    override fun onUpdata() {
        if (root_view.visibility != View.VISIBLE) {
            root_view.visibility = View.VISIBLE
            setSimpleView()
        }
    }

    /**
     * 切换成简要样式
     */
    private fun setSimpleView() {
        simple_rootview.visibility = View.VISIBLE
        detailed_rootview.visibility = View.GONE
        if (simple_rootview.childCount == 0) {
            val config = LocalSettingsConfig.getInstance()
            val upcolor = config.getUpColor()
            val downcolor = config.getDownColor()
            simple_rootview.setFactory {
                val v = StockIndexSimpleView(context)
                v.setColor(upcolor, downcolor)
                v
            }
            val inAnimation = TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                0f,
                Animation.RELATIVE_TO_PARENT,
                0f,
                Animation.RELATIVE_TO_PARENT,
                1f,
                Animation.RELATIVE_TO_PARENT,
                0f
            )
            inAnimation.duration = 300
            simple_rootview.inAnimation = inAnimation
            val outAnimation = TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,
                0f,
                Animation.RELATIVE_TO_PARENT,
                0f,
                Animation.RELATIVE_TO_PARENT,
                0f,
                Animation.RELATIVE_TO_PARENT,
                -1f
            )
            outAnimation.duration = 300
            simple_rootview.outAnimation = outAnimation
        }
        change_btn.setImageResource(R.mipmap.ic_arrow_up_c3cde3)
        nextText()
    }

    /**
     * 切换成详细样式
     */
    private fun setDetailedView() {
        handler.removeCallbacks(runnable)
        change_btn.setImageResource(R.mipmap.ic_arrow_down_c3cde3)
        detailed_rootview.visibility = View.VISIBLE
        simple_rootview.visibility = View.INVISIBLE
        if (viewpager.adapter == null) {
            val codes = presenter!!.getCodes()!!
            val tss = presenter!!.getTss()!!
            viewpager.offscreenPageLimit = codes.size
            viewpager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    val ts = tss[position]
                    val code = codes[position]
                    val tsCode = "$code.$ts"
                    return StockDetailIndexFragment.newInstance(code, ts, tsCode, 1)
                }

                override fun getCount(): Int {
                    return codes.size
                }
            }
            magic_indicator.navigator = getNavigator(viewpager, presenter!!.getTitles()!!)
            ViewPagerHelper.bind(magic_indicator, viewpager)
        }
        viewpager.currentItem = index
    }

    /**
     * 切换下一指数信息
     */
    private fun nextText() {
        if (presenter != null && presenter?.getCodes() != null) {
            val pos = if (index < presenter!!.getCodes()!!.size - 1) index + 1 else 0
            val v = simple_rootview.nextView
            if (v != null) {
                val simple = v as StockIndexSimpleView
                simple.setData(presenter?.getTitles()!![pos], 123.9f, 100f)
                simple_rootview.showNext()
            }
            index = pos
        }
        handler.postDelayed(runnable, 4000)
    }


    /**
     * 获取指示器适配器
     */
    private fun getNavigator(viewPager: ViewPager, titles: Array<String>): IPagerNavigator {
        val totalWidth = resources.displayMetrics.widthPixels - (resources.displayMetrics.density * (38f + 14f))
        val commonNavigator = CommonNavigator(context)
        commonNavigator.isAdjustMode = true
        val adapter = PaddingCommonNavigatorAdapter(titles)
        adapter.bindViewPager(viewPager)
        adapter.setTextSizeDp(14f)
        adapter.setTotalWidthPx(totalWidth)
        adapter.setNormalColor(Color.parseColor("#C3CDE3"))
        commonNavigator.adapter = adapter
        return commonNavigator
    }

    /**
     * 当左右设置padding不相等时，计算Indicator的位置信息
     */
    inner class PaddinTitleView(context: Context) : ColorTransitionPagerTitleView(context) {

        override fun getContentLeft(): Int {
            val bound = Rect()
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            val contentWidth = bound.width()
            return paddingLeft + left + (width - paddingLeft - paddingRight) / 2 - contentWidth / 2
        }

        override fun getContentRight(): Int {
            val bound = Rect()
            paint.getTextBounds(text.toString(), 0, text.length, bound)
            val contentWidth = bound.width()
            return contentLeft + contentWidth
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    override fun start(toFragment: ISupportFragment?) {
        (parentFragment as SupportFragment).start(toFragment)
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (simple_rootview.visibility == View.VISIBLE) {
            nextText()
        }
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        handler.removeCallbacks(runnable)
    }


}