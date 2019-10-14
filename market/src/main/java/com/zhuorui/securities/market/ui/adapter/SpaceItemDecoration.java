package com.zhuorui.securities.market.ui.adapter;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/20
 * Desc:
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int right;
    public SpaceItemDecoration(int left,int right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        // 由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) %2==0) {
            outRect.left = 0;
            outRect.right = 0;
            outRect.right = right;
        }else{
            outRect.left = 0;
            outRect.left = left;
            outRect.right = 0;
        }
    }
}

