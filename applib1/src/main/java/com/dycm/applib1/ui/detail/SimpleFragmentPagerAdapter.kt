package com.dycm.applib1.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * 装载Fragment的通用适配器
 */
class SimpleFragmentPagerAdapter : FragmentStatePagerAdapter {
    private var mFragments: List<Fragment>? = null
    private var mTitles: List<String>? = null

    constructor(fm: FragmentManager, fragments: Array<Fragment>) : super(fm) {
        mFragments = listOf(*fragments)
    }

    constructor(fm: FragmentManager, fragments: List<Fragment>) : super(fm) {
        mFragments = fragments
    }

    constructor(fm: FragmentManager, fragments: Array<Fragment>, titles: Array<String>) : this(fm, fragments) {
        mTitles = listOf(*titles)
    }

    constructor(fm: FragmentManager, fragments: List<Fragment>, titles: List<String>) : this(fm, fragments) {
        mTitles = titles
    }

    override fun getItem(position: Int): Fragment {
        return mFragments!![position]
    }

    override fun getCount(): Int {
        return mFragments!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (mTitles != null && mTitles!!.isNotEmpty()) {
            mTitles!![position]
        } else super.getPageTitle(position)
    }

}
