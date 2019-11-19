package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.PaddingCommonNavigatorAdapter
import com.zhuorui.securities.market.customer.RehabilitationPopupWindow
import com.zhuorui.securities.market.customer.view.kline.stat.TradeDetailView
import com.zhuorui.securities.market.customer.view.kline.stat.TradeStatView
import kotlinx.android.synthetic.main.fragment_kline.*
import kotlinx.android.synthetic.main.layout_kline_stat.*
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * 股票K线图
 */
open class KlineFragment : SupportFragment(), OnClickListener, OnKlineHighlightListener,
    PaddingCommonNavigatorAdapter.OnCommonNavigatorSelectListener {

    private var mStatTabTitle: Array<String>? = null
    protected var ts: String? = null
    protected var code: String? = null
    protected var tsCode: String? = null
    protected var type: Int? = null
    private var landscape: Boolean = false
    protected var mFragments: Array<AbsFragment<*, *, *, *>?>? = null
    protected var mKlineIndex = 0
    protected var mLastKlineTitle: TextView? = null
    private var mRehabilitationMode = 0
    private var mStatIndex = 0
    private var mHightligh: IKLineHighlightView? = null


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
        return View.inflate(context, R.layout.fragment_kline, null)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        ts = arguments?.getString("ts")
        code = arguments?.getString("code")
        tsCode = arguments?.getString("tsCode")
        type = arguments?.getInt("type")
        landscape = arguments?.getBoolean("landscape")!!

        mStatTabTitle = getStatTabTitle()
        initKline()
        initStat()
    }

    open fun getStatTabTitle(): Array<String>? {
        return ResUtil.getStringArray(R.array.stock_stat_tab_title)
    }

    open fun initKline() {
        mFragments = arrayOfNulls(7)

        for (index in 0..kline_indicator.childCount - 3) {
            val chlidView = kline_indicator.getChildAt(index)
            chlidView.tag = index
            chlidView.setOnClickListener(indicatorClickListener)
        }
        title_minute.setOnClickListener(this)
        title_rehabilitation.setOnClickListener(this)
        mLastKlineTitle = title_day
        updateIndicatorPosition()

        for (i in mFragments!!.indices) {
            mFragments!![i] = getFragment(i)
        }
        (mFragments!![0] as IKLine).setHighlightListener(this)
        loadMultipleRootFragment(
            R.id.kline_container, mKlineIndex,
            mFragments!![0],
            mFragments!![1],
            mFragments!![2],
            mFragments!![3],
            mFragments!![4],
            mFragments!![5],
            mFragments!![6]
        )
    }

    protected val indicatorClickListener = OnClickListener { v ->
        indicator?.visibility = View.VISIBLE
        mLastKlineTitle?.setTextColor(ResUtil.getColor(R.color.color_FFC0CCE0)!!)

        if (minute_triangle != null) {
            minute_triangle.setImageResource(R.mipmap.ic_pull_triangle_normal)
        }
        onSelectKline(v.tag.toString().toInt())

        if (v is TextView) {
            v.setTextColor(ResUtil.getColor(R.color.color_53A0FD)!!)
            mLastKlineTitle = v
            mKlineIndex = v.tag.toString().toInt()
        }

        updateIndicatorPosition()
    }

    protected fun updateIndicatorPosition() {
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
                toggleRehabilitation(kline_indicator, title_rehabilitation)
            }
        }
    }

    protected fun toggleRehabilitation(v: View, textView: TextView) {
        context?.let {
            RehabilitationPopupWindow.create(
                it,
                mRehabilitationMode,
                object : RehabilitationPopupWindow.CallBack {
                    override fun onSelected(mode: Int) {
                        mRehabilitationMode = mode
                        when (mode) {
                            0 -> {
                                textView.text = getString(R.string.no_rehabilitation)
                            }
                            1 -> {
                                textView.text = getString(R.string.before_rehabilitation)
                            }
                            2 -> {
                                textView.text = getString(R.string.after_rehabilitation)
                            }
                        }
                    }
                }).showAsDropDown(
                v,
                kline_indicator.width - ResUtil.getDimensionDp2Px(50f),
                0,
                Gravity.TOP
            )
        }
    }

    private fun onSelectKline(index: Int) {
        val show: IKLine = mFragments?.get(index) as IKLine
        val hide: IKLine = mFragments?.get(mKlineIndex) as IKLine
        show.setHighlightListener(this)
        hide.setHighlightListener(null)
        showHideFragment(mFragments!![index], mFragments!![mKlineIndex])
        mKlineIndex = index
    }

    override fun onSelected(index: Int) {
        onSelectStat(index)
    }

    protected open fun getFragment(index: Int): AbsFragment<*, *, *, *>? {
        when (index) {
            0 -> {
                // 分时
                return ChartOneDayFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 1)
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
        val adapter = PaddingCommonNavigatorAdapter(mStatTabTitle!!)
        adapter.setTextSizeDp(13f)
        if (indicatorAlignEdge()) {
            adapter.setTotalWidthPx(stat_indicator.width.toFloat())
        }
        adapter.bindListener(this)
        commonNavigator.adapter = adapter

        stat_indicator.navigator = commonNavigator
        onSelectStat(mStatIndex)
    }

    open fun indicatorAlignEdge(): Boolean {
        return false
    }

    private fun onSelectStat(index: Int) {
        stat_indicator.onPageSelected(index)
        stat_indicator.onPageScrolled(index, 0.0F, 0)

        if (stat_container.childCount > 0) {
            for (i in 0 until stat_container.childCount) {
                stat_container.getChildAt(i).visibility = View.GONE
            }

            if (index <= stat_container.childCount - 1) {
                stat_container.getChildAt(index).visibility = View.VISIBLE
                return
            }
        }
        if (ts != null && code != null && type != null) {
            stat_container.addView(getTradeView(ts!!, code!!, type!!, index))
        }
    }

    open fun getTradeView(ts: String, code: String, type: Int, index: Int): View? {
        return when (index) {
            0 -> {
                context?.let { TradeDetailView(it, ts, code, type) }
            }
            1 -> {
                context?.let { TradeStatView(it, ts, code, type) }
            }
            else -> {
                null
            }
        }
    }

    override fun onShowHighlightView(v: IKLineHighlightView) {
        mHightligh = v
        highlight_info.removeAllViews()
        highlight_info.addView(
            v.getView(),
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        )
    }

    override fun onHideHighlightView() {
        highlight_info.removeAllViews()
    }

    override fun onUpHighlightData(obj: Any) {
        mHightligh?.setData(obj)
    }
}