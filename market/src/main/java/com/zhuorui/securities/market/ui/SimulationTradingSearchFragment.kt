package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhuorui.commonwidget.ZRSearchView
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.config.LocalStocksConfig
import com.zhuorui.securities.market.config.SimulationTradingSearchConfig
import com.zhuorui.securities.market.databinding.FragmentSimulationTradingSearchBinding
import com.zhuorui.securities.market.model.IStocks
import com.zhuorui.securities.market.model.SearchStockInfo
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.ui.adapter.SimulationTradingSearchAdapter
import com.zhuorui.securities.market.ui.presenter.SimulationTradingSearchPresenter
import com.zhuorui.securities.market.ui.view.SimulationTradingSearchView
import com.zhuorui.securities.market.ui.viewmodel.SimulationTradingSearchViewModel
import com.zhuorui.securities.personal.ui.MessageFragment
import kotlinx.android.synthetic.main.fragment_simulation_trading_search.*
import me.yokeyword.fragmentation.ISupportFragment
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-09-09 14:05
 *    desc   : 模拟炒股搜索
 */
class SimulationTradingSearchFragment :
    AbsSwipeBackNetFragment<FragmentSimulationTradingSearchBinding, SimulationTradingSearchViewModel, SimulationTradingSearchView, SimulationTradingSearchPresenter>(),
    SimulationTradingSearchView, ZRSearchView.OnKeyChangeListener {

    var tabTitle: Array<String>? = null
    var selectIndex = 0
    var uiStatus = -1 // 0 自选股列表 1 搜索记录列表 2 搜索结果列表
    var searchAdapter: SimulationTradingSearchAdapter? = null
    var choiceAdapter: SimulationTradingSearchAdapter? = null
    var historyAdapter: SimulationTradingSearchAdapter? = null

    companion object {
        fun newInstance(): SimulationTradingSearchFragment {
            return SimulationTradingSearchFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_simulation_trading_search

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: SimulationTradingSearchPresenter
        get() = SimulationTradingSearchPresenter()

    override val createViewModel: SimulationTradingSearchViewModel?
        get() = ViewModelProviders.of(this).get(SimulationTradingSearchViewModel::class.java)

    override val getView: SimulationTradingSearchView
        get() = this

    override fun setSearchData(datas: List<SearchStockInfo>) {
        searchAdapter?.setData(datas.toMutableList())
        searchAdapter?.notifyDataSetChanged()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        top_bar.setRightClickListener {
            // 消息
            start(MessageFragment.newInstance())
        }
        top_bar.setRight2ClickListener {
            // 搜索自选股
            start(StockSearchFragment.newInstance())
        }
        initMagicIndicator()
        search.setOnKeyChangeListener(this)
        recycler_view.layoutManager = LinearLayoutManager(context)
        onSelect(0)
    }

    private fun initMagicIndicator() {
        val tab1: String = ResUtil.getString(R.string.str_my_own_choice)!!
        val tab2: String = ResUtil.getString(R.string.str_search_history)!!
        tabTitle = arrayOf(tab1, tab2)
        // 设置viewpager指示器
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return tabTitle?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.color_232323)!!
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
        magic_indicator.navigator = commonNavigator
    }

    private fun onSelect(index: Int) {
        selectIndex = index
        changeUi("")

    }

    override fun onKeyChange(key: String) {
        changeUi(key)
        searchAdapter?.setData(null)
        searchAdapter?.notifyDataSetChanged()
        presenter?.search(key)
    }

    private fun changeUi(key: String) {
        if (!TextUtils.isEmpty(key) && uiStatus != 2) {
            uiStatus = 2
            magic_indicator.visibility = View.GONE
            recycler_view.adapter = getSearchDataAdapter()
        } else if (TextUtils.isEmpty(key)) {
            if (selectIndex == 0 && uiStatus != 0) {
                uiStatus = 0
                magic_indicator.visibility = View.VISIBLE
                recycler_view.adapter = getMyOwnChoiceAdapter()
            } else if (selectIndex == 1 && uiStatus != 1) {
                uiStatus = 1
                magic_indicator.visibility = View.VISIBLE
                recycler_view.adapter = getSearchHistoryAdapter()
            }
        }
    }

    private fun getSearchDataAdapter(): SimulationTradingSearchAdapter? {
        if (searchAdapter == null) {
            searchAdapter = SimulationTradingSearchAdapter(context!!)
            searchAdapter?.listener = object : SimulationTradingSearchAdapter.OnSimulationTradingSearchListener {
                override fun onItemClick(data: IStocks) {
                    SimulationTradingSearchConfig.read().add(data as SearchStockInfo)
                    onClickStock(data)
                }
            }
        }
        return searchAdapter
    }

    private fun getMyOwnChoiceAdapter(): SimulationTradingSearchAdapter? {
        if (choiceAdapter == null) {
            choiceAdapter = SimulationTradingSearchAdapter(context!!)
            choiceAdapter?.listener = object : SimulationTradingSearchAdapter.OnSimulationTradingSearchListener {
                override fun onItemClick(data: IStocks) {
                    onClickStock(data)
                }
            }
            val list: MutableList<StockMarketInfo> = LocalStocksConfig.read().getStocks()
            choiceAdapter?.setData(list.toMutableList())
        }
        return choiceAdapter
    }

    private fun getSearchHistoryAdapter(): SimulationTradingSearchAdapter? {
        if (historyAdapter == null) {
            historyAdapter = SimulationTradingSearchAdapter(context!!)
            historyAdapter?.setFooterView(getHistoryFooterView())
            historyAdapter?.listener = object : SimulationTradingSearchAdapter.OnSimulationTradingSearchListener {
                override fun onItemClick(data: IStocks) {
                    onClickStock(data)
                }
            }
            val list: MutableList<SearchStockInfo> = SimulationTradingSearchConfig.read().getStocks()
            historyAdapter?.setData(list.toMutableList())
        }
        return historyAdapter
    }

    private fun getHistoryFooterView(): View {
        val clear = TextView(context)
        clear.setBackgroundResource(android.R.color.white)
        clear.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        clear.setTextColor(ResUtil.getColor(R.color.color_232323)!!)
        clear.setText(R.string.str_clear_record)
        clear.gravity = Gravity.CENTER
        clear.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ResUtil.getDimensionDp2Px(42f))
        clear.setOnClickListener {
            SimulationTradingSearchConfig.read().clear()
            historyAdapter?.setData(null)
            historyAdapter?.notifyDataSetChanged()
            presenter?.clearSearchHistory()
        }
        return clear
    }

    fun onClickStock(data: IStocks) {
        var b = Bundle()
        val stockInfo = SearchStockInfo()
        stockInfo.id = data.getIID()
        stockInfo.name = data.getIName()
        stockInfo.ts = data.getITs()
        stockInfo.code = data.getICode()
        stockInfo.tsCode = data.getITsCode()
        stockInfo.type = data.getIType()
        b.putParcelable(SearchStockInfo::class.java.simpleName, stockInfo)
        setFragmentResult(ISupportFragment.RESULT_OK, b)
        pop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideSoftInput()
    }

}