package com.zhuorui.securities.market.customer.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.socket.vo.OrderData;
import com.zhuorui.securities.market.util.MarketUtil;
import com.zhuorui.securities.market.util.MathUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-18 17:45
 * desc   : 买卖十档
 */
public class BuyingSellingFilesView extends FrameLayout {

    private RecyclerView vRv;
    private ProgressBar vPB;
    private TextView vBuyingValue;
    private TextView vSellingValue;
    private TextView vBuyingTitle;
    private TextView vSellingTitle;
    private BuySellFileAdapter mAdapter;
    private final int defColor;
    private final LocalSettingsConfig config;
    private float preClosePrice = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mTextSize = 0;

    public BuyingSellingFilesView(Context context) {
        this(context, null);
    }

    public BuyingSellingFilesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuyingSellingFilesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defColor = Color.parseColor("#C0CCE0");
        config = LocalSettingsConfig.Companion.getInstance();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BuyingSellingFilesView);
        mTextSize = a.getDimensionPixelOffset(R.styleable.BuyingSellingFilesView_zr_textSize, mPaddingLeft);
        mPaddingLeft = a.getDimensionPixelOffset(R.styleable.BuyingSellingFilesView_zr_paddingLeft, mPaddingLeft);
        mPaddingRight = a.getDimensionPixelOffset(R.styleable.BuyingSellingFilesView_zr_paddingRight, mPaddingRight);
        int type = a.getInt(R.styleable.BuyingSellingFilesView_zr_LayoutManager, 0);
        a.recycle();
        addLayout(type);
        setPadding(mPaddingLeft, mPaddingRight);
    }

    private void addLayout(int type) {
        if (type == 1) {
            inflate(getContext(), R.layout.view_buying_selling_files_grid, this);
            initView();
            vRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
            vRv.setAdapter(mAdapter = new GridAdapter(getContext()));
            mAdapter.notifyDataSetChanged();
        } else {
            inflate(getContext(), R.layout.view_buying_selling_files_linear, this);
            initView();
            vRv.setLayoutManager(new LinearLayoutManager(getContext()));
            vRv.setAdapter(mAdapter = new LinearAdapter(getContext()));
            post(() -> {
                int itemHight = mAdapter.calculationItemHight(getHeight());
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) vPB.getLayoutParams();
                lp.height = itemHight;
                vPB.requestLayout();
                mAdapter.notifyDataSetChanged();
            });
        }
    }

    private void initView() {
        vPB = findViewById(R.id.progress_bar);
        vBuyingTitle = findViewById(R.id.tv_title1);
        vSellingTitle = findViewById(R.id.tv_title2);
        vBuyingValue = findViewById(R.id.tv_progress1_value);
        vSellingValue = findViewById(R.id.tv_progress2_value);
        vRv = findViewById(R.id.recycler_view);
        vRv.setNestedScrollingEnabled(false);
        vRv.setHasFixedSize(true);
        vRv.setFocusable(false);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        vRv.setItemAnimator(itemAnimator);
        if (mTextSize > 0 ){
            vBuyingValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            vSellingValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }
    }

    public void setPadding(int left, int right) {
        mPaddingLeft = left;
        mPaddingRight = right;
        vBuyingValue.setPadding(mPaddingLeft, 0, 0, 0);
        vSellingValue.setPadding(0, 0, mPaddingRight, 0);
        if (vBuyingTitle != null) vBuyingTitle.setPadding(mPaddingLeft, 0, mPaddingRight, 0);
        if (vSellingTitle != null) vSellingTitle.setPadding(mPaddingLeft, 0, mPaddingRight, 0);
    }

    public void setTextSizeSp(float textSize) {
        mTextSize = ResUtil.INSTANCE.getDimensionDp2Px(textSize);
        vBuyingValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        vSellingValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置昨收价
     *
     * @param preClosePrice
     */
    public void setPreClosePrice(float preClosePrice) {
        this.preClosePrice = preClosePrice;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置买卖盘数据
     */
    public void setData(List<OrderData.AskBidModel> askData, List<OrderData.AskBidModel> bidData) {
        float buyingValue = askData != null && !askData.isEmpty() ? Float.valueOf(askData.get(0).getQty()) : 0;
        float sellingValue = bidData != null && !bidData.isEmpty() ? Float.valueOf(bidData.get(0).getQty()) : 0;
        float total = buyingValue + sellingValue;
        float buyingB = buyingValue / total;
        float sellingB = 1 - buyingB;
        vPB.setProgress((int) (buyingB * 10000));
        vBuyingValue.setText(String.format(ResUtil.INSTANCE.getString(R.string.buying_percentage), buyingB * 100));
        vSellingValue.setText(String.format(ResUtil.INSTANCE.getString(R.string.selling_percentage), sellingB * 100));
        mAdapter.setData(askData, bidData);
    }

    class GridAdapter extends BuySellFileAdapter {

        private final int bay1color;
        private final int sell1color;
        private final int bayColor;
        private final int sellColor;
        private final String[] mTitles;

        public GridAdapter(Context context) {
            super(context);
            mTitles = context.getResources().getStringArray(R.array.buy_sell_file_grid);
            bay1color = Color.parseColor("#17355E");
            sell1color = Color.parseColor("#5D3A1C");
            bayColor = Color.parseColor("#0D1A6ED2");
            sellColor = Color.parseColor("#0DFF8E1B");
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_buying_selling_files, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            int backgroundColor;
            if (position == 0) {
                backgroundColor = bay1color;
            } else if (position == 1) {
                backgroundColor = sell1color;
            } else if (position % 2 == 0) {
                backgroundColor = bayColor;
            } else {
                backgroundColor = sellColor;
            }
            holder.bindData(position, mTitles[position], getData(position), backgroundColor);
            switch (getUpDownAnimType(position)) {
                case 1:
                    holder.showUpDownAnim(true);
                    break;
                case -1:
                    holder.showUpDownAnim(false);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }


        @Override
        public void setData(List<OrderData.AskBidModel> buyingData, List<OrderData.AskBidModel> sellingData) {
            List<OrderData.AskBidModel> datas = new ArrayList<>();
            for (int i = 0, size = getItemCount() / 2; i < size; i++) {
                datas.add(buyingData != null && i < buyingData.size() ? buyingData.get(i) : null);
                datas.add(sellingData != null && i < sellingData.size() ? sellingData.get(i) : null);
            }
            setData(datas);
        }

    }


    class LinearAdapter extends BuySellFileAdapter {

        private final String[] mTitles;
        private int mItemHight;

        public LinearAdapter(Context context) {
            super(context);
            mTitles = context.getResources().getStringArray(R.array.buy_sell_file_linear);
        }

        @Override
        public int calculationItemHight(int totalHight) {
            mItemHight = totalHight / mTitles.length;
            mItemHight = (int) Math.max(mItemHight, context.getResources().getDisplayMetrics().density * 25);
            return mItemHight;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_buying_selling_files, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp == null) {
                lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, mItemHight);
            } else {
                lp.height = mItemHight;
            }
            holder.itemView.setLayoutParams(lp);
            holder.bindData(position, mTitles[position], getData(position), 0);
            switch (getUpDownAnimType(position)) {
                case 1:
                    holder.showUpDownAnim(true);
                    break;
                case -1:
                    holder.showUpDownAnim(false);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }

        @Override
        public void setData(List<OrderData.AskBidModel> buyingData, List<OrderData.AskBidModel> sellingData) {
            int size = (mTitles.length - 1) / 2;
            List<OrderData.AskBidModel> datas = new ArrayList<>();
            for (int i = size - 1; i >= 0; i--) {
                datas.add(buyingData != null && i < buyingData.size() ? buyingData.get(i) : null);
            }
            datas.add(null);
            for (int i = 0; i < size; i++) {
                datas.add(sellingData != null && i < sellingData.size() ? sellingData.get(i) : null);
            }
            setData(datas);
        }
    }

    private abstract class BuySellFileAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private final List<OrderData.AskBidModel> mDatas;
        protected Context context;
        private Map<Integer, Boolean> upDownAnim = new HashMap<>();

        public BuySellFileAdapter(Context context) {
            this.context = context;
            mDatas = new ArrayList<>();
        }

        public abstract void setData(List<OrderData.AskBidModel> buyingData, List<OrderData.AskBidModel> sellingData);

        protected void setData(List<OrderData.AskBidModel> datas) {
            upDownAnim.clear();
            if (mDatas.size() != datas.size()) {
                mDatas.clear();
                mDatas.addAll(datas);
                notifyDataSetChanged();
            } else {
                for (int i = 0; i < datas.size(); i++) {
                    replaceData(mDatas.get(i), datas.get(i), i);
                }
            }
        }

        protected int getUpDownAnimType(int position) {
            if (upDownAnim.containsKey(position)) {
                int type = upDownAnim.get(position) ? 1 : -1;
                upDownAnim.remove(position);
                return type;
            }
            return 0;
        }

        protected OrderData.AskBidModel getData(int position) {
            return position < mDatas.size() ? mDatas.get(position) : null;
        }

        protected int calculationItemHight(int totalHight) {
            return 0;
        }

        protected final void replaceData(OrderData.AskBidModel oldData, OrderData.AskBidModel newData, int position) {
            if (oldData != null && newData != null) {
                if (TextUtils.equals(oldData.getPrice(), newData.getPrice()) && !TextUtils.equals(oldData.getQty(), newData.getQty())) {
                    upDownAnim.put(position, Double.valueOf(newData.getQty()) > Double.valueOf(oldData.getQty()));
                    mDatas.remove(position);
                    mDatas.add(position, newData);
                    notifyItemChanged(position);
                } else if (!TextUtils.equals(oldData.getPrice(), newData.getPrice())) {
                    mDatas.remove(position);
                    mDatas.add(position, newData);
                    notifyItemChanged(position);
                } else if (!TextUtils.equals(oldData.getNum(), newData.getNum())) {
                    mDatas.remove(position);
                    mDatas.add(position, newData);
                    notifyItemChanged(position);
                }
            } else if (newData != null) {
                mDatas.remove(position);
                mDatas.add(position, newData);
                notifyItemChanged(position);
            }
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView vTitle;
        private TextView vPirce;
        private TextView vNum;
        private View vAnim;
        private ObjectAnimator animator;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.tv_title);
            vPirce = itemView.findViewById(R.id.tv_price);
            vNum = itemView.findViewById(R.id.tv_num);
            vAnim = itemView.findViewById(R.id.v_anim);
            if (mTextSize > 0) {
                vTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                vPirce.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
                vNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
        }

        public void bindData(int position, String title, OrderData.AskBidModel data, int backgroundColor) {
            vTitle.setText(title);
            vTitle.setPadding(mPaddingLeft, vTitle.getPaddingTop(), vTitle.getPaddingRight(), vTitle.getPaddingBottom());
            vNum.setPadding(vNum.getPaddingLeft(), vTitle.getPaddingTop(), mPaddingRight, vNum.getPaddingBottom());
            itemView.setBackgroundColor(backgroundColor);
            if (data == null) {
                vPirce.setTextColor(defColor);
                vPirce.setText("--");
                vNum.setText("--(--)");
            } else {
                float price = Float.valueOf(data.getPrice());
                vPirce.setTextColor(config.getUpDownColor(price, preClosePrice, defColor));
                vPirce.setText(String.format("%.3f", price));
                vNum.setText(String.format("%s(%s)", MathUtil.INSTANCE.convertToUnitString(new BigDecimal(data.getQty())), data.getNum()));
            }
        }

        public void showUpDownAnim(boolean isUp) {
            animator = MarketUtil.showUpDownAnim(animator, vAnim, isUp);
        }

    }

}
