package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-09-09 14:38
 * desc   :
 */
public class MockStockFundAccountView extends FrameLayout implements View.OnClickListener {

    private OnMockStockFundAccountListener mListener;
    private View vBusiness;
    private View vOrder;
    private View vNotAcc;
    private ConstraintLayout vRootView;

    public MockStockFundAccountView(Context context) {
        this(context, null);
    }

    public MockStockFundAccountView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MockStockFundAccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

    }

    private void initView() {
        inflate(getContext(), R.layout.layout_mock_stock_fund_account, this);
        vRootView = findViewById(R.id.root_view);
        vBusiness = findViewById(R.id.business);
        vOrder = findViewById(R.id.order);
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

    public void setData(IFundAccountData data) {
        if (!data.isCreate()) {
            inflateNotAccount();
            return;
        }


    }

    public void createFundAccountSuccess() {
        if (vNotAcc != null) {
            vRootView.removeView(vNotAcc);
            vNotAcc = null;
        }
    }

    public interface IFundAccountData {
        boolean isCreate();
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
