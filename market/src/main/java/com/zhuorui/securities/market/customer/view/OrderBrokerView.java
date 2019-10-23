package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.securities.market.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-18 17:45
 * desc   : 买卖盘经济
 */
public class OrderBrokerView extends FrameLayout implements View.OnClickListener {
    int color1 = Color.parseColor("#122c51");
    int color2 = Color.parseColor("#603b16");
    int color3 = Color.parseColor("#211F2A");
    private int[] colors;
    private RecyclerView vRv;
    private MyAdapter mAdapter;
    private TextView vDateNum;
    private int itemType;
    private int mNum = 5;

    public OrderBrokerView(Context context) {
        this(context, null);
    }

    public OrderBrokerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderBrokerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_order_broker, this);
        initView();
        setNum();
    }

    private void initView() {
        vDateNum = findViewById(R.id.tv_date_num);
        vDateNum.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        changeNum();
    }

    private void changeNum() {
        if (mNum == 5) {
            mNum = 10;
        } else if (mNum == 10) {
            mNum = 40;
        } else if (mNum == 40) {
            mNum = -1;
        } else if (mNum == -1) {
            mNum = 5;
        }
        setNum();
    }

    private void setNum() {
        GridLayoutManager lm = (GridLayoutManager) vRv.getLayoutManager();
        if (mNum == -1) {
            itemType = 1;
            lm.setSpanCount(8);
            colors = new int[]{color1, color3, color1, color3, color2, color3, color2, color3};
        } else {
            lm.setSpanCount(2);
            colors = new int[]{color1, color2};
            itemType = 0;
        }
        vDateNum.setText(String.valueOf(mNum));
        mAdapter.setNum(mNum);
        mAdapter.notifyDataSetChanged();
    }

    public void setData(List<? extends Object> buyings, List<? extends Object> sellings) {
        mAdapter.setData(buyings, sellings);
        mAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private Context context;
        private int mNum = 0;
        private int itemCount = 0;
        private List<Object> mBuyingDatas;
        private List<Object> mSellingDatas;
        private List<Object> mDatas;

        public MyAdapter(Context context) {
            this.context = context;
            mBuyingDatas = new ArrayList<>();
            mSellingDatas = new ArrayList<>();
            mDatas = new ArrayList<>();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(itemType == 0 ? R.layout.item_order_broker : R.layout.item_order_broker_code, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Object object = mDatas.get(position);
            holder.bindData(position,object);
        }

        @Override
        public int getItemViewType(int position) {
            return itemType;
        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

        public void setNum(int mNum) {
            this.mNum = mNum;
            initData();
        }

        public void setData(List<? extends Object> buyings, List<? extends Object> sellings) {
            mBuyingDatas.clear();
            mSellingDatas.clear();
            mBuyingDatas.addAll(buyings);
            mSellingDatas.addAll(sellings);
            initData();
        }

        private void initData() {
            mDatas.clear();
            int relSize = Math.max(mBuyingDatas.size(), mSellingDatas.size());
            int lineSize;
            int spanCount;//一行单边数据条数
            if (mNum == -1) {
                spanCount = 4;
                lineSize = (relSize / spanCount) + (relSize % spanCount == 0 ? 0 : 1);
            } else {
                spanCount = 1;
                lineSize = Math.min(relSize, mNum);
            }
            itemCount = lineSize * spanCount * 2;
            for (int i = 0; i < lineSize; i++) {
                for (int j = 0; j < spanCount; j++) {
                    int pos = i * spanCount + j;
                    if (pos < mBuyingDatas.size()) {
                        mDatas.add(mBuyingDatas.get(pos));
                    } else {
                        mDatas.add(String.valueOf(pos));
                    }
                }
                for (int j = 0; j < spanCount; j++) {
                    int pos = i * spanCount + j;
                    if (pos < mSellingDatas.size()) {
                        mDatas.add(mSellingDatas.get(pos));
                    } else {
                        mDatas.add(String.valueOf(pos));
                    }
                }
            }

        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView vCode;
        private TextView vName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vCode = itemView.findViewById(R.id.tv_code);
            vName = itemView.findViewById(R.id.tv_name);
        }

        public void bindData(int position, Object object) {
            vCode.setText(String.valueOf(80900 + position));
            if (vName != null)vName.setText(object.toString());
            itemView.setBackgroundColor(colors[position % colors.length]);
        }
    }
}
