package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.common.CommonEnum
import com.zhuorui.securities.base2app.adapter.BaseListAdapter
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackNetFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentSettingBinding
import com.zhuorui.securities.personal.event.SettingChooseEvent
import com.zhuorui.securities.personal.ui.adapter.HelpCenterInfoAdapter
import com.zhuorui.securities.personal.ui.adapter.SettingDataAdapter
import com.zhuorui.securities.personal.ui.model.SettingData
import com.zhuorui.securities.personal.ui.presenter.SettingPresenter
import com.zhuorui.securities.personal.ui.view.SettingView
import com.zhuorui.securities.personal.ui.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.fragment_setting.*
import me.jessyan.autosize.utils.LogUtils
import me.yokeyword.fragmentation.ISupportFragment

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/9
 * Desc:setting 界面
 */
class SettingFragment : AbsSwipeBackNetFragment<FragmentSettingBinding, SettingViewModel, SettingView, SettingPresenter>(),
    SettingView{
    private var type: Int = -1
    private var tips : String =""
    private var adapter: SettingDataAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_setting
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: SettingPresenter
        get() = SettingPresenter()
    override val createViewModel: SettingViewModel?
        get() = ViewModelProviders.of(this).get(SettingViewModel::class.java)
    override val getView: SettingView
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getInt("type")?:type
        tips = arguments?.getString("tips") ?:tips
        when (type) {
            1 -> {
                title_bar.setTitle(ResUtil.getString(R.string.change_color))
            }
            2 -> {
                title_bar.setTitle(ResUtil.getString(R.string.setting_language))
            }
        }
        adapter = presenter?.getAdapter()
        presenter?.getData(type,tips)
        lv_info.adapter = adapter
        title_bar.setRightTextViewClickListener{
            //todo 国际化语言和颜色涨跌post设置
            presenter?.detailSaveState(type,adapter?.getTips())
            pop()
            adapter?.getTips()?.let { it1 -> presenter?.detailSettingEvent(type, it1) }
        }
        title_bar.setBackClickListener{
            pop()
            adapter?.getTips()?.let { it1 -> presenter?.detailSettingEvent(type, it1) }
        }
    }

    companion object {
        fun newInstance(type: Int,tips:String): SettingFragment {
            val fragment = SettingFragment()
            if (type != 0) {
                val bundle = Bundle()
                bundle.putInt("type", type)
                bundle.putString("tips",tips)
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}