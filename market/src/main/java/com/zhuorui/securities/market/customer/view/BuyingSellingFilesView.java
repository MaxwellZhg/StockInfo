package com.zhuorui.securities.market.customer.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.securities.market.R;

import java.text.DecimalFormat;
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
    private MyAdapter mAdapter;
    // 是否是横屏展示
    private boolean isLand;

    public BuyingSellingFilesView(Context context) {
        this(context, null);
    }

    public BuyingSellingFilesView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuyingSellingFilesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_buying_selling_files, this);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BuyingSellingFilesView);
        isLand = typedArray.getBoolean(R.styleable.BuyingSellingFilesView_isLand, false);
        typedArray.recycle();
        initView();
    }

    private void initView() {
        vPB = findViewById(R.id.progress_bar);
        vBuyingValue = findViewById(R.id.tv_progress1_value);
        vSellingValue = findViewById(R.id.tv_progress2_value);
        vRv = findViewById(R.id.recycler_view);
        //解决数据加载不完的问题
        vRv.setNestedScrollingEnabled(false);
        vRv.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        vRv.setFocusable(false);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 2);
        vRv.setLayoutManager(lm);
        vRv.setAdapter(mAdapter = new MyAdapter(getContext()));
    }

    public void setData(float buyingValue, float sellingValue, List<? extends Object> buyingData, List<? extends Object> sellingData) {
        float total = buyingValue + sellingValue;
        float buyingB = buyingValue / total;
        float sellingB = 1 - buyingB;
        vPB.setProgress((int) (buyingB * 10000));
        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
        vBuyingValue.setText(decimalFormat.format(buyingB));
        vSellingValue.setText(decimalFormat.format(sellingB));
        List<Object> datas = new ArrayList<>();
        for (int i = 0, l = buyingData.size(); i < l; i++) {
            datas.add(buyingData.get(i));
            datas.add(sellingData.get(i));
        }
        mAdapter.setData(datas);
        mAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private Context context;
        private List<Object> mDatas;
        private String[] mTitles = {"买一", "卖一", "买二", "卖二", "买三", "卖三", "买四", "卖四", "买五", "卖五", "买六", "卖六", "买七", "卖七", "买八", "卖八", "买九", "卖九", "买十", "卖十"};

        public MyAdapter(Context context) {
            this.context = context;
            mDatas = new ArrayList<>();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_buying_selling_files, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.bindData(position, mTitles[position]);
        }

        @Override
        public int getItemCount() {
            int size = mDatas.size();
            return size > 20 ? 20 : size;
        }

        public void setData(List<Object> datas) {
            mDatas.clear();
            mDatas.addAll(datas);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final int color1;
        private final int color2;
        private final int color3;
        private final int color4;
        private TextView vTitle;
        private TextView vPirce;
        private TextView vNum;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            color1 = Color.parseColor("#17355E");
            color2 = Color.parseColor("#5D3A1C");
            color3 = Color.parseColor("#0D1A6ED2");
            color4 = Color.parseColor("#0DFF8E1B");
            vTitle = itemView.findViewById(R.id.tv_title);
            vPirce = itemView.findViewById(R.id.tv_price);
            vNum = itemView.findViewById(R.id.tv_num);
        }

        public void bindData(int position, String title) {
            vTitle.setText(title);
            vPirce.setText(String.format("%.3f", 90.253 + position));
            vNum.setText(String.format("%.1fK(%2d)", position + 1.2, position));
            if (position == 0) {
                itemView.setBackgroundColor(color1);
            } else if (position == 1) {
                itemView.setBackgroundColor(color2);
            } else {
                int i = position % 2;
                if (i == 0) {
                    itemView.setBackgroundColor(color3);
                } else {
                    itemView.setBackgroundColor(color4);
                }
            }
            itemView.setOnClickListener(v -> {
                ValueAnimator colorAnim = ObjectAnimator.ofInt(vNum, "backgroundColor", 0x00000000, 0x33D9001B, 0x00000000);
                colorAnim.setDuration(400);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.setRepeatMode(ValueAnimator.REVERSE);
                colorAnim.start();
            });
        }
    }

}
