package com.zhuorui.commonwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-30 17:28
 * desc   :
 */
public class ZRDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Rect mBounds = new Rect();
    private boolean horizontal;
    private boolean vertical;


    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;

    public ZRDividerItemDecoration(Context context) {
        this(context, true, true);
    }

    public ZRDividerItemDecoration(Context context, boolean horizontal, boolean vertical) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    /**
     * @return the {@link Drawable} for this divider.
     */
    @Nullable
    public Drawable getDrawable() {
        return mDivider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        if (vertical && mDivider.getIntrinsicHeight() > 0) {
            drawVertical(c, parent);
        }
        if (horizontal && mDivider.getIntrinsicWidth() > 0) {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isBottom(parent, i)) break;
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - getDrawable().getIntrinsicHeight();
            getDrawable().setBounds(left, top, right, bottom);
            getDrawable().draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isRight(parent, i)) continue;
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - getDrawable().getIntrinsicWidth();
            getDrawable().setBounds(left, top, right, bottom);
            getDrawable().draw(canvas);
        }
        canvas.restore();
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (getDrawable() == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        int right = 0;
        int bottom = 0;
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (vertical && getDrawable().getIntrinsicHeight() > 0) {
            bottom = isBottom(parent, position) ? 0 : getDrawable().getIntrinsicHeight();
        }
        if (horizontal && getDrawable().getIntrinsicWidth() > 0) {
            right = isRight(parent, position) ? 0 : getDrawable().getIntrinsicWidth();
        }
        outRect.set(0, 0, right, bottom);
    }

    private boolean isBottom(RecyclerView parent, int position) {
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int childCount = parent.getAdapter().getItemCount();
        int lastLineCount = childCount % spanCount;
        lastLineCount = lastLineCount == 0 ? spanCount : lastLineCount;
        return childCount - position <= lastLineCount;
    }

    private boolean isRight(RecyclerView parent, int position) {
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        return position % spanCount == spanCount - 1;

    }
}
