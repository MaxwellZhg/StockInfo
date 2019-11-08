package com.zhuorui.commonwidget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by PengXianglin on 2018/8/15.
 * 用于Linear模式RecyclerView控制间距
 */
class LinearSpacingItemDecoration(
    private val spacing: Int //间隔
    , private val includeEdge: Boolean //是否包含边缘
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        //这里是关键，需要根据你有几列来判断
        val position = parent.getChildAdapterPosition(view) // item position

        if (includeEdge) {
            outRect.top = spacing// top edge
            if (position == parent.layoutManager!!.itemCount) {
                outRect.bottom = spacing // item bottom
            }
        } else {
            if (position > 0) { // top edge
                outRect.top = spacing
            }
        }
    }
}