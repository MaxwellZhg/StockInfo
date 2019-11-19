package com.zhuorui.securities.market.ui.kline

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.view.BuyingSellingFilesView
import com.zhuorui.securities.market.customer.view.kline.stat.TradeDetailView
import com.zhuorui.securities.market.customer.view.kline.stat.TradeStatView
import kotlinx.android.synthetic.main.fragment_kline_land.*
import me.jessyan.autosize.internal.CustomAdapt

/**
 * 横屏股票K线图
 */
class KlineLandFragment : KlineFragment(), CustomAdapt,
    AbsActivity.OnOrientationChangedListener {

    companion object {

        fun newInstance(ts: String, code: String, tsCode: String, type: Int): KlineLandFragment {
            val fragment = KlineLandFragment()
            val bundle = Bundle()
            bundle.putString("ts", ts)
            bundle.putString("code", code)
            bundle.putString("tsCode", tsCode)
            bundle.putInt("type", type)
            bundle.putBoolean("landscape", true)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_kline_land, null)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        // 全屏
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        (activity as AbsActivity).addOrientationChangedListener(this)

        iv_serach.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }

    override fun getStatTabTitle(): Array<String>? {
        return ResUtil.getStringArray(R.array.stock_stat_tab_land_title)
    }

    override fun initKline() {
        mFragments = arrayOfNulls(10)

        for (index in 0..kline_indicator.childCount - 2) {
            val chlidView = kline_indicator.getChildAt(index)
            chlidView.tag = index
            chlidView.setOnClickListener(indicatorClickListener)
        }
        rl_rehabilitation.setOnClickListener(this)
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
            mFragments!![6],
            mFragments!![7],
            mFragments!![8],
            mFragments!![9]
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            rl_rehabilitation -> {
                toggleRehabilitation(kline_indicator, title_reha)
            }
            iv_back -> {
                // 添加setRequestedOrientation方法实现屏幕不允许旋转
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                // 当由横屏切换到竖屏时会触发onChange(landscape: Boolean)回调来关闭界面
            }
            iv_serach -> {
                ToastUtil.instance.toast("搜索")
            }
            else -> {
            }
        }
    }

    override fun getFragment(index: Int): AbsFragment<*, *, *, *>? {
        when (index) {
            0 -> {
                // 分时
                return ChartOneDayLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 1)
            }
            1 -> {
                // 五日
                return ChartFiveDayLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!)
            }
            2 -> {
                // 日K
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 1)
            }
            3 -> {
                // 周K
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 7)
            }
            4 -> {
                // 月K
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 30)
            }
            5 -> {
                // 年K
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 365)
            }
            6 -> {
                // 5分
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 0)
            }
            7 -> {
                // 15分
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 0)
            }
            8 -> {
                // 30分
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 0)
            }
            9 -> {
                // 60分
                return ChartKLineLandFragment.newInstance(ts!!, code!!, tsCode!!, type!!, 0)
            }
        }
        return null
    }

    override fun indicatorAlignEdge(): Boolean {
        return true
    }

    override fun getTradeView(ts: String, code: String, type: Int, index: Int): View? {
        return when (index) {
            0 -> {
                context?.let {
                    val view = BuyingSellingFilesView(it)
                    view.layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    view
                }
            }
            1 -> {
                context?.let { TradeDetailView(it, ts, code, type) }
            }
            2 -> {
                context?.let { TradeStatView(it, ts, code, type) }
            }
            else -> {
                null
            }
        }
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 375f
    }

    override fun onChange(landscape: Boolean) {
        if (!landscape) {
            pop()
        }
    }

    override fun onBackPressedSupport(): Boolean {
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AbsActivity).removeOrientationChangedListener(this)

        // 取消全屏
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}