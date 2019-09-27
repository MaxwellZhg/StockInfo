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
    private var type: Int = 0
    private var tips : String? =null
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
        type = arguments?.getSerializable("type") as Int
        tips = arguments?.getSerializable("tips") as String
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
            RxBus.getDefault().post(SettingChooseEvent(type,adapter?.getTips()))
            pop()
        }
        title_bar.setBackClickListener{
            RxBus.getDefault().post(SettingChooseEvent(type,adapter?.getTips()))
            pop()
        }
    }

    companion object {
        fun newInstance(type: Int,tips:String): SettingFragment {
            val fragment = SettingFragment()
            if (type != 0) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                bundle.putSerializable("tips",tips)
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}