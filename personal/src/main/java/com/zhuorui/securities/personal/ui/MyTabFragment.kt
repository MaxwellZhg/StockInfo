package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsBackFinishFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.databinding.FragmentMyTabBinding
import com.zhuorui.securities.personal.event.JumpToOpenAccountEvent
import com.zhuorui.securities.personal.event.JumpToSimulationTradingStocksEvent
import com.zhuorui.securities.personal.ui.presenter.MyTabPresenter
import com.zhuorui.securities.personal.ui.view.MyTabVierw
import com.zhuorui.securities.personal.ui.viewmodel.MyTabVierwModel
import kotlinx.android.synthetic.main.fragment_my_tab.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/27 14:10
 *    desc   : 主页“我的”tab界面
 */
class MyTabFragment :
    AbsBackFinishFragment<FragmentMyTabBinding, MyTabVierwModel, MyTabVierw, MyTabPresenter>(),
    MyTabVierw, View.OnClickListener {

    companion object {
        fun newInstance(): MyTabFragment {
            return MyTabFragment()
        }
    }

    override val layout: Int
        get() = R.layout.fragment_my_tab

    override val viewModelId: Int
        get() = BR.viewModel

    override val createPresenter: MyTabPresenter
        get() = MyTabPresenter()

    override val createViewModel: MyTabVierwModel?
        get() = ViewModelProviders.of(this).get(MyTabVierwModel::class.java)

    override val getView: MyTabVierw
        get() = this

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        when (presenter?.getLoginStatus()) {
            false -> {
                (parentFragment as AbsFragment<*, *, *, *>).start(LoginRegisterFragment.newInstance())
            }
        }
        open_account.setOnClickListener(this)
        simulation_trading_stocks.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            open_account -> {
                // 极速开户
                RxBus.getDefault().post(JumpToOpenAccountEvent())
            }
            simulation_trading_stocks -> {
                // 模拟炒股
                RxBus.getDefault().post(JumpToSimulationTradingStocksEvent())
            }
        }
    }
}