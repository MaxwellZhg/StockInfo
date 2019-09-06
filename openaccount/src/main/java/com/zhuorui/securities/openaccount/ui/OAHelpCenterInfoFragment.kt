package com.zhuorui.securities.openaccount.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.SimpleItemAnimator
import com.zhuorui.securities.base2app.ui.fragment.AbsSwipeBackFragment
import com.zhuorui.securities.openaccount.BR
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.adapter.HelpCenterInfoAdapter
import com.zhuorui.securities.openaccount.databinding.FragmentOaHelpcenterInfoBinding
import com.zhuorui.securities.openaccount.ui.presenter.OAHelpCenterInfoPresenter
import com.zhuorui.securities.openaccount.ui.view.OAHelpCenterInfoView
import com.zhuorui.securities.openaccount.ui.viewmodel.OAHelpCenterInfoViewModel
import kotlinx.android.synthetic.main.fragment_oa_helpcenter_info.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/5
 * Desc:
 */
class OAHelpCenterInfoFragment :AbsSwipeBackFragment<FragmentOaHelpcenterInfoBinding,OAHelpCenterInfoViewModel,OAHelpCenterInfoView,OAHelpCenterInfoPresenter>(),OAHelpCenterInfoView{
    private var type: Int = 0
    private var adapter: HelpCenterInfoAdapter? = null
    override val layout: Int
        get() = R.layout.fragment_oa_helpcenter_info
    override val viewModelId: Int
        get() = BR.viewModel
    override val createPresenter: OAHelpCenterInfoPresenter
        get() = OAHelpCenterInfoPresenter()
    override val createViewModel: OAHelpCenterInfoViewModel?
        get() = ViewModelProviders.of(this).get(OAHelpCenterInfoViewModel::class.java)
    override val getView: OAHelpCenterInfoView
        get() = this
    companion object {
        fun newInstance(type:Int): OAHelpCenterInfoFragment {
            val fragment = OAHelpCenterInfoFragment()
            if (type !=0) {
                val bundle = Bundle()
                bundle.putSerializable("type", type)
                fragment.arguments = bundle
            }
            return fragment
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        type = arguments?.getSerializable("type") as Int
        adapter = presenter?.getAdapter(type)
        presenter?.getTipsInfo(type)
        title_bar.setCancleClickListener{
            pop()
        }
        title_bar.setBackClickListener{
          startWithPopTo(OASelectRegionFragment.newInstance(),OASelectRegionFragment::class.java,true)
        }
        rv_helpcenter_info.adapter = adapter
    }

}