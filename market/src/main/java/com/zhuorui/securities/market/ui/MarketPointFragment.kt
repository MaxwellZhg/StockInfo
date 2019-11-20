package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentMarketPointBinding
import com.zhuorui.securities.market.ui.adapter.MarketPointConsInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.presenter.MarketPointPresenter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel
import kotlinx.android.synthetic.main.fragment_market_point.*
import kotlinx.android.synthetic.main.layout_market_point_topbar.*
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/22
 * Desc:指数模块
 * */
class MarketPointFragment :
    AbsSwipeBackNetFragment<FragmentMarketPointBinding, MarketPointViewModel, MarketPointView, MarketPointPresenter>(),
    MarketPointView, View.OnClickListener {
    private var type: Int? = null
    private var infoadapter: MarketPointConsInfoAdapter? = null
    private var pointInfoAdapter: MarketPointInfoAdapter? = null
    private var tabTitle: ArrayList<String> = ArrayList()
    private val mFragments = arrayOfNulls<SupportFragment>(2)
    private var mIndex = 0
    override val layout: Int
        get() = R.layout.fragment_market_point
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: MarketPointPresenter
        get() = MarketPointPresenter()
    override val createViewModel: MarketPointViewModel?
        get() = ViewModelProviders.of(this).get(MarketPointViewModel::class.java)
    override val getView: MarketPointView
        get() = this

    companion object {
        fun newInstance(type: Int): MarketPointFragment {
            val fragment = MarketPointFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getSerializable("type") as Int?
        when (type) {
            1 -> {
                tv_title.text = "恒生指数（800000）"
            }
            2 -> {
                tv_title.text = "国企指数（800100）"
            }
            3 -> {
                tv_title.text = "红筹指数（HSCCI.HK）"
            }
        }
        tabTitle.add("成分股")
        tabTitle.add("资讯")
        iv_back.setOnClickListener(this)
        iv_search.setOnClickListener(this)
       // loadRootFragment(R.id.kline_view, MarketPointKlineFragment.newInstance("","","",false))
        scroll_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (magic_indicator != null) {
                top_magic_indicator?.visibility = if (scrollY < magic_indicator.top) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_back -> {
                pop()
            }
            iv_search -> {
                start(SearchInfoFragment.newInstance())
            }
        }

    }

    /**
     * 获取指示器适配器
     */
    private fun getNavigator(): IPagerNavigator? {
        // 设置viewpager指示器
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.isAdjustMode = false
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return tabTitle?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor =
                    ResUtil.getColor(R.color.color_B3BCD0)!!
                colorTransitionPagerTitleView.selectedColor =
                    ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = tabTitle!![index]
                colorTransitionPagerTitleView.setOnClickListener {
                    magic_indicator.onPageSelected(index)
                    magic_indicator.onPageScrolled(index, 0.0F, 0)
                    top_magic_indicator.onPageSelected(index)
                    top_magic_indicator.onPageScrolled(index, 0.0F, 0)
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

    private fun onSelect(index: Int) {
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }



    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        loadFragment()
    }

    /**
     * 加载界面中的fragment
     */
    private fun loadFragment() {
        //加载K线Fragment
   /*     loadRootFragment(
            R.id.kline_view,
            KlineFragment.newInstance(
                mStock.ts ?: "",
                mStock.code ?: "",
                mStock.tsCode ?: mStock.code + "." + mStock.ts,
                mStock.type ?: 2,
                false
            )
        )
        //加载指数Fragment
        if (!mBMP) {
            loadIndexFragment()
        }*/
        initTabFragment()
        magic_indicator.navigator = getNavigator()
        top_magic_indicator.navigator = getNavigator()
    }

    private fun initTabFragment() {
        val firstFragment = findChildFragment(MarketStockConsFragment::class.java)
        if (firstFragment == null) {
            mFragments[0] = MarketStockConsFragment.newInstance()
            mFragments[1] = MarketPointConsInfoFragment.newInstance()

            loadMultipleRootFragment(
                R.id.fl_tab_container, mIndex,
                mFragments[0],
                mFragments[1]
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用、
            mFragments[0] = firstFragment
            mFragments[1] = findChildFragment(MarketPointConsInfoFragment::class.java)
        }
    }

}