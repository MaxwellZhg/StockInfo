package com.zhuorui.securities.market.ui.kline

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.RehabilitationPopupWindow
import com.zhuorui.securities.market.customer.view.kline.stat.TradeDetailView
import com.zhuorui.securities.market.customer.view.kline.stat.TradeStatView
import kotlinx.android.synthetic.main.fragment_stockdetail.*
import kotlinx.android.synthetic.main.layout_kline_stat.*
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
class KlineFragment private constructor() : SupportFragment(), OnClickListener {

    //    private val tabTitle: Array<String> = arrayOf("分时", "五日", "日K", "周K", "月K", "年K", "分钟", "不复权")
    //    private val tabTitle: Array<String> = arrayOf("分时", "五日", "日K", "周K", "月K", "年K", "5分", "15分", "30分", "60分", "不复权")
    private val mStatTabTitle: Array<String?> =
        arrayOf(ResUtil.getString(R.string.kline_detail), ResUtil.getString(R.string.kline_stat))
    private var ts: String? = null
    private var code: String? = null
    private var tsCode: String? = null
    private var type: Int? = null
    private var landscape: Boolean = false
    private val mFragments = arrayOfNulls<AbsFragment<*, *, *, *>>(7)
    private var mKlineIndex = 0
    private var mLastKlineTitle: TextView? = null
    private var mRehabilitationMode = 0
    private var mStatIndex = 0


    companion object {

        fun newInstance(ts: String, code: String, tsCode: String, type: Int, land: Boolean): KlineFragment {
            val fragment = KlineFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putBoolean("landscape", land)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_stockdetail, null)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        ts = arguments?.getString("ts")
        code = arguments?.getString("code")
        tsCode = arguments?.getString("tsCode")
        type = arguments?.getInt("type")
        landscape = arguments?.getBoolean("landscape")!!

        initKline()

        initStat()
    }

    private fun initKline() {
        for (index in 0..kline_indicator.childCount - 3) {
            val chlidView = kline_indicator.getChildAt(index)
            chlidView.tag = index
            chlidView.setOnClickListener(indicatorClickListener)
        }
        title_minute.setOnClickListener(this)
        title_rehabilitation.setOnClickListener(this)
        mLastKlineTitle = title_day
        updateIndicatorPosition()

        for (i in 0..6) {
            mFragments[i] = getFragment(i)
        }

        loadMultipleRootFragment(
            R.id.kline_container, mKlineIndex,
            mFragments[0],
            mFragments[1],
            mFragments[2],
            mFragments[3],
            mFragments[4],
            mFragments[5],
            mFragments[6]
        )
    }

    private val indicatorClickListener = OnClickListener { v ->
        indicator?.visibility = View.VISIBLE
        mLastKlineTitle?.setTextColor(ResUtil.getColor(R.color.color_FFC0CCE0)!!)

        minute_triangle.setImageResource(R.mipmap.ic_pull_triangle_normal)

        onSelectKline(v.tag.toString().toInt())

        if (v is TextView) {
            v.setTextColor(ResUtil.getColor(R.color.color_53A0FD)!!)
            mLastKlineTitle = v
            mKlineIndex = v.tag.toString().toInt()
        }

        updateIndicatorPosition()
    }

    private fun updateIndicatorPosition() {
        val layoutParams = indicator.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = mLastKlineTitle?.width ?: 0
        layoutParams.marginStart = mLastKlineTitle?.left ?: 0
        indicator.layoutParams = layoutParams
    }

    override fun onClick(v: View?) {
        when (v) {
            title_minute -> {
                ToastUtil.instance.toast("分钟")
                minute_triangle.setImageResource(R.mipmap.ic_pull_triangle_selected)

                indicator.visibility = View.GONE
                mLastKlineTitle?.setTextColor(ResUtil.getColor(R.color.color_FFC0CCE0)!!)

                mLastKlineTitle = tv_minute
                tv_minute.setTextColor(ResUtil.getColor(R.color.color_53A0FD)!!)
            }
            title_rehabilitation -> {
                context?.let {
                    RehabilitationPopupWindow.create(
                        it,
                        mRehabilitationMode,
                        object : RehabilitationPopupWindow.CallBack {
                            override fun onSelected(mode: Int) {
                                mRehabilitationMode = mode
                                when (mode) {
                                    0 -> {
                                        title_rehabilitation.text = getString(R.string.no_rehabilitation)
                                    }
                                    1 -> {
                                        title_rehabilitation.text = getString(R.string.before_rehabilitation)
                                    }
                                    2 -> {
                                        title_rehabilitation.text = getString(R.string.after_rehabilitation)
                                    }
                                }
                            }
                        }).showAsDropDown(
                        kline_indicator,
                        kline_indicator.width - ResUtil.getDimensionDp2Px(50f),
                        0,
                        Gravity.TOP
                    )
                }
            }
        }
    }

    private fun onSelectKline(index: Int) {
        showHideFragment(mFragments[index], mFragments[mKlineIndex])
        mKlineIndex = index
    }

    private fun getFragment(index: Int): AbsFragment<*, *, *, *>? {
        when (index) {
            0 -> {
                // 分时
                return ChartOneDayFragment.newInstance(ts!!, code!!, tsCode!!, type!!, landscape)
            }
            1 -> {
                // 五日
                return ChartFiveDayFragment.newInstance(ts!!, code!!, tsCode!!, type!!, landscape)
            }
            2 -> {
                // 日K
                return ChartKLineFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 1, landscape)
            }
            3 -> {
                // 周K
                return ChartKLineFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 7, landscape)
            }
            4 -> {
                // 月K
                return ChartKLineFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 30, landscape)
            }
            5 -> {
                // 年K
                return ChartKLineFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 365, landscape)
            }
            6 -> {
                // 自定义分钟K
                return ChartKLineFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 0, landscape)
            }
        }
        return null
    }


    private fun initStat() {
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mStatTabTitle.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.color_FFFFFFFF)!!
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 13f
                colorTransitionPagerTitleView.text = mStatTabTitle[index]
                colorTransitionPagerTitleView.setOnClickListener {
                    stat_indicator.onPageSelected(index)
                    stat_indicator.onPageScrolled(index, 0.0F, 0)
                    onSelectStat(index)
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ResUtil.getColor(R.color.tab_select))
                indicator.lineHeight = ResUtil.getDimensionDp2Px(1f).toFloat()
                return indicator
            }
        }
        stat_indicator.navigator = commonNavigator
        onSelectStat(mStatIndex)
    }

    private fun onSelectStat(index: Int) {
        stat_container.removeAllViews()
        if (ts != null && code != null && type != null) {
            when (index) {
                0 -> {
                    stat_container.addView(context?.let { TradeDetailView(it, ts!!, code!!, type!!) })
                }
                1 -> {
                    stat_container.addView(context?.let { TradeStatView(it, ts!!, code!!, type!!) })
                }
            }
        }
    }
}