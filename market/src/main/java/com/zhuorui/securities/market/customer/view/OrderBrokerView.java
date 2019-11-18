package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.customer.OrderBrokerNumPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-18 17:45
 * desc   : 买卖盘经济
 */
public class OrderBrokerView extends FrameLayout implements View.OnClickListener, OrderBrokerNumPopWindow.OnSelectCallBack {

    int color1 = Color.parseColor("#0D1A6ED2");
    int color2 = Color.parseColor("#0DFF8E1B");
    int color3 = Color.parseColor("#211F2A");

    private int[] colors;
    private RecyclerView vRv;
    private MyAdapter mAdapter;
    private TextView vDateNum;
    private ImageView ivDateNum;
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
        ivDateNum = findViewById(R.id.iv_date_num);
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
        if (vDateNum == v) {
            OrderBrokerNumPopWindow.Companion.create(getContext(), mNum, this).showAsDropDown(v);
        }
    }

    private void setNum() {
        GridLayoutManager lm = (GridLayoutManager) vRv.getLayoutManager();
        if (mNum == -1) {
            itemType = 1;
            lm.setSpanCount(8);
            colors = new int[]{color1, color3, color1, color3, color2, color3, color2, color3};
            ivDateNum.setVisibility(VISIBLE);
            vDateNum.setText("");
        } else {
            lm.setSpanCount(2);
            colors = new int[]{color1, color2};
            itemType = 0;
            ivDateNum.setVisibility(GONE);
            vDateNum.setText(String.valueOf(mNum));
        }
        mAdapter.setNum(mNum);
        mAdapter.notifyDataSetChanged();
    }

    public void setData(List<? extends Object> buyings, List<? extends Object> sellings) {
        mAdapter.setData(buyings, sellings);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelected(int num) {
        mNum = num;
        setNum();
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private Context context;
        private int mNum = 0;
        private int itemCount = 0;
        private int lineSize = 0;
//        private int mItemHeight = 0;
        private List<Object> mBuyingDatas;
        private List<Object> mSellingDatas;
        private List<Object> mDatas;

        public MyAdapter(Context context) {
            this.context = context;
            mBuyingDatas = new ArrayList<>();
            mSellingDatas = new ArrayList<>();
            mDatas = new ArrayList<>();
//            mItemHeight = (int) (context.getResources().getDisplayMetrics().density * 24);
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            if (itemType == 0) {
                v = LayoutInflater.from(context).inflate(R.layout.item_order_broker, parent, false);
            } else {
                v = LayoutInflater.from(context).inflate(R.layout.item_order_broker_code, parent, false);
                v.setOnClickListener(v1 -> {
                    int pos = (int) v1.getTag();
                    Toast toast = Toast.makeText(context, mDatas.get(pos).toString(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                });
            }
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Object object = mDatas.get(position);
            holder.bindData(position, object);
            holder.itemView.setTag(position);
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

//        public int getHeight() {
//            return lineSize * mItemHeight;
//        }
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
            vCode.setText(String.valueOf(new Random().nextInt(1000) + position));
            if (vName != null) vName.setText(object.toString());
            itemView.setBackgroundColor(colors[position % colors.length]);
        }
    }
}
