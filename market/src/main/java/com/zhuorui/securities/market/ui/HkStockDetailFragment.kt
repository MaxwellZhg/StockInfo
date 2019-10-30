package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.presenter.HkStockDetailPresenter
import com.zhuorui.securities.market.ui.view.HkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.HkStockDetailViewModel
import kotlinx.android.synthetic.main.fragment_hk_stock_detail.*
import kotlinx.android.synthetic.main.fragment_market_detail.*
import kotlinx.android.synthetic.main.fragment_search_info.magic_indicator
import kotlinx.android.synthetic.main.fragment_simulation_trading_main.*
import kotlinx.android.synthetic.main.item_stock_detail_header.*
import kotlinx.android.synthetic.main.layout_hk_top_tips_filters.*
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
    HkStockDetailView, View.OnClickListener {
    private var type: Int? = null
    private var infoadapter: MarketPartInfoAdapter? = null
    private var tabTitle: ArrayList<String> = ArrayList()
    private var topType:Int = 0;
    private var allHkStockIndex: Int = 0
    private var allHkMainStockIndex: Int = 0
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
        fun newInstance(type: Int): HkStockDetailFragment {
            val fragment = HkStockDetailFragment()
            if (type != null) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getSerializable("type") as Int?
        when (type) {
            1 -> {
                tv_point_one.text = "恒生指数"
                tv_point_two.text = "国企指数"
                tv_point_three.text = "红筹指数"
            }
            2 -> {
                tv_point_one.text = "上证指数"
                tv_point_two.text = "深证成指"
                tv_point_three.text = "创业板指"
            }
        }
        tabTitle.add("涨幅榜")
        tabTitle.add("跌幅榜")
        tabTitle.add("成交额")
        zr_line.setType(1)
        zr_line.setValues(40, 10, 50)
        zr_line_text.setType(0)
        zr_line_text.setValues(40, 10, 50)
        zr_line1.setType(1)
        zr_line1.setValues(50, 40, 30)
        zr_line_text1.setType(0)
        zr_line_text1.setValues(50, 40, 30)
        zr_line2.setType(1)
        zr_line2.setValues(20, 40, 50)
        zr_line_text2.setType(0)
        zr_line_text2.setValues(20, 40, 50)
        magic_indicator1.navigator = getNavigator(1)
        magic_indicator2.navigator = getNavigator(2)
        magic_indicator3.navigator = getNavigator(3)
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
        rv_hk_stock1.adapter = infoadapter
        rv_hk_stock2.adapter = infoadapter
        rv_hk_stock3.adapter = infoadapter
        ll_hs_point.setOnClickListener(this)
        ll_country_point.setOnClickListener(this)
        ll_red_point.setOnClickListener(this)
        rl_new_stock_date.setOnClickListener(this)
        ll_part_one.setOnClickListener(this)
        ll_part_two.setOnClickListener(this)
        tv_all_hk_stock.setOnClickListener(this)
        tv_main_part.setOnClickListener(this)
        tv_create_newly_part.setOnClickListener(this)
        tv_top_tips.setOnClickListener(this)
        scroll_hk_detail_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (tv_all_hk_stock != null) {
                //ll_top_tips?.visibility = if (scrollY < tv_all_hk_stock.top) View.GONE else View.VISIBLE
                if (scrollY < tv_all_hk_stock.top) {
                    topType=0
                    ll_top_tips.visibility = View.GONE
                } else if (scrollY >= tv_all_hk_stock.top && scrollY < tv_main_part.top) {
                    if (topType==0) {
                        topType=1
                        ll_top_tips.visibility = View.VISIBLE
                        tv_top_tips.text = "全部港股"
                        magic_indicator_top_tips.navigator = getTopNavigator(1)
                        magic_indicator_top_tips.onPageSelected(allHkStockIndex)
                        magic_indicator_top_tips.onPageScrolled(allHkStockIndex, 0.0F, 0)
                    }
                } else {
                    if (topType==1) {
                        topType=2
                        tv_top_tips.text = "主板"
                        magic_indicator_top_tips.navigator = getTopNavigator(2)
                        magic_indicator_top_tips.onPageSelected(allHkMainStockIndex)
                        magic_indicator_top_tips.onPageScrolled(allHkMainStockIndex, 0.0F, 0)
                    }
                }
            }
        }
    }

    /**
     * 获取指示器适配器
     */
    private fun getNavigator(type: Int): IPagerNavigator? {
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
                    detailClickIndecator(index, type)
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

    fun detailClickIndecator(index: Int, type: Int) {
        when (type) {
            1 -> {
                magic_indicator1.onPageSelected(index)
                magic_indicator1.onPageScrolled(index, 0.0F, 0)
                allHkStockIndex = index
            }
            2 -> {
                magic_indicator2.onPageSelected(index)
                magic_indicator2.onPageScrolled(index, 0.0F, 0)
                allHkMainStockIndex = index
            }
            3 -> {
                magic_indicator3.onPageSelected(index)
                magic_indicator3.onPageScrolled(index, 0.0F, 0)
            }
        }
        onSelect(index, type)
    }

    override fun addInfoToAdapter(list: List<Int>) {
        infoadapter?.clearItems()
        if (infoadapter?.items == null) {
            infoadapter?.items = ArrayList()
        }
        infoadapter?.addItems(list)
    }

    override fun onClick(p0: View?) {
        var pre = parentFragment as AbsFragment<*, *, *, *>
        var parent = pre.getParentFragment() as AbsFragment<*, *, *, *>
        when (p0?.id) {
            R.id.ll_hs_point -> {
                parent.start(MarketPointFragment.newInstance(1))
            }
            R.id.ll_country_point -> {
                parent.start(MarketPointFragment.newInstance(2))
            }
            R.id.ll_red_point -> {
                parent.start(MarketPointFragment.newInstance(3))
            }
            R.id.rl_new_stock_date -> {
                parent.start(NewStockDateFragment.newInstance())
            }
            R.id.ll_part_one -> {
                parent.start(MarketPartInfoFragment.newInstance(1))
            }
            R.id.ll_part_two -> {
                parent.start(MarketPartInfoFragment.newInstance(2))
            }
            R.id.tv_all_hk_stock -> {
                parent.start(AllHkStockFragment.newInstance(1))
            }
            R.id.tv_main_part -> {
                parent.start(AllHkStockFragment.newInstance(2))
            }
            R.id.tv_create_newly_part -> {
                parent.start(AllHkStockFragment.newInstance(3))
            }
            R.id.tv_top_tips->{
                when(topType){
                    1->{
                        parent.start(AllHkStockFragment.newInstance(1))
                    }
                    2->{
                        parent.start(AllHkStockFragment.newInstance(2))
                    }
                }
            }
        }
    }

    /**
     * 获取指示器适配器
     */
    private fun getTopNavigator(type: Int): IPagerNavigator? {
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
                    detailClickTopIndecator(index, type)
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

    private fun detailClickTopIndecator(index: Int, type: Int) {
        when (type) {
            1 -> {
                magic_indicator_top_tips.onPageSelected(index)
                magic_indicator_top_tips.onPageScrolled(index, 0.0F, 0)
                magic_indicator1.onPageSelected(index)
                magic_indicator1.onPageScrolled(index, 0.0F, 0)
                allHkStockIndex = index
            }
            2 -> {
                magic_indicator_top_tips.onPageSelected(index)
                magic_indicator_top_tips.onPageScrolled(index, 0.0F, 0)
                magic_indicator2.onPageSelected(index)
                magic_indicator2.onPageScrolled(index, 0.0F, 0)
                allHkMainStockIndex = index
            }
        }
        onSelect(index, type)
    }


}