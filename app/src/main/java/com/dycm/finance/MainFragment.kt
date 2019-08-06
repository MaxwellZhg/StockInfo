package com.dycm.finance

import android.os.Bundle
import android.view.View
import com.dycm.applib1.ui.StockTabFragment
import com.dycm.applib2.TestSubFragment
import com.dycm.applib3.ui.InfomationFragment
import com.dycm.applib4.ui.OpenAccountFragment
import com.dycm.base2app.ui.fragment.AbsBackFinishNetFragment
import com.dycm.finance.view.BottomBar
import com.dycm.finance.view.BottomBarTab
import com.example.applib5.ui.MyFragment
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/6
 * Desc:
 */
class MainFragment : AbsBackFinishNetFragment() {
    private  val FIRST = 0
    private val SECOND = 1
    private val THIRD = 2
    private val FOUR = 3
    private val FIVE = 4
    private val mFragments = arrayOfNulls<SupportFragment>(5)
    override val layout: Int
        get() = R.layout.fragment_main
   // private var mBottomBar: BottomBar? = null

    override fun init() {
       // mBottomBar = view?.findViewById<View>(R.id.bottomBar) as BottomBar

        bottomBar!!
            .addItem(BottomBarTab(_mActivity, R.mipmap.checked, getString(R.string.checked)))
            .addItem(BottomBarTab(_mActivity,R.mipmap.market_info, getString(R.string.market_info)))
            .addItem(BottomBarTab(_mActivity, R.mipmap.infomation, getString(R.string.infomation)))
            .addItem(BottomBarTab(_mActivity, R.mipmap.open_account, getString(R.string.open_account)))
            .addItem(BottomBarTab(_mActivity, R.mipmap.myself, getString(R.string.my_self)))

        bottomBar!!.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener{
            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(mFragments[position], mFragments[prePosition])

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
            mFragments[FIRST] = StockTabFragment()
            mFragments[SECOND] = TestSubFragment()
            mFragments[THIRD] = InfomationFragment()
            mFragments[FOUR]=  OpenAccountFragment()
            mFragments[FIVE]=  MyFragment()
            loadMultipleRootFragment(
                R.id.fl_tab_container, FIRST,
                mFragments[FIRST],
                mFragments[SECOND],
                mFragments[THIRD],
                mFragments[FOUR],
                mFragments[FIVE]
            )
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findChildFragment(TestSubFragment::class.java)
            mFragments[THIRD] = findChildFragment(InfomationFragment::class.java)
            mFragments[FOUR] = findChildFragment(OpenAccountFragment::class.java)
            mFragments[FIVE] = findChildFragment(MyFragment::class.java)
        }
    }

}