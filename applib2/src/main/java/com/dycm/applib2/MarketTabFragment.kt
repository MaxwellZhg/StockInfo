package com.dycm.applib2

import android.annotation.SuppressLint
import com.alibaba.android.arouter.facade.annotation.Route
import com.dycm.base2app.ui.fragment.AbsSwipeBackFragment
import kotlinx.android.synthetic.main.fargment_testsub.*

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/18 10:40
 *    desc   :
 */
@Route(path = AppLib2Constants.FRAGMENT_URL_TESTSUB)
class MarketTabFragment : AbsSwipeBackFragment() {

    var params: String ?= null

    override val layout: Int
        get() = R.layout.fargment_testsub

    @SuppressLint("SetTextI18n")
    override fun init() {
        params = arguments?.getString("params")
        textviwe.text = textviwe.text.toString() + params
    }
}