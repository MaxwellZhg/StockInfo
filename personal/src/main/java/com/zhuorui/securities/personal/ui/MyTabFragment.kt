package com.zhuorui.securities.personal.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.ui.fragment.AbsBackFinishFragment
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.BR
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.config.LocalAccountConfig
import com.zhuorui.securities.personal.databinding.FragmentMyTabBinding
import com.zhuorui.securities.personal.event.JumpToOpenAccountEvent
import com.zhuorui.securities.personal.event.JumpToSimulationTradingStocksEvent
import com.zhuorui.securities.personal.ui.presenter.MyTabPresenter
import com.zhuorui.securities.personal.ui.view.MyTabVierw
import com.zhuorui.securities.personal.ui.viewmodel.MyTabVierwModel
import kotlinx.android.synthetic.main.fragment_my_tab.*
import me.yokeyword.fragmentation.ISupportFragment

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/27 14:10
 *    desc   : 主页“我的”tab界面
 */
class MyTabFragment :
    AbsBackFinishFragment<FragmentMyTabBinding, MyTabVierwModel, MyTabVierw, MyTabPresenter>(),
    MyTabVierw, View.OnClickListener{
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
        top_bar.setRightClickListener {
            (parentFragment as AbsFragment<*, *, *, *>).start(MessageFragment.newInstance())
        }
        help_center_bar.setOnClickListener(this)
        ll_login_out.setOnClickListener(this)
        ll_login.setOnClickListener(this)
        ll_cell_change_color.setOnClickListener(this)
        ll_setting_language.setOnClickListener(this)
        ll_account_safety.setOnClickListener(this)
        open_account.setOnClickListener(this)
        simulation_trading_stocks.setOnClickListener(this)
        ll_about_us.setOnClickListener(this)
        if(LocalAccountConfig.read().getAccountInfo().token==""||LocalAccountConfig.read().getAccountInfo().token==null){
            ll_login_out.visibility=View.INVISIBLE
            tv_login_tips.text=ResUtil.getString(R.string.login_register)
        }else{
            ll_login_out.visibility=View.VISIBLE
            tv_login_tips.text=LocalAccountConfig.read().getAccountInfo().userId
        }
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.open_account -> {
                // 极速开户
                RxBus.getDefault().post(JumpToOpenAccountEvent())
            }
            R.id.simulation_trading_stocks -> {
                // 模拟炒股
                RxBus.getDefault().post(JumpToSimulationTradingStocksEvent())
            }
            R.id.help_center_bar -> {
                (parentFragment as AbsFragment<*, *, *, *>).start(OAHelpCenterFragment.newInstance())
            }
            R.id.ll_login_out -> {
                presenter?.requestUserLoginOut()
            }
            R.id.ll_login -> {
                if(LocalAccountConfig.read().getAccountInfo().token==""||LocalAccountConfig.read().getAccountInfo().token==null) {
                    (parentFragment as AbsFragment<*, *, *, *>).start(LoginRegisterFragment.newInstance())
                }
            }
            R.id.ll_cell_change_color -> {
                startForResult(SettingFragment.newInstance(1,ll_cell_change_color.tipsValue), 100)
            }
            R.id.ll_setting_language -> {
                startForResult(SettingFragment.newInstance(2,ll_setting_language.tipsValue), 101)
            }
            R.id.ll_account_safety->{
                (parentFragment as AbsFragment<*, *, *, *>).start(SecurityFragment.newInstance())
            }
            R.id.ll_about_us->{
                (parentFragment as AbsFragment<*, *, *, *>).start(IntroProFragment.newInstance())
            }
        }
    }

    override fun gotomain() {
        if(LocalAccountConfig.read().getAccountInfo().token==""||LocalAccountConfig.read().getAccountInfo().token==null){
            ll_login_out.visibility=View.INVISIBLE
            tv_login_tips.text=ResUtil.getString(R.string.login_register)
        }else{
            ll_login_out.visibility=View.VISIBLE
            tv_login_tips.text=LocalAccountConfig.read().getAccountInfo().userId
        }
        (parentFragment as AbsFragment<*, *, *, *>).start(LoginRegisterFragment.newInstance())
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        if (resultCode == ISupportFragment.RESULT_OK && requestCode == 100) {
            ll_cell_change_color.setRightTips(data?.getString("str"))
        } else {
            ll_setting_language.setRightTips(data?.getString("str"))
        }
    }
    override fun loginStateChange() {
        if(LocalAccountConfig.read().getAccountInfo().token==""||LocalAccountConfig.read().getAccountInfo().token==null){
            ll_login_out.visibility=View.INVISIBLE
            tv_login_tips.text=ResUtil.getString(R.string.login_register)
        }else{
            ll_login_out.visibility=View.VISIBLE
            tv_login_tips.text=LocalAccountConfig.read().getAccountInfo().userId
        }
    }


}