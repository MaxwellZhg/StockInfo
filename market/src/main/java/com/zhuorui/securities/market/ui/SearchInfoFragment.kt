package com.zhuorui.securities.market.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.market.BR
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.databinding.FragmentSearchInfoBinding
import com.zhuorui.securities.market.event.ChageSearchTabEvent
import com.zhuorui.securities.market.model.SearchStokcInfoEnum
import com.zhuorui.securities.market.model.StockPageInfo
import com.zhuorui.securities.market.ui.adapter.SearchInfoAdapter
import com.zhuorui.securities.market.ui.presenter.SearchInfoPresenter
import com.zhuorui.securities.market.ui.view.SearchInfoView
import com.zhuorui.securities.market.ui.viewmodel.SearchInfoViewModel
import kotlinx.android.synthetic.main.fragment_search_info.*
import kotlinx.android.synthetic.main.fragment_search_info.magic_indicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import kotlin.collections.ArrayList

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/19
 * Desc:
 */
class SearchInfoFragment :
    AbsSwipeBackNetFragment<FragmentSearchInfoBinding, SearchInfoViewModel, SearchInfoView, SearchInfoPresenter>(),
    SearchInfoView, View.OnClickListener, TextWatcher {

    var mfragment=ArrayList<StockPageInfo>()
    private var adapter: SearchInfoAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_search_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: SearchInfoPresenter
        get() = SearchInfoPresenter(requireContext())
    override val createViewModel: SearchInfoViewModel?
        get() = ViewModelProviders.of(this).get(SearchInfoViewModel::class.java)
    override val getView: SearchInfoView
        get() = this
    private var handler = Handler()
    private var getTopicStockDataRunnable: GetTopicStockDataRunnable? = null
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }

    companion object {
        fun newInstance(): SearchInfoFragment {
            return SearchInfoFragment()
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        adapter = presenter?.getAdapter()
        search_info.adapter = adapter
        presenter?.getData()
        mfragment.add(StockPageInfo(ResUtil.getString(R.string.stock_search_all), SearchStokcInfoEnum.All))
        mfragment.add(StockPageInfo(ResUtil.getString(R.string.stock_search_topic), SearchStokcInfoEnum.Stock))
        mfragment.add(StockPageInfo(ResUtil.getString(R.string.stock_search_info), SearchStokcInfoEnum.Info))
        mfragment?.let { initViewPager(it) }
        tv_cancle.setOnClickListener(this)
        et_search_info.addTextChangedListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_cancle -> {
                pop()
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.toString().isNotEmpty()) {
            p0?.toString()?.trim()?.let {
                if (et_search_info.text.toString().isNotEmpty()) {
                    presenter?.initViewPager(it)
                    search_info.visibility = View.GONE
                    ll_search_info.visibility = View.VISIBLE
                } else {
                    search_info.visibility = View.VISIBLE
                    ll_search_info.visibility = View.GONE
                }
            }

        } else {
            search_info.visibility = View.VISIBLE
            ll_search_info.visibility = View.GONE
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    private inner class GetTopicStockDataRunnable(val keyWord: String) : Runnable {
        override fun run() {
            presenter?.initViewPager(keyWord)
        }
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): SearchResultInfoFragment {
            return SearchResultInfoFragment.newInstance(mfragment?.get(position)?.type)
        }

        override fun getCount(): Int {
            return mfragment?.size!!
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mfragment?.get(position)?.title
        }
    }

   private fun initViewPager(fragments: ArrayList<StockPageInfo>) {
        if (viewpager.adapter == null) {
            mfragment = fragments
            // 设置viewpager指示器
            val commonNavigator = CommonNavigator(requireContext())
            commonNavigator.adapter = object : CommonNavigatorAdapter() {

                override fun getCount(): Int {
                    return mfragment!!.size
                }

                override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                    val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                    colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.un_tab_select)!!
                    colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                    colorTransitionPagerTitleView.text = mfragment!![index].title
                    colorTransitionPagerTitleView.textSize = 18f
                    colorTransitionPagerTitleView.setOnClickListener {
                        viewpager.currentItem = index
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

            // 设置viewpager页面缓存数量
            viewpager.offscreenPageLimit = 3
            // 设置viewpager适配器
            viewpager.adapter = childFragmentManager?.let { ViewPagerAdapter(it) }
            viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                    magic_indicator.onPageScrollStateChanged(state)
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    magic_indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    magic_indicator.onPageSelected(position)
                }
            })

            // 指示器绑定viewpager
            magic_indicator.navigator = commonNavigator
            ViewPagerHelper.bind(magic_indicator, viewpager)
        }
    }


    override fun changeTab(enum: ChageSearchTabEvent) {
         when(enum.enum){
             SearchStokcInfoEnum.Stock->{
                 viewpager.currentItem = 1
             }
             SearchStokcInfoEnum.Info->{
                 viewpager.currentItem = 2
             }
         }
    }

}