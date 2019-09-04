package com.zhuorui.commonwidget

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout

class AndroidBug5497Workaround constructor(view: ViewGroup) : ViewTreeObserver.OnGlobalLayoutListener {

    private val mChildOfContent: View
    private var usableHeightPrevious: Int = 0
    private val frameLayoutParams: ViewGroup.LayoutParams

    init {
        mChildOfContent = view
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    fun addOnGlobalLayoutListener() {
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener(this)

    }

    fun removeOnGlobalLayoutListener() {
        mChildOfContent.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
//        return r.bottom - r.top// 全屏模式下： return r.bottom
        return r.bottom// 全屏模式下： return r.bottom
    }
}
