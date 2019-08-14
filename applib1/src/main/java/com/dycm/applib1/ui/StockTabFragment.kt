package com.dycm.applib1.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dycm.applib1.R
import com.dycm.applib1.databinding.FragmentStockTabBinding
import com.dycm.applib1.event.SocketDisconnectEvent
import com.dycm.applib1.model.StockTsEnum
import com.dycm.applib1.socket.SocketClient
import com.dycm.applib1.ui.contract.StockTabContract
import com.dycm.applib1.ui.mvp.StockTabPresenter
import com.dycm.applib1.ui.mvp.StockTabVmWarpper
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.mvp.wrapper.BaseMvpNetVmFragment
import com.dycm.base2app.rxbus.EventThread
import com.dycm.base2app.rxbus.RxSubscribe
import com.dycm.base2app.ui.fragment.AbsBackFinishEventFragment
import com.dycm.base2app.ui.fragment.AbsFragment
import com.dycm.base2app.util.ResUtil
import kotlinx.android.synthetic.main.fragment_stock_tab.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import java.util.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/18 10:32
 *    desc   : 主页中的自选股Tab页面
 */
class StockTabFragment : BaseMvpNetVmFragment<StockTabPresenter,StockTabVmWarpper,FragmentStockTabBinding>(), View.OnClickListener,StockTabContract.View {
    override fun createPresenter(): StockTabPresenter {
      return StockTabPresenter(this)
    }

    override fun isDestroyed(): Boolean {
       return false
    }

    override fun createWrapper(): StockTabVmWarpper {
      return StockTabVmWarpper(this)
    }

    private var mfragment: ArrayList<PageInfo> = ArrayList()

    override val layout: Int
        get() = R.layout.fragment_stock_tab

    inner class PageInfo(val title: String, val type: StockTsEnum?)

    override fun init() {
        iv_serach.setOnClickListener(this)

        // 添加标题页面
        mfragment.add(PageInfo(ResUtil.getString(R.string.all_stock)!!, null))
        mfragment.add(PageInfo(ResUtil.getString(R.string.hk_stock)!!, StockTsEnum.HK))
        mfragment.add(PageInfo(ResUtil.getString(R.string.sh_stock)!!, StockTsEnum.HS))

        // 设置viewpager指示器
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mfragment.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.un_tab_select)!!
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.text = mfragment[index].title
                colorTransitionPagerTitleView.setOnClickListener {
                    viewpager.currentItem = index
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                indicator.setColors(ResUtil.getColor(R.color.tab_select))
                return indicator
            }
        }

        // 设置viewpager页面缓存数量
        dataBinding.viewpager.offscreenPageLimit = mfragment.size
        // 设置viewpager适配器
        dataBinding.viewpager.adapter = fragmentManager?.let { ViewPagerAdapter(it) }
        dataBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                dataBinding.magicIndicator.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                dataBinding.magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            override fun onPageSelected(position: Int) {
                dataBinding.magicIndicator.onPageSelected(position)
            }
        })

        // 指示器绑定viewpager
        dataBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(dataBinding.magicIndicator, dataBinding.viewpager)

        // 启动长链接
        SocketClient.getInstance()?.connect()
    }

    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return TopicStockListFragment.newInstance(mfragment[position].type)
        }

        override fun getCount(): Int {
            return mfragment.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mfragment[position].title
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_serach -> {
                (parentFragment as AbsFragment).start(StockSearchFragment.newInstance(1))
            }
        }
    }

    @RxSubscribe(observeOnThread = EventThread.SINGLE)
    fun onSocketDisconnectEvent(event: SocketDisconnectEvent) {
        LogInfra.Log.d(TAG, "onSocketDisconnectEvent()")
        Thread.sleep(1000)
        SocketClient.getInstance()?.connect()
    }

    override fun onDestroy() {
        super.onDestroy()

        // 关闭长链接
        SocketClient.getInstance()?.destroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = generateDataBinding(inflater,container,layout)
        if (viewWrapper != null) {
            viewWrapper.setBinding(dataBinding)
        }
        presenter.fetchData()
        dataBinding.root.setPadding(0, getRootViewFitsSystemWindowsPadding(), 0, 0)
        return dataBinding.root
    }
}