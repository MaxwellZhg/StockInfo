package com.dycm.applib1

import android.os.Bundle
import android.view.View
import com.dycm.base2app.ui.fragment.AbsBackFinishFragment
import com.alibaba.android.arouter.launcher.ARouter
import com.dycm.base2app.ui.fragment.AbsFragment
import kotlinx.android.synthetic.main.fragment_test.*


/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/7/18 10:32
 *    desc   :
 */
class TestFragment : AbsBackFinishFragment(), View.OnClickListener {

    override val layout: Int
        get() = R.layout.fragment_test

    override fun init() {
        button.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val fragment =
            (ARouter.getInstance().build("/lib2/TestSubFragment").navigation() as AbsFragment)
        val arg = Bundle()
        arg.putString("params", "A")
        fragment.arguments = arg
        start(fragment)
    }
}