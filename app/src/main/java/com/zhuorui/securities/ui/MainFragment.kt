package com.zhuorui.securities.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.zhuorui.commonwidget.dialog.ConfirmToCancelDialog
import com.zhuorui.securities.BR
import com.zhuorui.securities.R
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.custom.view.BottomBar
import com.zhuorui.securities.custom.view.BottomBarTab
import com.zhuorui.securities.databinding.FragmentMainBinding
import com.zhuorui.securities.infomation.ui.InfomationTabFragment
import com.zhuorui.securities.market.ui.MarketTabFragment
import com.zhuorui.securities.market.ui.StockTabFragment
import com.zhuorui.securities.openaccount.ui.OpenAccountTabFragment
import com.zhuorui.securities.personal.ui.LoginRegisterFragment
import com.zhuorui.securities.personal.ui.MyTabFragment
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/6
 * Desc: 主界面
 */
class MainFragment :
    AbsFragment<FragmentMainBinding, MainFragmentViewModel, MainFragmentView, MainFramgentPresenter>(),
    MainFragmentView {
    var currentPos: Int = -1
    //    var prePos: Int = -1
    private val FIRST = 0
    private val SECOND = 1
    private val THIRD = 2
    private val FOUR = 3
    private val FIVE = 4
    private val mFragments = arrayOfNulls<AbsFragment<*, *, *, *>>(5)

    override val layout: Int
        get() = R.layout.fragment_main

    override val viewModelId: Int
        get() = BR.viewModel

    override val getView: MainFragmentView
        get() = this

    override val createPresenter: MainFramgentPresenter
        get() = MainFramgentPresenter()

    override val createViewModel: MainFragmentViewModel?
        get() = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        // mBottomBar = view?.findViewById<View>(R.id.bottomBar) as BottomBar
        bottomBar!!
            .addItem(
                BottomBarTab(
                    _mActivity,
                    R.mipmap.checked, getString(R.string.checked)
                )
            )
            .addItem(
                BottomBarTab(
                    _mActivity,
                    R.mipmap.market_info, getString(R.string.market_info)
                )
            )
            .addItem(
                BottomBarTab(
                    _mActivity,
                    R.mipmap.infomation, getString(R.string.infomation)
                )
            )
            .addItem(
                BottomBarTab(
                    _mActivity,
                    R.mipmap.open_account, getString(R.string.open_account)
                )
            )
            .addItem(
                BottomBarTab(
                    _mActivity,
                    R.mipmap.myself, getString(R.string.my_self)
                )
            )

        bottomBar!!.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(mFragments[position], mFragments[prePosition])
                currentPos = position
//                prePos = prePosition
            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {
            }

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val firstFragment = findChildFragment(StockTabFragment::class.java)
        if (firstFragment == null) {
            mFragments[FIRST] = StockTabFragment.newInstance()
            mFragments[SECOND] = MarketTabFragment.newInstance()
            mFragments[THIRD] = InfomationTabFragment.newInstance()
            mFragments[FOUR] = OpenAccountTabFragment.newInstance()
            mFragments[FIVE] = MyTabFragment.newInstance()
            loadMultipleRootFragment(
                R.id.fl_tab_container, FIRST,
                mFragments[FIRST],
                mFragments[SECOND],
                mFragments[THIRD],
                mFragments[FOUR],
                mFragments[FIVE]
            )
            currentPos = FIRST
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findChildFragment(MarketTabFragment::class.java)
            mFragments[THIRD] = findChildFragment(InfomationTabFragment::class.java)
            mFragments[FOUR] = findChildFragment(OpenAccountTabFragment::class.java)
            mFragments[FIVE] = findChildFragment(MyTabFragment::class.java)
        }
    }

    override fun jumpToLogin() {
        //设备下线
        ToastUtil.instance.toastCenter(R.string.other_dev_login_tips)
        start(LoginRegisterFragment.newInstance(1))
    }

    override fun inOpenAccoutTab(): Boolean {
        return topFragment == this && currentPos == FOUR
    }

    override fun showOpenAccountDailog() {
        ConfirmToCancelDialog.createWidth265Dialog(requireContext(), false, false)
            .setMsgText(com.zhuorui.securities.personal.R.string.register_tips)
            .setCancelText(com.zhuorui.securities.personal.R.string.cancle)
            .setConfirmText(com.zhuorui.securities.personal.R.string.complete_info)
            .setCallBack(object : ConfirmToCancelDialog.CallBack {
                override fun onCancel() {

                }

                override fun onConfirm() {
                    // 返回首页
                    val homeFragment = (activity as AbsActivity).findFragment(MainFragment::class.java)
                    popTo(homeFragment::class.java, false)
                    bottomBar.setCurrentItem(FOUR)
                    currentPos = FOUR
                }
            }).show()
    }
}