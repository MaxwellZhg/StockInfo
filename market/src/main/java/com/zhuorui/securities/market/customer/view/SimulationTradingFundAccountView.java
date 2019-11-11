package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.model.STFundAccountData;
import com.zhuorui.securities.market.util.MarketUtil;
import com.zhuorui.securities.market.util.MathUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-09-09 14:38
 * desc   :
 */
public class SimulationTradingFundAccountView extends FrameLayout implements View.OnClickListener {

    private OnMockStockFundAccountListener mListener;
    private View vBusiness;
    private View vOrder;
    private View vNotAcc;
    private ConstraintLayout vRootView;
    private TextView vTotalAssets;
    private TextView vTodayProfitLoss;
    private TextView vTodayProfitLossB;
    private TextView vMarketValue;
    private TextView vAvailableFunds;
    private TextView vTotalProfitLoss;
    private TextView vTips;
    private int upColor;
    private int downColor;

    public SimulationTradingFundAccountView(Context context) {
        this(context, null);
    }

    public SimulationTradingFundAccountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimulationTradingFundAccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LocalSettingsConfig config = LocalSettingsConfig.Companion.getInstance();
        upColor = config.getUpColor();
        downColor = config.getDownColor();
        initView();

    }

    private void initView() {
        inflate(getContext(), R.layout.layout_simulation_trading_fund_account, this);
        vRootView = findViewById(R.id.root_view);
        vBusiness = findViewById(R.id.business);
        vTotalAssets = findViewById(R.id.total_assets);
        vTodayProfitLoss = findViewById(R.id.today_profit_and_loss);
        vTodayProfitLossB = findViewById(R.id.today_profit_and_loss_b);
        vMarketValue = findViewById(R.id.market_value);
        vAvailableFunds = findViewById(R.id.available_funds);
        vTotalProfitLoss = findViewById(R.id.total_profit_and_loss);
        vOrder = findViewById(R.id.order);
        vTips = findViewById(R.id.tips);
        vBusiness.setOnClickListener(this);
        vOrder.setOnClickListener(this);
    }

    public void setOnMockStockFundAccountListener(OnMockStockFundAccountListener l) {
        mListener = l;
    }

    @Override
    public void onClick(View v) {
        if (v == vBusiness) {
            mListener.toBusiness();
        } else if (v == vOrder) {
            mListener.toOrder();
        }
    }

    public void setData(STFundAccountData data) {
        vTips.setVisibility(GONE);
        if (!data.isCreate()) {
            inflateNotAccount();
            return;
        }
        vTotalAssets.setText(String.valueOf(data.getTotalAssets()));
        vMarketValue.setText(String.valueOf(data.getMarketValue()));
        vAvailableFunds.setText(String.valueOf(data.getAvailableFund()));
        vTotalProfitLoss.setText(String.valueOf(data.getTotalProfitAndLoss()));
        float todayPL = data.getTodayProfitAndLoss();
        vTodayProfitLoss.setText(String.valueOf(todayPL));
        int color;
        String percentageTxt;
        if (todayPL > 0) {
            color = upColor;
            percentageTxt = getPercentageText(data.getTodayProfitAndLossPercentage().multiply(new BigDecimal(100)).floatValue());
        } else if (todayPL < 0) {
            color = downColor;
            percentageTxt = getPercentageText(data.getTodayProfitAndLossPercentage().multiply(new BigDecimal(100)).floatValue());
        } else {
            color = Color.parseColor("#282828");
            percentageTxt = "--";
        }
        vTodayProfitLossB.setText(percentageTxt);
        vTodayProfitLossB.setTextColor(color);
        vTodayProfitLoss.setTextColor(color);

    }

    private String getPercentageText(float percentage) {
        String s = String.valueOf(Math.abs(percentage));
        if (s.startsWith("0.00")) {
            return String.format("%1s%2s%%", percentage > 0 ? "+" : "-", MathUtil.INSTANCE.subZeroAndDot(s));
        } else {
            return String.format("%+.2f%%", percentage);
        }
    }

    public void createFundAccountSuccess() {
        if (vNotAcc != null) {
            vRootView.removeView(vNotAcc);
            vNotAcc = null;
        }
    }



    public interface OnMockStockFundAccountListener {

        /**
         * 去买卖
         */
        void toBusiness();

        /**
         * 去订单
         */
        void toOrder();

        /**
         * 去创建资金账号
         */
        void toCreateFundAccount();

    }

    private void inflateNotAccount() {
        ViewStub vs = findViewById(R.id.vs_not_account);
        if (vs != null) {
            vNotAcc = vs.inflate();
            View btn = vNotAcc.findViewById(R.id.tv_lmsna_btn);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.toCreateFundAccount();
                }
            });
            String blue = ResUtil.INSTANCE.getString(R.string.create_account_desc_bule);
            String txt = ResUtil.INSTANCE.getString(R.string.create_account_desc);
            int color = getResources().getColor(R.color.color_1A6ED2);
            int s = txt.indexOf(blue);
            SpannableString ss = new SpannableString(txt);
            ss.setSpan(new ForegroundColorSpan(color), s, s + blue.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            TextView desc = vNotAcc.findViewById(R.id.tv_lmsna_text);
            desc.setText(ss);
        }
    }


}
