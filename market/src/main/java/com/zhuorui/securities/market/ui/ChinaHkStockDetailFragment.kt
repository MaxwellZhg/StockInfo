package com.zhuorui.securities.market.ui

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentChinaHkStockDetailBinding
import com.zhuorui.securities.market.ui.adapter.AllHkStockContainerAdapter
import com.zhuorui.securities.market.ui.adapter.AllHkStockNameAdapter
import com.zhuorui.securities.market.ui.presenter.ChinaHkStockDetailPresenter
import com.zhuorui.securities.market.ui.view.ChinaHkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.ChinaHkStockDetailViewModel
import kotlinx.android.synthetic.main.fragment_all_hk_stock.*
import kotlinx.android.synthetic.main.fragment_china_hk_stock_tab.*
import kotlinx.android.synthetic.main.layout_filters_hk_stock_info.*
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
 * Date: 2019/10/29
 * Desc:港股通详情
 * */
class ChinaHkStockDetailFragment :AbsSwipeBackNetFragment<FragmentChinaHkStockDetailBinding,ChinaHkStockDetailViewModel,ChinaHkStockDetailView,ChinaHkStockDetailPresenter>(),ChinaHkStockDetailView,View.OnClickListener{
    private var tabTitle:ArrayList<String> = ArrayList()
    private var nameAdapter: AllHkStockNameAdapter? = null
    private var conAdapter: AllHkStockContainerAdapter? = null
    private var showFilters = false
    override val layout: Int
        get() = R.layout.fragment_china_hk_stock_detail
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: ChinaHkStockDetailPresenter
        get() = ChinaHkStockDetailPresenter()
    override val createViewModel: ChinaHkStockDetailViewModel?
        get() = ViewModelProviders.of(this).get(ChinaHkStockDetailViewModel::class.java)
    override val getView: ChinaHkStockDetailView
        get() = this

    companion object {
        fun newInstance(): ChinaHkStockDetailFragment {
            return ChinaHkStockDetailFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        tabTitle.add("沪股通")
        tabTitle.add("深股通")
        tabTitle.add("港股通(沪)")
        tabTitle.add("港股通(深)")
        magic_indicator.navigator=getNavigator()
        top_bar.setRight2ClickListener {
            // 搜索
            start(SearchInfoFragment.newInstance())
        }
        requireActivity().layoutInflater.inflate(R.layout.table_right_title, right_title_container)
        presenter?.setLifecycleOwner(this)
        nameAdapter = presenter?.getAllHkStockNameAdapter()
        conAdapter = presenter?.getAllHkStockContainerAdapter()
        right_container_rv.setHasFixedSize(true)
        presenter?.getAllHkStockNameData()
        presenter?.getAllHkStockContentData()
        left_container_rv.adapter = nameAdapter
        right_container_rv.adapter = conAdapter
        title_horsv.setScrollView(content_horsv)
        content_horsv.setScrollView(title_horsv)
        tv_filters.setOnClickListener(this)
    }

    override fun addIntoAllHkStockName(list: List<Int>) {
        nameAdapter?.clearItems()
        if (nameAdapter?.items == null) {
            nameAdapter?.items = ArrayList()
        }
        nameAdapter?.addItems(list)
    }

    override fun addIntoAllHkContainer(list: List<Int>) {
        conAdapter?.clearItems()
        if (conAdapter?.items == null) {
            conAdapter?.items = ArrayList()
        }
        conAdapter?.addItems(list)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_filters -> {
                val values = IntArray(2)
                if (showFilters) {
                    values[0] =  ResUtil.getDimensionDp2Px(330f)
                    values[1] = 0
                    showFilters = false
                } else {
                    values[0] = 0
                    values[1] = ResUtil.getDimensionDp2Px(330f)
                    showFilters = true
                }
                val valueAnimator = ValueAnimator.ofInt(values[0], values[1])
                valueAnimator.duration = 150
                valueAnimator.addUpdateListener { animation ->
                    val value = animation.animatedValue as Int
                    val layoutParams = ll_pick_tips.layoutParams
                    layoutParams.height = value
                    ll_pick_tips?.layoutParams = layoutParams
                }
                valueAnimator.start()
            }
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
                colorTransitionPagerTitleView.normalColor =
                    ResUtil.getColor(R.color.color_B3BCD0)!!
                colorTransitionPagerTitleView.selectedColor =
                    ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 14f
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
}