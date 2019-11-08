package com.zhuorui.securities.market.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhuorui.securities.base2app.ui.fragment.AbsFragment
import com.zhuorui.securities.market.R
import me.yokeyword.fragmentation.SupportFragment

/**
 *    author : liuwei
 *    e-mail : vsanliu@foxmail.com
 *    date   : 2019-10-12 15:51
 *    desc   : 个股详情F10页面
 */
class MarketDetailF10Fragment : SupportFragment(), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    /* private val mFragments = arrayOfNulls<AbsFragment<*, *, *, *>>(2)

     companion object {
         fun newInstance(): MarketDetailF10Fragment {
             return MarketDetailF10Fragment()
         }
     }

     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return View.inflate(context, R.layout.fragment_market_detail_f10, null)
     }

     override fun onLazyInitView(savedInstanceState: Bundle?) {
         super.onLazyInitView(savedInstanceState)
         tv_info.setOnClickListener(this)
         tv_report.setOnClickListener(this)

 //        mFragments[0] = MarketDetailF10BriefFragment.newInstance()
         mFragments[0] = MarketDetailF10FinancialFragment.newInstance()
         mFragments[1] = MarketDetailF10FinancialFragment.newInstance()
         loadMultipleRootFragment(R.id.fl_container, 0, mFragments[0], mFragments[1])
     }

     override fun onClick(v: View?) {

         when (v) {
             tv_info -> {
                 // 简况
                 showHideFragment(mFragments[0], mFragments[1])
             }
             tv_report -> {
                 // 财报
                 showHideFragment(mFragments[1], mFragments[0])
             }
         }
     }*/
}