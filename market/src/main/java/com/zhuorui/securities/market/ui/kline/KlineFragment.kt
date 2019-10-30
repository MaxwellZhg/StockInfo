package com.zhuorui.securities.market.ui.kline

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.customer.RehabilitationPopupWindow
import kotlinx.android.synthetic.main.fragment_stockdetail.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * 股票K线图
 */
class KlineFragment : SupportFragment(), OnClickListener {

    //    private val tabTitle: Array<String> = arrayOf("分时", "五日", "日K", "周K", "月K", "年K", "分钟", "不复权")
    //    private val tabTitle: Array<String> = arrayOf("分时", "五日", "日K", "周K", "月K", "年K", "5分", "15分", "30分", "60分", "不复权")
    private val mFragments = arrayOfNulls<AbsFragment<*, *, *, *>>(7)
    private var mIndex = 0
    private var lastTitle: TextView? = null
    private var rehabilitationMode = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.fragment_stockdetail, null)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        for (index in 0..kline_indicator.childCount - 3) {
            val chlidView = kline_indicator.getChildAt(index)
            chlidView.tag = index
            chlidView.setOnClickListener(indicatorClickListener)
        }
        title_minute.setOnClickListener(this)
        title_rehabilitation.setOnClickListener(this)
        lastTitle = title_day
        updateIndicatorPosition()

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

    private val indicatorClickListener = OnClickListener { v ->
        indicator?.visibility = View.VISIBLE
        lastTitle?.setTextColor(ResUtil.getColor(R.color.color_FFC0CCE0)!!)

        minute_triangle.setImageResource(R.mipmap.ic_pull_triangle_normal)

        onSelect(v.tag.toString().toInt())

        if (v is TextView) {
            v.setTextColor(ResUtil.getColor(R.color.color_53A0FD)!!)
            lastTitle = v
            mIndex = v.tag.toString().toInt()
        }

        updateIndicatorPosition()
    }

    private fun updateIndicatorPosition() {
        val layoutParams = indicator.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = lastTitle?.width ?: 0
        layoutParams.marginStart = lastTitle?.left ?: 0
        indicator.layoutParams = layoutParams
    }

    override fun onClick(v: View?) {
        when (v) {
            title_minute -> {
                ToastUtil.instance.toast("分钟")
                minute_triangle.setImageResource(R.mipmap.ic_pull_triangle_selected)

                indicator.visibility = View.GONE
                lastTitle?.setTextColor(ResUtil.getColor(R.color.color_FFC0CCE0)!!)

                lastTitle = tv_minute
                tv_minute.setTextColor(ResUtil.getColor(R.color.color_53A0FD)!!)
            }
            title_rehabilitation -> {
                context?.let {
                    RehabilitationPopupWindow.create(
                        it,
                        rehabilitationMode,
                        object : RehabilitationPopupWindow.CallBack {
                            override fun onSelected(mode: Int) {
                                rehabilitationMode = mode
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

    private fun onSelect(index: Int) {
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }

    private fun getFragment(index: Int): AbsFragment<*, *, *, *>? {
        when (index) {
            0 -> {
                return ChartOneDayFragment.newInstance(true)
            }
            1 -> {
                return ChartFiveDayFragment.newInstance(true)
            }
            2 -> {
                return ChartKLineFragment.newInstance(1, true)
            }
            3 -> {
                return ChartKLineFragment.newInstance(7, true)
            }
            4 -> {
                return ChartKLineFragment.newInstance(30, true)
            }
            5 -> {
                return ChartKLineFragment.newInstance(90, true)
            }
            6 -> {
                return ChartKLineFragment.newInstance(365, true)
            }
        }
        return null
    }
}