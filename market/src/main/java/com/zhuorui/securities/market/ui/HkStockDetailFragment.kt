package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.config.LocalSettingsConfig
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.PushIndexHandicapData
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData
import com.zhuorui.securities.market.socket.vo.StockHandicapData
import com.zhuorui.securities.market.ui.adapter.MarketPartInfoAdapter
import com.zhuorui.securities.market.ui.presenter.HkStockDetailPresenter
import com.zhuorui.securities.market.ui.view.HkStockDetailView
import com.zhuorui.securities.market.ui.viewmodel.HkStockDetailViewModel
import com.zhuorui.securities.market.util.MathUtil
import kotlinx.android.synthetic.main.fragment_hk_stock_detail.*
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
import java.math.BigDecimal

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/17
 * Desc:港股详情
 * */
class HkStockDetailFragment :
    AbsFragment<com.zhuorui.securities.market.databinding.FragmentHkStockDetailBinding, HkStockDetailViewModel, HkStockDetailView, HkStockDetailPresenter>(),
    HkStockDetailView, View.OnClickListener,MarketPartInfoAdapter.OnAllPartInfoClickListener,MarketPartInfoAdapter.OnMainPartInfoClickListener,MarketPartInfoAdapter.OnCreatePartInfoClickListener {
    private var type: Int = -1
    private var allHkPartAdapter: MarketPartInfoAdapter? = null
    private var allMainPartAdapter: MarketPartInfoAdapter? = null
    private var allCreatePartAdapter: MarketPartInfoAdapter? = null
    private var tabTitle: ArrayList<String> = ArrayList()
    private var topType:Int = 0
    private var allHkStockIndex: Int = 0
    private var allHkMainStockIndex: Int = 0
    private var tsType:String =""
    private var preTemplete :Int = -1
    private var templete :Int = 0
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
                bundle.putInt("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getInt("type")?:type
        tabTitle.add("涨幅榜")
        tabTitle.add("跌幅榜")
        tabTitle.add("成交额")
        magic_indicator1.navigator = getNavigator(1)
        magic_indicator2.navigator = getNavigator(2)
        magic_indicator3.navigator = getNavigator(3)
        presenter?.setLifecycleOwner(this)
        allHkPartAdapter = presenter?.getMarketInfoAdapter(1)
        allMainPartAdapter = presenter?.getMarketInfoAdapter(2)
        allCreatePartAdapter = presenter?.getMarketInfoAdapter(3)
        allHkPartAdapter?.onPartInfoClickListener=this
        allMainPartAdapter?.onMainPartInfoClickListener=this
        allCreatePartAdapter?.onCreatePartInfoClickListener=this
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
        allHkPartAdapter?.notifyDataSetChanged()
        allMainPartAdapter?.notifyDataSetChanged()
        allCreatePartAdapter?.notifyDataSetChanged()
        rv_hk_stock1.adapter = allHkPartAdapter
        rv_hk_stock2.adapter = allMainPartAdapter
        rv_hk_stock3.adapter = allCreatePartAdapter
        rl_new_stock_date.setOnClickListener(this)
        ll_part_one.setOnClickListener(this)
        ll_part_two.setOnClickListener(this)
        tv_all_hk_stock.setOnClickListener(this)
        tv_main_part.setOnClickListener(this)
        tv_create_newly_part.setOnClickListener(this)
        tv_top_tips.setOnClickListener(this)
        view_line1.setOnClickListener(this)
        view_line2.setOnClickListener(this)
        view_line3.setOnClickListener(this)
        scroll_hk_detail_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (tv_all_hk_stock != null) {
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
                } else if(scrollY >= tv_main_part.top) {
                    if (topType==1) {
                        topType=2
                        tv_top_tips.text = "主板"
                        magic_indicator_top_tips.navigator = getTopNavigator(2)
                        magic_indicator_top_tips.onPageSelected(allHkMainStockIndex)
                        magic_indicator_top_tips.onPageScrolled(allHkMainStockIndex, 0.0F, 0)
                    }
                    if(oldScrollY-scrollY>0){
                        topType=0
                    }
                }
            }
        }

        presenter?.getMarketStatisticsInfo("HK")


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
        allHkPartAdapter?.clearItems()
        if (allHkPartAdapter?.items == null) {
            allHkPartAdapter?.items = ArrayList()
        }
        allHkPartAdapter?.addItems(list)
        allMainPartAdapter?.clearItems()
        if (allMainPartAdapter?.items == null) {
            allMainPartAdapter?.items = ArrayList()
        }
        allMainPartAdapter?.addItems(list)
        allCreatePartAdapter?.clearItems()
        if (allCreatePartAdapter?.items == null) {
            allCreatePartAdapter?.items = ArrayList()
        }
        allCreatePartAdapter?.addItems(list)
    }

    override fun onClick(p0: View?) {
        var pre = parentFragment as AbsFragment<*, *, *, *>
        var parent = pre.getParentFragment() as AbsFragment<*, *, *, *>
        when (p0?.id) {
            R.id.view_line1->{
               parent.start(MarketPointFragment.newInstance(1))
            }
            R.id.view_line2->{
                parent.start(MarketPointFragment.newInstance(2))
            }
            R.id.view_line3->{
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

    override fun onPartInfoClick() {
      ToastUtil.instance.toastCenter("全部港股")
    }

    override fun onMainPartInfoClick() {
        ToastUtil.instance.toastCenter("主板")
    }

    override fun onCreatePartInfoClick() {
        ToastUtil.instance.toastCenter("创业板")

    }
/*    override fun setHsiIndexData(list: List<IndexPonitHandicapData?>) {
        if(list[0]!=null) {
           when(list[0]?.code){
                "HSI"-> {
                    when {
                        list[0]?.open?.let { list[0]?.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! > BigDecimal.ZERO -> {
                            tv_one_ponit_num.setText(list[0]?.last.toString(), 1)
                            tv_one_point_rate.text = "+"+list[0]?.diffPrice.toString() + "  +" + list[0]?.diffRate.toString() + "%"
                            tv_one_point_rate.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                        }
                        list[0]?.open?.let { list[0]?.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! <BigDecimal.ZERO -> {
                            tv_one_ponit_num.setText(list[0]?.last.toString(), -1)
                            tv_one_point_rate.text = "-"+list[0]?.diffPrice.toString() + "  -" + list[0]?.diffRate.toString() + "%"
                            tv_one_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                        }
                        else -> {
                            tv_one_ponit_num.setText(list[0]?.last.toString(), 0)
                            tv_one_point_rate.text = list[0]?.diffPrice.toString() + "  " + list[0]?.diffRate.toString() + "%"
                            tv_one_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                        }
                    }
                    zr_line.setType(1)
                    zr_line.setValues(list[0]?.rise!!, list[0]?.flatPlate!!, list[0]?.fall!!)
                    zr_line_text.setType(0)
                    zr_line_text.setValues(list[0]?.rise!!, list[0]?.flatPlate!!, list[0]?.fall!!)
                }
               "HSCEI"-> {
                   when {
                       list[0]?.open?.let { list[0]?.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! > BigDecimal.ZERO -> {
                           tv_two_ponit_num.setText(list[0]?.last.toString(), 1)
                           tv_two_point_rate.text = "+"+list[0]?.diffPrice.toString() + "  +" + list[0]?.diffRate.toString() + "%"
                           tv_two_point_rate.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                       }
                       list[0]?.open?.let { list[0]?.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! <BigDecimal.ZERO -> {
                           tv_two_ponit_num.setText(list[0]?.last.toString(), -1)
                           tv_two_point_rate.text = "-"+list[0]?.diffPrice.toString() + "  -" + list[0]?.diffRate.toString() + "%"
                           tv_two_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                       }
                       else -> {
                           tv_two_ponit_num.setText(list[0]?.last.toString(), 0)
                           tv_two_point_rate.text = list[0]?.diffPrice.toString() + "  " + list[0]?.diffRate.toString() + "%"
                           tv_two_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                       }
                   }
                   zr_line1.setType(1)
                   zr_line1.setValues(list[0]?.rise!!, list[0]?.flatPlate!!, list[0]?.fall!!)
                   zr_line_text1.setType(0)
                   zr_line_text1.setValues(list[0]?.rise!!, list[0]?.flatPlate!!, list[0]?.fall!!)
               }

               "HSCCI"-> {
                   when {
                       list[0]?.open?.let { list[0]?.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! > BigDecimal.ZERO -> {
                           tv_three_ponit_num.setText(list[0]?.last.toString(), 1)
                           tv_three_ponit_rate.text = "+"+list[0]?.diffPrice.toString() + "  +" + list[0]?.diffRate.toString() + "%"
                           tv_three_ponit_rate.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                       }
                       list[0]?.open?.let { list[0]?.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! <BigDecimal.ZERO -> {
                           tv_three_ponit_num.setText(list[0]?.last.toString(), -1)
                           tv_three_ponit_rate.text = "-"+list[0]?.diffPrice.toString() + "  -" + list[0]?.diffRate.toString() + "%"
                           tv_three_ponit_rate.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                       }
                       else -> {
                           tv_three_ponit_num.setText(list[0]?.last.toString(), 0)
                           tv_three_ponit_rate.text = list[0]?.diffPrice.toString() + "  " + list[0]?.diffRate.toString() + "%"
                           tv_three_ponit_rate.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                       }
                   }
                   zr_line2.setType(1)
                   zr_line2.setValues(list[0]?.rise!!, list[0]?.flatPlate!!, list[0]?.fall!!)
                   zr_line_text2.setType(0)
                   zr_line_text2.setValues(list[0]?.rise!!, list[0]?.flatPlate!!, list[0]?.fall!!)
               }
            }
        }

    }

    override fun detailPushData(data: PushIndexHandicapData) {
        if(data.type==1&&data.code!=null){
            when(data.code){
                "HSI"-> {
                    when {
                        data.open?.let { data.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! > BigDecimal.ZERO -> {
                            tv_one_ponit_num.setText(data.last.toString(), 1)
                            tv_one_point_rate.text = "+"+data.diffPrice.toString() + "  +" + data.diffRate.toString() + "%"
                            tv_one_point_rate.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                        }
                        data.open?.let { data.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! <BigDecimal.ZERO -> {
                            tv_one_ponit_num.setText(data.last.toString(), -1)
                            tv_one_point_rate.text = "-"+data.diffPrice.toString() + "  -" + data.diffRate.toString() + "%"
                            tv_one_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                        }
                        else -> {
                            tv_one_ponit_num.setText(data.last.toString(), 0)
                            tv_one_point_rate.text = data.diffPrice.toString() + "  " + data.diffRate.toString() + "%"
                            tv_one_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                        }
                    }
                    zr_line.setType(1)
                    zr_line.setValues(data.rise!!, data.flatPlate!!, data.fall!!)
                    zr_line_text.setType(0)
                    zr_line_text.setValues(data.rise!!, data.flatPlate!!, data.fall!!)
                }
                "HSCEI"-> {
                    when {
                        data.open?.let { data.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! > BigDecimal.ZERO -> {
                            tv_two_ponit_num.setText(data.last.toString(), 1)
                            tv_two_point_rate.text = "+"+data.diffPrice.toString() + "  +" + data.diffRate.toString() + "%"
                            tv_two_point_rate.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                        }
                        data.open?.let { data.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! <BigDecimal.ZERO -> {
                            tv_two_ponit_num.setText(data.last.toString(), -1)
                            tv_two_point_rate.text = "-"+data.diffPrice.toString() + "  -" + data.diffRate.toString() + "%"
                            tv_two_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                        }
                        else -> {
                            tv_two_ponit_num.setText(data.last.toString(), 0)
                            tv_two_point_rate.text = data.diffPrice.toString() + "  " + data.diffRate.toString() + "%"
                            tv_two_point_rate.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                        }
                    }
                    zr_line1.setType(1)
                    zr_line1.setValues(data.rise!!, data.flatPlate!!, data.fall!!)
                    zr_line_text1.setType(0)
                    zr_line_text1.setValues(data.rise!!, data.flatPlate!!, data.fall!!)
                }

                "HSCCI"-> {
                    when {
                        data.open?.let { data.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! > BigDecimal.ZERO -> {
                            tv_three_ponit_num.setText(data.last.toString(), 1)
                            tv_three_ponit_rate.text = "+"+data.diffPrice.toString() + "  +" + data.diffRate.toString() + "%"
                            tv_three_ponit_rate.setTextColor(LocalSettingsConfig.getInstance().getUpColor())
                        }
                        data.open?.let { data.last?.let { it1 -> MathUtil.subtract2(it1, it) } }!! <BigDecimal.ZERO -> {
                            tv_three_ponit_num.setText(data.last.toString(), -1)
                            tv_three_ponit_rate.text = "-"+data.diffPrice.toString() + "  -" + data.diffRate.toString() + "%"
                            tv_three_ponit_rate.setTextColor(LocalSettingsConfig.getInstance().getDownColor())
                        }
                        else -> {
                            tv_three_ponit_num.setText(data.last.toString(), 0)
                            tv_three_ponit_rate.text = data.diffPrice.toString() + "  " + data.diffRate.toString() + "%"
                            tv_three_ponit_rate.setTextColor(LocalSettingsConfig.getInstance().getDefaultColor())
                        }
                    }
                    zr_line2.setType(1)
                    zr_line2.setValues(data.rise!!, data.flatPlate!!, data.fall!!)
                    zr_line_text2.setType(0)
                    zr_line_text2.setValues(data.rise!!, data.flatPlate!!, data.fall!!)
                }
            }
        }
    }*/


}