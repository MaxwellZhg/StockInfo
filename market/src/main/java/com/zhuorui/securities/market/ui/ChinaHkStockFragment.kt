package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentChinaHkStockTabBinding
import com.zhuorui.securities.market.ui.adapter.ChinaHkStockAdapter
import com.zhuorui.securities.market.ui.presenter.ChinaHkStockTabPresenter
import com.zhuorui.securities.market.ui.view.ChinaHkStockTabView
import com.zhuorui.securities.market.ui.viewmodel.ChinaHkStockTabViewModel
import kotlinx.android.synthetic.main.fragment_china_hk_stock_tab.*
import kotlinx.android.synthetic.main.fragment_hk_stock_detail.*
import kotlinx.android.synthetic.main.item_stock_detail_header.*
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
 * Date: 2019/10/28
 * Desc:
 */
class ChinaHkStockFragment :AbsSwipeBackNetFragment<FragmentChinaHkStockTabBinding,ChinaHkStockTabViewModel,ChinaHkStockTabView,ChinaHkStockTabPresenter>()
    ,ChinaHkStockTabView,ChinaHkStockAdapter.OnItemClickMoreListener,ChinaHkStockAdapter.OnChinaHkStockClickListener{
    private var tabTitle:ArrayList<String> = ArrayList()
    private var infoadapter: ChinaHkStockAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_china_hk_stock_tab
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ChinaHkStockTabPresenter
        get() = ChinaHkStockTabPresenter()
    override val createViewModel: ChinaHkStockTabViewModel?
        get() =ViewModelProviders.of(this).get(ChinaHkStockTabViewModel::class.java)
    override val getView: ChinaHkStockTabView
        get() = this
    companion object {
        fun newInstance(): ChinaHkStockFragment {
            return ChinaHkStockFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tv_point_one.text="沪港通"
        tv_point_two.text="沪港通100"
        tv_point_three.text="沪港通300"
        zr_line.setType(1)
        zr_line.setValues(40,10,50)
        zr_line_text.setType(0)
        zr_line_text.setValues(40,10,50)
        zr_line1.setType(1)
        zr_line1.setValues(50,40,30)
        zr_line_text1.setType(0)
        zr_line_text1.setValues(50,40,30)
        zr_line2.setType(1)
        zr_line2.setValues(20,40,50)
        zr_line_text2.setType(0)
        zr_line_text2.setValues(20,40,50)
        trend_one.setType(1)
        trend_two.setType(2)
        tabTitle.add("沪股通")
        tabTitle.add("深股通")
        tabTitle.add("港股通(沪)")
        tabTitle.add("港股通(深)")
        magic_indicator.navigator=getNavigator()
        presenter?.setLifecycleOwner(this)
        infoadapter = presenter?.getChinaHkStockAdapter()
        infoadapter?.onItemClickMoreListener=this
        infoadapter?.onChinaHkStockClickListener=this
        //解决数据加载不完的问题
        rv_hk_stock.isNestedScrollingEnabled = false
        rv_hk_stock.setHasFixedSize(true)
        rv_hk_stock.isFocusable = false
        presenter?.setRvData()
        infoadapter?.notifyDataSetChanged()
        rv_hk_stock.adapter=infoadapter
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
                colorTransitionPagerTitleView.normalColor =
                    ResUtil.getColor(R.color.color_B3BCD0)!!
                colorTransitionPagerTitleView.selectedColor =
                    ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 16f
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

    private fun onSelect(index: Int) {

    }

    override fun addIntoRvData(list: List<Int>) {
        infoadapter?.clearItems()
        if (infoadapter?.items == null) {
            infoadapter?.items = ArrayList()
        }
        infoadapter?.addItems(list)
    }

    override fun onclickMore() {
        var pre  = parentFragment as AbsFragment<*, *, *, *>
        var parent  = pre.getParentFragment() as AbsFragment<*, *, *, *>
         parent.start(ChinaHkStockDetailFragment.newInstance())
    }
    override fun onChianHkStock() {
        ToastUtil.instance.toastCenter("港股通")
    }

}