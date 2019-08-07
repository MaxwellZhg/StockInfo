package com.dycm.applib1.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dycm.applib1.R
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment
import com.dycm.base2app.util.ResUtil
import kotlinx.android.synthetic.main.fragment_tab_choose.*
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import java.util.ArrayList
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator










/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/7
 * Desc:
 */
class ChooseTabStockFragment : AbsBackFinishNetFragment(){
    private var mtitle: ArrayList<String> = ArrayList()
    private var mfragment: ArrayList<Fragment> = ArrayList()

    override val layout: Int
        get() = R.layout.fragment_tab_choose

    override fun init() {
        mtitle.add(context!!.resources.getString(R.string.all_stock))
        mtitle.add(context!!.resources.getString(R.string.hk_stock))
        mtitle.add(context!!.resources.getString(R.string.sh_stock))
        mfragment.add(AllChooseFragment(1))
        mfragment.add(AllChooseFragment(2))
        mfragment.add(AllChooseFragment(3))
        val commonNavigator = CommonNavigator(requireContext())
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return if (mfragment == null) 0 else mfragment.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = ResUtil.getColor(R.color.un_tab_select)!!
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.text = mtitle[index]
                colorTransitionPagerTitleView.setOnClickListener {
                   viewpager.currentItem=index
                }
                return colorTransitionPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                return indicator
            }
        }
        viewpager.adapter= fragmentManager?.let { MyAdapter(it) }
        viewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                magic_indicator.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                magic_indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            override fun onPageSelected(position: Int) {
                magic_indicator.onPageSelected(position)
            }

        });

        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, viewpager)
    }
    override fun rootViewFitsSystemWindowsPadding(): Boolean {
        return true
    }
    inner class MyAdapter(fm:FragmentManager) :FragmentPagerAdapter(fm){
        override fun getItem(position: Int): Fragment {
            return mfragment[position]
        }

        override fun getCount(): Int {
            return mtitle.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mtitle[position]
        }

    }
}
