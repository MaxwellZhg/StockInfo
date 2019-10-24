package com.zhuorui.securities.market.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.NewStockDatePresenter
import com.zhuorui.securities.market.ui.view.NewStockDateView
import com.zhuorui.securities.market.ui.viewmodel.NewStockDateViewModel
import com.zhuorui.securities.market.databinding.FragmentNewStockDateBinding
import com.zhuorui.securities.market.event.SelectsSearchTabEvent
import com.zhuorui.securities.market.event.TabPositionEvent
import com.zhuorui.securities.market.model.StockPageInfo
import kotlinx.android.synthetic.main.fragment_search_info.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
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
 * Date: 2019/10/23
 * Desc:
 */
class NewStockDateFragment :
    AbsSwipeBackNetFragment<FragmentNewStockDateBinding, NewStockDateViewModel, NewStockDateView, NewStockDatePresenter>(),
    NewStockDateView {
    private var tabTitle: ArrayList<String> = ArrayList()
    private val mFragments: Array<NewStockInfoFragment?> = arrayOfNulls<NewStockInfoFragment>(4)
    private var mIndex = 0
    override val layout: Int
        get() = R.layout.fragment_new_stock_date
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: NewStockDatePresenter
        get() = NewStockDatePresenter()
    override val createViewModel: NewStockDateViewModel?
        get() = ViewModelProviders.of(this).get(NewStockDateViewModel::class.java)
    override val getView: NewStockDateView
        get() = this

    companion object {
        fun newInstance(): NewStockDateFragment {
            return NewStockDateFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tabTitle.add("已递表")
        tabTitle.add("可认购")
        tabTitle.add("待上市")
        tabTitle.add("已上市")
        magic_indicator.navigator=getNavigator()
    }
    private fun onSelect(index: Int) {
        showHideFragment(mFragments[index], mFragments[mIndex])
        mIndex = index
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val firstFragment = findChildFragment(NewStockInfoFragment::class.java)
        if (firstFragment == null) {
            mFragments[0] = NewStockInfoFragment.newInstance()
            mFragments[1] = NewStockInfoFragment.newInstance()
            mFragments[2] = NewStockInfoFragment.newInstance()
            mFragments[3] = NewStockInfoFragment.newInstance()
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
            mFragments[1] = findChildFragment(NewStockInfoFragment::class.java)
            mFragments[2] = findChildFragment(NewStockInfoFragment::class.java)
            mFragments[3] = findChildFragment(NewStockInfoFragment::class.java)
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
                return tabTitle?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.parseColor("#C0CCE0")
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = tabTitle!![index]
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

