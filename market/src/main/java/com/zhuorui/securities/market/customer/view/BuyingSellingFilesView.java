package com.zhuorui.securities.market.customer.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.socket.vo.OrderData;
import com.zhuorui.securities.market.util.MarketUtil;

import java.util.ArrayList;
import java.util.List;

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
        mPaddingLeft = a.getDimensionPixelOffset(R.styleable.BuyingSellingFilesView_zr_paddingLeft, mPaddingLeft);
        mPaddingRight = a.getDimensionPixelOffset(R.styleable.BuyingSellingFilesView_zr_paddingRight, mPaddingRight);
        int type = a.getInt(R.styleable.BuyingSellingFilesView_zr_LayoutManager, 0);
        a.recycle();
        if (type == 1) {
            inflate(context, R.layout.view_buying_selling_files_grid, this);
            initView();
            vRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
            vRv.setAdapter(mAdapter = new GridAdapter(getContext()));
            mAdapter.notifyDataSetChanged();
        } else {
            inflate(context, R.layout.view_buying_selling_files_linear, this);
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
        setPadding(mPaddingLeft, mPaddingRight);
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
    }

    public void setPadding(int left, int right) {
        mPaddingLeft = left;
        mPaddingRight = right;
        vBuyingValue.setPadding(mPaddingLeft, 0, 0, 0);
        vSellingValue.setPadding(0, 0, mPaddingRight, 0);
        if (vBuyingTitle != null) vBuyingTitle.setPadding(mPaddingLeft, 0, mPaddingRight, 0);
        if (vSellingTitle != null) vSellingTitle.setPadding(mPaddingLeft, 0, mPaddingRight, 0);
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
        float buyingValue = Float.valueOf(askData.get(0).getQty());
        float sellingValue = Float.valueOf(askData.get(0).getQty());
        float total = buyingValue + sellingValue;
        float buyingB = buyingValue / total;
        float sellingB = 1 - buyingB;
        vPB.setProgress((int) (buyingB * 10000));
        vBuyingValue.setText(String.format(ResUtil.INSTANCE.getString(R.string.buying_percentage), buyingB * 100));
        vSellingValue.setText(String.format(ResUtil.INSTANCE.getString(R.string.selling_percentage), sellingB * 100));
        mAdapter.setData(askData, bidData);
        mAdapter.notifyDataSetChanged();
    }

    class GridAdapter extends BuySellFileAdapter {

        private Context context;
        private final List<OrderData.AskBidModel> mDatas;
        private final int bay1color;
        private final int sell1color;
        private final int bayColor;
        private final int sellColor;
        private final String[] mTitles;

        public GridAdapter(Context context) {
            this.context = context;
            mTitles = context.getResources().getStringArray(R.array.buy_sell_file_grid);
            mDatas = new ArrayList<>();
            for (int i = 0; i < mTitles.length; i++) {
                mDatas.add(null);
            }
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
            holder.bindData(position, mTitles[position], mDatas.get(position), backgroundColor);
        }

        @Override
        public int getItemCount() {
            return mTitles.length;
        }


        @Override
        public void setData(List<OrderData.AskBidModel> buyingData, List<OrderData.AskBidModel> sellingData) {
            List<OrderData.AskBidModel> datas = new ArrayList<>();
            for (int i = 0, size = getItemCount() / 2; i < size; i++) {
                datas.add(i < buyingData.size() ? buyingData.get(i) : null);
                datas.add(i < sellingData.size() ? sellingData.get(i) : null);
            }
            mDatas.clear();
            mDatas.addAll(datas);
        }

    }

    class LinearAdapter extends BuySellFileAdapter {

        private Context context;
        private final List<OrderData.AskBidModel> mDatas;
        private final String[] mTitles;
        private int mItemHight;

        public LinearAdapter(Context context) {
            this.context = context;
            mTitles = context.getResources().getStringArray(R.array.buy_sell_file_linear);
            mDatas = new ArrayList<>();
            for (int i = 0; i < mTitles.length; i++) {
                mDatas.add(null);
            }
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
            holder.bindData(position, mTitles[position], mDatas.get(position), 0);
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
                datas.add(i < buyingData.size() ? buyingData.get(i) : null);
            }
            datas.add(null);
            for (int i = 0; i < size; i++) {
                datas.add(i < sellingData.size() ? sellingData.get(i) : null);
            }
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    private abstract class BuySellFileAdapter extends RecyclerView.Adapter<MyViewHolder> {

        public abstract void setData(List<OrderData.AskBidModel> buyingData, List<OrderData.AskBidModel> sellingData);

        public int calculationItemHight(int totalHight) {
            return 0;
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
            itemView.setOnClickListener(v -> animator = MarketUtil.showUpDownAnim(animator, vAnim, true));
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
                float qty = Float.valueOf(data.getQty());
                vNum.setText(String.format("%.1fK(%S)", qty, data.getNum()));
            }
        }


    }

}
