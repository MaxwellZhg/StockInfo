package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.adapter.StockInfoAdapter
import com.zhuorui.securities.market.ui.presenter.HkStockDetailPresenter
import com.zhuorui.securities.market.ui.view.HkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.HkStockDetailViewModel
import kotlinx.android.synthetic.main.fragment_hk_stock_detail.*
import kotlinx.android.synthetic.main.fragment_search_info.*
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.*
import kotlinx.android.synthetic.main.item_stock_detail_header.*
import kotlinx.android.synthetic.main.layout_new_stock_date.*
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
 * Date: 2019/10/17
 * Desc:
 */
class HkStockDetailFragment :
    AbsFragment<com.zhuorui.securities.market.databinding.FragmentHkStockDetailBinding, HkStockDetailViewModel, HkStockDetailView, HkStockDetailPresenter>(),
    HkStockDetailView,View.OnClickListener {
    private var infoadapter: MarketPartInfoAdapter? = null
    private var tabTitle:ArrayList<String> = ArrayList()
    override val layout: Int
        get() = R.layout.fragment_hk_stock_detail
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: HkStockDetailPresenter
        get() = HkStockDetailPresenter()
    override val createViewModel: HkStockDetailViewModel?
        get() = ViewModelProviders.of(this).get(HkStockDetailViewModel::class.java)
    override val getView: HkStockDetailView
        get() = this

    companion object {
        fun newInstance(): HkStockDetailFragment {
            return HkStockDetailFragment()
        }
    }



    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tabTitle.add("涨幅榜")
        tabTitle.add("跌幅榜")
        tabTitle.add("成交额")
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
        magic_indicator1.navigator=getNavigator(1)
        magic_indicator2.navigator=getNavigator(2)
        magic_indicator3.navigator=getNavigator(3)
        presenter?.setLifecycleOwner(this)
        infoadapter = presenter?.getMarketInfoAdapter()
        //解决数据加载不完的问题
        rv_hk_stock1.isNestedScrollingEnabled = false
        rv_hk_stock1.setHasFixedSize(true)
        rv_hk_stock2.isNestedScrollingEnabled = false
        rv_hk_stock2.setHasFixedSize(true)
        rv_hk_stock3.isNestedScrollingEnabled = false
        rv_hk_stock3.setHasFixedSize(true)
        //解决数据加载完成后, 没有停留在顶部的问题
        rv_hk_stock1.isFocusable = false
        rv_hk_stock2.isFocusable = false
        rv_hk_stock3.isFocusable = false
        presenter?.getData()
        infoadapter?.notifyDataSetChanged()
        rv_hk_stock1.adapter=infoadapter
        rv_hk_stock2.adapter=infoadapter
        rv_hk_stock3.adapter=infoadapter
        ll_hs_point.setOnClickListener(this)
        rl_new_stock_date.setOnClickListener(this)
    }

    /**
     * 获取指示器适配器
     */
    private fun getNavigator(type :Int): IPagerNavigator? {
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
                    detailClickIndecator(index,type)
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

    private fun onSelect(index: Int, type: Int) {

    }

    fun detailClickIndecator(index :Int,type:Int){
        when(type){
            1->{
                magic_indicator1.onPageSelected(index)
                magic_indicator1.onPageScrolled(index, 0.0F, 0)
            }
            2->{
                magic_indicator2.onPageSelected(index)
                magic_indicator2.onPageScrolled(index, 0.0F, 0)
            }
            3->{
                magic_indicator3.onPageSelected(index)
                magic_indicator3.onPageScrolled(index, 0.0F, 0)
            }
        }
        onSelect(index,type)
    }
    override fun addInfoToAdapter(list: List<Int>) {
        infoadapter?.clearItems()
        if (infoadapter?.items == null) {
            infoadapter?.items = ArrayList()
        }
        infoadapter?.addItems(list)
    }

    override fun onClick(p0: View?) {
        var pre  = parentFragment as AbsFragment<*,*,*,*>
        var parent  = pre.getParentFragment() as AbsFragment<*,*,*,*>
       when(p0?.id){
           R.id.ll_hs_point->{
               parent.start(MarketPointFragment.newInstance())
           }
           R.id.rl_new_stock_date->{
               parent.start(NewStockDateFragment.newInstance())
           }
       }
    }


}