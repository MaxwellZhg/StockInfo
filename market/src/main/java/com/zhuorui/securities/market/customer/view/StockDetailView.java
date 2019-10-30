package com.zhuorui.securities.market.customer.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.commonwidget.ZRDividerItemDecoration;
import com.zhuorui.securities.market.R;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-11 18:18
 * desc   : 股票详情价格详细数据View
 */
public class StockDetailView extends FrameLayout {


    /*数据对应位置*/
    private final int ITEMPOS_HIGH_PRICE = 0;//最高价
    private final int ITEMPOS_OPEN_PRICE = 1;//今开
    private final int ITEMPOS_SHARESTRADED = 2;//成交量
    private final int ITEMPOS_LOW_PRICE = 3;//最低价
    private final int ITEMPOS_PRE_CLOSE_PRICE = 4;//昨收
    private final int ITEMPOS_TURNOVER = 5;//成交额
    private final int ITEMPOS_TURNOVER_RATE = 6;//换手率
    private final int ITEMPOS_PE_RATIO_STATIC = 7;//市盈率(静)
    private final int ITEMPOS_TOTAL_MARK_VALUE = 8;//总市值
    private final int ITEMPOS_AMPLITUDE = 9;//振幅
    private final int ITEMPOS_PERATIO_TTM = 10;//市盈率(TTM)
    private final int ITEMPOS_TOTAL_CAPITAL_STOCK = 11;//总股本
    private final int ITEMPOS_FIFTY_TWO_WEEKS_HIGH = 12;//52周最高
    private final int ITEMPOS_MARKET_RATE = 13;//市净率
    private final int ITEMPOS_CIRCULATION_VALUE = 14;//流通值
    private final int ITEMPOS_FIFTY_TWO_WEEKS_LOW = 15;//52周最低
    private final int ITEMPOS_COMPARISON = 16;//委比
    private final int ITEMPOS_CIRCULATING_SHARES = 17;//流通股
    private final int ITEMPOS_HANDS = 18;//每手
    private final int ITEMPOS_VOLUME_RATIO = 19;//量比
    private final int ITEMPOS_DIVIDEND_RATE = 20;//股息率

    private TextView vPrice;//股票价格
    private TextView vDiffPrice;//股票涨跌价
    private TextView vDiffRate;//股票涨跌幅
    private TextView vMoreBtn;
    private final int CLOSE_LINE = 3;
    private final int SPAN_COUNT = 3;
    private int closeNum;
    private RecyclerView vRv;
    private String[] mItemTitles;
    private String[] mItemDatas;
    private ArrayMap<Integer, Integer> mItemColor;//特殊条目颜色
    private MyAdapter mAdapter;


    public StockDetailView(Context context) {
        this(context, null);
    }

    public StockDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        closeNum = CLOSE_LINE * SPAN_COUNT;
        mItemColor = new ArrayMap<>();
        mItemTitles = context.getResources().getStringArray(R.array.stock_detail_title);
        mItemDatas = new String[mItemTitles.length];
        inflate(context, R.layout.view_stock_detail, this);
        initView();
    }

    private void initView() {
        vMoreBtn = findViewById(R.id.more_btn);
        vMoreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMore();
            }
        });
        vRv = findViewById(R.id.recycler_view);
        vRv.setNestedScrollingEnabled(false);
        vRv.setHasFixedSize(true);
        vRv.setFocusable(false);
        vRv.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        ZRDividerItemDecoration decoration = new ZRDividerItemDecoration(getContext());
        decoration.setDrawable(getResources().getDrawable(R.drawable.stock_detail_item_divider));
        vRv.addItemDecoration(decoration);
        vRv.setAdapter(mAdapter = new MyAdapter(getContext()));
    }

    private void changeMore() {
        if (mAdapter.getItemCount() != closeNum) {
            mAdapter.setItemCount(closeNum);
            vMoreBtn.setText("查看更多");
            vMoreBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_arrow_down_53a0fd, 0, 0, 0);
        } else {
            mAdapter.setItemCount(mItemTitles.length);
            vMoreBtn.setText("收起");
            vMoreBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_arrow_up_53a0fd, 0, 0, 0);
        }
    }

    public void setData() {
        mItemColor.clear();

    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        private int mItemCount = 9;
        private Context mCtx;

        public MyAdapter(Context context) {
            mCtx = context;
        }

        public void setItemCount(int count) {
            mItemCount = count;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mCtx).inflate(R.layout.item_stock_detail, parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Integer color = mItemColor.get(position);
            holder.bindData(mItemTitles[position], mItemDatas[position], color == null ? Color.WHITE : color);
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView vTitle;
        private TextView vText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.tv_title);
            vText = itemView.findViewById(R.id.tv_text);
        }

        public void bindData(String title, String text, int color) {
            vTitle.setText(title);
            vText.setTextColor(color);
            vText.setText(TextUtils.isEmpty(text) ? "--" : text);
        }
    }


}
