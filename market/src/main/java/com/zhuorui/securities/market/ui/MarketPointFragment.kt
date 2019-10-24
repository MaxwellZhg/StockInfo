package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.ui.presenter.MarketPointPresenter
import com.zhuorui.securities.market.ui.view.MarketPointView
import com.zhuorui.securities.market.ui.viewmodel.MarketPointViewModel
import com.zhuorui.securities.market.databinding.FragmentMarketPointBinding
import com.zhuorui.securities.market.generated.callback.OnClickListener
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.adapter.MarketPointInfoAdapter
import com.zhuorui.securities.market.ui.kline.KlineFragment
import kotlinx.android.synthetic.main.fragment_hk_stock_detail.*
import kotlinx.android.synthetic.main.fragment_market_point.*
import kotlinx.android.synthetic.main.layout_market_point_topbar.*
import kotlinx.android.synthetic.main.layout_market_point_view_tips.*
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
 * Desc:
 */
class MarketPointFragment :AbsSwipeBackNetFragment<FragmentMarketPointBinding,MarketPointViewModel,MarketPointView,MarketPointPresenter>(),MarketPointView,View.OnClickListener{
    private var infoadapter: MarketPartInfoAdapter? = null
    private var pointInfoAdapter:MarketPointInfoAdapter?=null
    private var tabTitle:ArrayList<String> = ArrayList()
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
        fun newInstance(): MarketPointFragment {
            return MarketPointFragment()
        }
    }

    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tabTitle.add("成分股")
        tabTitle.add("咨讯")
        iv_back.setOnClickListener(this)
        iv_search.setOnClickListener(this)
        loadRootFragment(R.id.kline_view, KlineFragment())
        magic_indicator.navigator=getNavigator()
        presenter?.setLifecycleOwner(this)
        infoadapter = presenter?.getMarketInfoAdapter()
        pointInfoAdapter=presenter?.getMarketPointInfoAdapter()
        //解决数据加载不完的问题
        rv_point_stock.isNestedScrollingEnabled = false
        rv_point_stock.setHasFixedSize(true)
        rv_point_stock.isFocusable = false
        presenter?.getData()
        infoadapter?.notifyDataSetChanged()
        rv_point_stock.adapter=infoadapter
        refrsh_layout.setEnableLoadMore(true)
    }
    override fun onClick(v: View?) {
        when(v){
            iv_back->{
                pop()
            }
            iv_search->{
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
      when(index){
          0->{
              presenter?.getData()
              infoadapter?.notifyDataSetChanged()
              rv_point_stock.adapter=infoadapter
              point_tips.visibility=View.VISIBLE
          }
          1->{
              presenter?.getInfoData()
              pointInfoAdapter?.notifyDataSetChanged()
              rv_point_stock.adapter=pointInfoAdapter
              point_tips.visibility=View.GONE
          }
      }
    }

    override fun addInfoToAdapter(list: List<Int>) {
        infoadapter?.clearItems()
        if (infoadapter?.items == null) {
            infoadapter?.items = ArrayList()
        }
        infoadapter?.addItems(list)
    }

    override fun addPointInfoAdapter(list: List<Int>) {
        pointInfoAdapter?.clearItems()
        if (pointInfoAdapter?.items == null) {
            pointInfoAdapter?.items = ArrayList()
        }
        pointInfoAdapter?.addItems(list)
    }


}