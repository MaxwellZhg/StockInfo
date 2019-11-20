package com.zhuorui.securities.infomation.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.zhuorui.securities.base2app.ui.fragment.AbsBackFinishFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.infomation.BR
import com.zhuorui.securities.infomation.R
import com.zhuorui.securities.infomation.databinding.FragmentInfomationTabBinding
import com.zhuorui.securities.infomation.ui.presenter.InfomationTabPresenter
import com.zhuorui.securities.infomation.ui.view.InfomationTabView
import com.zhuorui.securities.infomation.ui.viewmodel.InfomationTabViewModel
import com.zhuorui.securities.personal.ui.MessageFragment
import kotlinx.android.synthetic.main.fragment_infomation_tab.*
import net.lucode.hackware.magicindicator.abs.IPagerNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/27 14:23
 *    desc   : 主页资讯tab页面
 */
class InfomationTabFragment :
    AbsBackFinishFragment<FragmentInfomationTabBinding, InfomationTabViewModel, InfomationTabView, InfomationTabPresenter>(),
    InfomationTabView {
    private var mfragment:ArrayList<String?> = ArrayList()
    private val mFragments: Array<AbsFragment<*, *, *, *>?> = arrayOfNulls<AbsFragment<*, *, *, *>>(4)
    private var mIndex = 0
    companion object {
        fun newInstance(): InfomationTabFragment {
            return InfomationTabFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_infomation_tab

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: InfomationTabPresenter
        get() = InfomationTabPresenter()

    override val createViewModel: InfomationTabViewModel?
        get() = ViewModelProviders.of(this).get(InfomationTabViewModel::class.java)

    override val getView: InfomationTabView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        top_bar.setRightClickListener {
            // 消息
            (parentFragment as AbsFragment<*, *, *, *>).start(MessageFragment.newInstance())
        }
        top_bar.setRight2ClickListener {
            // 搜索
           // (parentFragment as AbsFragment<*, *, *, *>).start(SearchInfoFragment.newInstance())
        }
        mfragment.add(ResUtil.getString(R.string.main_news))
        mfragment.add(ResUtil.getString(R.string.topic_news))
        mfragment.add(ResUtil.getString(R.string.frist_news))
        mfragment.add(ResUtil.getString(R.string.china_stock_news))
        mfragment.add(ResUtil.getString(R.string.hk_stock_news))
        mfragment.add(ResUtil.getString(R.string.date_news))
        magic_indicator.navigator = getNavigator()
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
                return mfragment?.size!!
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(context)
                colorTransitionPagerTitleView.normalColor = Color.parseColor("#282828")
                colorTransitionPagerTitleView.selectedColor = ResUtil.getColor(R.color.tab_select)!!
                colorTransitionPagerTitleView.textSize = 18f
                colorTransitionPagerTitleView.text = mfragment!![index]
                colorTransitionPagerTitleView.setOnClickListener {
                    magic_indicator.onPageSelected(index)
                    magic_indicator.onPageScrolled(index, 0.0F, 0)
                    //onSelect(index)
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

}