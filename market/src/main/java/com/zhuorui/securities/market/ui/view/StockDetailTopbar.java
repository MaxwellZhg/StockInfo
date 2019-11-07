package com.zhuorui.securities.market.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.zhuorui.securities.base2app.util.StatusBarUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.util.MarketUtil;
import me.yokeyword.fragmentation.ISupportActivity;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-11-07 15:43
 * desc   :
 */
public class StockDetailTopbar extends LinearLayout {

    private View vBack;
    private TextView vTitle;
    private TextView vStatus;
    private ImageView vTsLogo;
    private View vSearch;
    private View vRefresh;

    public StockDetailTopbar(Context context) {
        this(context, null);
    }

    public StockDetailTopbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockDetailTopbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_stock_detail_topbar, this);
        vBack = findViewById(R.id.iv_back);
        vTitle = findViewById(R.id.tv_title);
        vStatus = findViewById(R.id.tv_status);
        vTsLogo = findViewById(R.id.iv_ts_logo);
        vSearch = findViewById(R.id.iv_search);
        vRefresh = findViewById(R.id.iv_refresh);
        setPadding(0, StatusBarUtil.getStatusBarHeight(context), 0, 0);
        setBackClickListener(view -> {
            Context context1 = view.getContext();
            if (context1 instanceof ISupportActivity) {
                ((ISupportActivity) context1).onBackPressedSupport();
            }
        });
    }

    public void setBackClickListener(OnClickListener onClickListener) {
        vBack.setOnClickListener(onClickListener);
    }

    public void setSearchClickListener(OnClickListener l) {
        vSearch.setOnClickListener(l);
    }

    public void setRefreshClickListener(OnClickListener l) {
        vRefresh.setOnClickListener(l);
    }

    public void setRefreshButton(boolean isShow) {
        if (isShow) {
            vRefresh.setVisibility(VISIBLE);
            post(() -> {
                int padding = vSearch.getWidth() + vRefresh.getWidth();
                vTitle.setPadding(padding, 0, padding, 0);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) vTsLogo.getLayoutParams();
                lp.leftMargin = (int) (padding - vTsLogo.getWidth() - (getResources().getDisplayMetrics().density * 6));
                vTsLogo.setLayoutParams(lp);
            });
        } else {
            vRefresh.setVisibility(GONE);
            post(() -> {
                int padding = vSearch.getWidth();
                vTitle.setPadding(padding, 0, padding, 0);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) vTsLogo.getLayoutParams();
                lp.leftMargin = (int) (padding - vTsLogo.getWidth() - (getResources().getDisplayMetrics().density * 6));
                vTsLogo.setLayoutParams(lp);
            });
        }
    }

    public void setStockInfo(String ts, String name, String code) {
        vTsLogo.setImageResource(MarketUtil.getStockTSIcon(ts));
        vTitle.setText(name + "(" + code + ")");
    }

    public void setStatus(CharSequence text, int color) {
        vStatus.setTextColor(color);
        vStatus.setText(text);
    }

}
