package com.zhuorui.securities.ui

import android.os.Bundle
import com.zhuorui.securities.base2app.ui.activity.AbsNetActivity
import com.zhuorui.securities.R
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

class MainActivity : AbsNetActivity() {

    override val acContentRootViewId: Int
        get() = R.id.root_view

    override val layout: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.root_view, MainFragment())
        }
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置Fragment切换时默认动画为横向滑动，类似微信
        return DefaultHorizontalAnimator()
    }

    override fun statusBarLightMode(): Boolean {
        return false
    }
}
