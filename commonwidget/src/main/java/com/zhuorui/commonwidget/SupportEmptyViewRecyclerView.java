package com.zhuorui.commonwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.securities.base2app.infra.LogInfra;

/**
 * 支持RecyclerView里面添加setEmptyView
 */
public class SupportEmptyViewRecyclerView extends RecyclerView {

    private static final String TAG = "RecyclerViewEmptySupport";
    /**
     * 当数据为空时展示的View
     */
    private View mEmptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {
        @SuppressLint("LongLogTag")
        @Override
        public void onChanged() {
            LogInfra.Log.i(TAG, "onChanged: 000");
            Adapter<?> adapter = getAdapter();
            //判断数据为空否，再进行显示或者隐藏
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    SupportEmptyViewRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    SupportEmptyViewRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public SupportEmptyViewRecyclerView(Context context) {
        super(context);
    }

    public SupportEmptyViewRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SupportEmptyViewRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * * @param emptyView 展示的空view
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        LogInfra.Log.i(TAG, "setAdapter: adapter::" + adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }
        emptyObserver.onChanged();
    }
}