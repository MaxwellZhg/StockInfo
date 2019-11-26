package com.zhuorui.securities.market.customer.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.commonwidget.ZRDividerItemDecoration;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.model.PushIndexHandicapData;
import com.zhuorui.securities.market.socket.vo.IndexPonitHandicapData;
import com.zhuorui.securities.market.socket.vo.StockHandicapData;
import com.zhuorui.securities.market.util.MarketUtil;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/10/23
 * Desc:
 */
public class MarketPointDetailView extends FrameLayout {
    /*数据对应位置*/
    private final int ITEMPOS_HIGH_PRICE = 0;//最高价
    private final int ITEMPOS_OPEN_PRICE = 1;//今开
    private final int ITEMPOS_SHARESTRADED = 2;//成交额
    private final int ITEMPOS_LOW_PRICE = 3;//最低价
    private final int ITEMPOS_PRE_CLOSE_PRICE = 4;//昨收
    private final int ITEMPOS_TURNOVER = 5;//振幅
    private final int ITEMPOS_TURNOVER_RATE = 6;//涨家
    private final int ITEMPOS_PE_RATIO_STATIC = 7;//平家
    private final int ITEMPOS_TOTAL_MARK_VALUE = 8;//跌家

    private int closeNum;
    private String[] mItemTitles;
    private String[] mItemDatas;
    private RecyclerView vRv;
    private MyAdapter mAdapter;
    private final int CLOSE_LINE = 3;
    private final int SPAN_COUNT = 3;
    private int upColor;
    private int downColor;
    private ArrayMap<Integer, Integer> mItemColor;//特殊条目颜色

    private TextView vPrice;//股票价格
    private TextView vDiffPrice;//股票涨跌价
    private TextView vDiffRate;//股票涨跌幅
    private ImageView vDiffLogo;//涨跌logo
    private TextView vCurrencyCode;//货币代码
    private View vAnimator;

    private Float mPrice;
    private Float mPreClosePrice;
    private Float mOpenPrice;
    private Float mHighPricel;
    private Float mLowPricel;
    public MarketPointDetailView(Context context) {
        this(context,null);
    }

    public MarketPointDetailView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarketPointDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        closeNum = CLOSE_LINE * SPAN_COUNT;
        mItemColor = new ArrayMap<>();
        mItemTitles = context.getResources().getStringArray(R.array.stock_point_detail_title);
        mItemDatas = new String[mItemTitles.length];
        inflate(context, R.layout.layout_market_point_detail_view,this);
        initView();
    }

    private void initView() {
        vPrice = findViewById(R.id.tv_price);
        vDiffPrice = findViewById(R.id.tv_diff_price);
        vDiffRate = findViewById(R.id.tv_diff_rate);
        vDiffLogo = findViewById(R.id.iv_diff_logo);
        vCurrencyCode = findViewById(R.id.tv_currency_code);
        vAnimator = findViewById(R.id.v_price_cahnge);
        vRv = findViewById(R.id.recycler_view);
        vRv.setNestedScrollingEnabled(false);
        vRv.setHasFixedSize(true);
        vRv.setFocusable(false);
        vRv.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        ZRDividerItemDecoration decoration = new ZRDividerItemDecoration(getContext());
        decoration.setDrawable(getResources().getDrawable(R.drawable.stock_detail_item_divider));
        vRv.addItemDecoration(decoration);
        vRv.setAdapter(mAdapter = new MyAdapter(getContext()));
        vDiffPrice.setOnClickListener(v -> priceAnimator(true));
        vDiffRate.setOnClickListener(v -> priceAnimator(false));
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
            View v = LayoutInflater.from(mCtx).inflate(R.layout.item_stock_detail, parent, false);
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


    public void setData(IndexPonitHandicapData data) {
        setPrice(data);
        readData(data, mPreClosePrice,false);
        mAdapter.notifyDataSetChanged();
    }

    public void upData(IndexPonitHandicapData    data) {
        setPrice(data);
        readData(data, mPreClosePrice, true);
        mAdapter.notifyDataSetChanged();
    }

    private void setPrice(IndexPonitHandicapData data) {
        if (upColor == 0) {
            LocalSettingsConfig config = LocalSettingsConfig.Companion.getInstance();
            upColor = config.getUpColor();
            downColor = config.getDownColor();
        }
        final Float price = data.getLast()!= null ? data.getLast().floatValue() : mPrice;
        final Float preClosePrice = data.getLast() != null ? data.getOpen().floatValue() : mPreClosePrice;
        int priceColor = Color.WHITE;
        int updownIc = 0;
        if (price == null || preClosePrice == null) {
            vPrice.setText("_____");
            vDiffPrice.setText("___");
            vDiffRate.setText("___");
            vCurrencyCode.setText("");
        } else {
            if (price > preClosePrice) {
                priceColor = upColor;
                updownIc = MarketUtil.getUpIcon();
            } else if (price < preClosePrice) {
                priceColor = downColor;
                updownIc = MarketUtil.getDownIcon();
            }
            vPrice.setText(String.format("%.3f", price));
            float diffPrice = price - preClosePrice;
            vDiffPrice.setText(String.format("%+.3f", diffPrice));
            vDiffRate.setText(String.format("%+.2f%%", diffPrice * 100 / preClosePrice));
            if (mPrice != null && mPrice.floatValue() != price.floatValue()) {
                priceAnimator = MarketUtil.showUpDownAnim(priceAnimator, vAnimator, price.floatValue() > mPrice.floatValue());
            }
            vCurrencyCode.setText(MarketUtil.getCurrencyCodeByTs("HK"));
            mPreClosePrice = preClosePrice;
            mPrice = price;
        }
        vPrice.setTextColor(priceColor);
        vDiffPrice.setTextColor(priceColor);
        vDiffRate.setTextColor(priceColor);
        vDiffLogo.setImageResource(updownIc);
    }

    private void readData(IndexPonitHandicapData data, Float preClosePrice,boolean isUpdata) {
        mItemColor.clear();
        //最高
        Float highPrice = data.getHigh() != null ? data.getHigh().floatValue() : mHighPricel;
        if (highPrice != null) {
            mHighPricel = highPrice;
            mItemDatas[ITEMPOS_HIGH_PRICE] = String.format("%.3f", data.getHigh().floatValue());
        }
        if (highPrice != null && preClosePrice != null) {
            mItemColor.put(ITEMPOS_HIGH_PRICE, getUpDownColor(highPrice, preClosePrice, Color.WHITE));
        }
        //今开
        Float openPrice = data.getOpen() != null ? data.getHigh().floatValue() : mOpenPrice;
        if (openPrice != null) {
            mOpenPrice = openPrice;
            mItemDatas[ITEMPOS_OPEN_PRICE] = String.format("%.3f", openPrice);
        }
        if (openPrice != null && preClosePrice != null) {
            mItemColor.put(ITEMPOS_OPEN_PRICE, getUpDownColor(openPrice, preClosePrice, Color.WHITE));
        }
        //成交额
        mItemDatas[ITEMPOS_SHARESTRADED] = data == null || data.getSharestraded()== null ? getDefText(isUpdata, ITEMPOS_TURNOVER) : data.getSharestraded().toString();
        //最低
        Float lowPrice = data.getLow() != null ? data.getLow().floatValue(): mLowPricel;
        if (lowPrice != null) {
            mLowPricel = lowPrice;
            mItemDatas[ITEMPOS_LOW_PRICE] = String.format("%.3f", lowPrice);
        }
        if (lowPrice != null && preClosePrice != null) {
            mItemColor.put(ITEMPOS_LOW_PRICE, getUpDownColor(lowPrice, preClosePrice, Color.WHITE));
        }
        //昨收
        if (preClosePrice != null) {
            mItemDatas[ITEMPOS_PRE_CLOSE_PRICE] = String.format("%5.3f", preClosePrice);
        }
     /*   //振幅
        mItemDatas[ITEMPOS_TURNOVER] = data == null || data.getAmplitude() == null ? getDefText(isUpdata, ITEMPOS_TURNOVER) : data.getAmplitude();
        //涨家
        mItemDatas[ITEMPOS_TURNOVER_RATE] = data == null || data.getAmplitude() == null ? getDefText(isUpdata, ITEMPOS_TURNOVER_RATE) : data.getAmplitude();
        //平家
        mItemDatas[ITEMPOS_PE_RATIO_STATIC] = data == null || data.getAmplitude() == null ? getDefText(isUpdata, ITEMPOS_PE_RATIO_STATIC) : data.getAmplitude();
        //跌家
        mItemDatas[ITEMPOS_TOTAL_MARK_VALUE] = data == null || data.getAmplitude() == null ? getDefText(isUpdata, ITEMPOS_TOTAL_MARK_VALUE) : data.getAmplitude();*/

    }

    private int getUpDownColor(float price1, float price2, int defColor) {
        if (price1 > price2) {
            return upColor;
        } else if (price1 < price2) {
            return downColor;
        } else {
            return defColor;
        }
    }

    private ObjectAnimator priceAnimator;
    private int upAnimatorColor = Color.parseColor("#26D9001B");
    private int downAnimatorColor = Color.parseColor("#2600CC00");

    /**
     * 涨跌动画
     *
     * @param isUp
     */
    private void priceAnimator(boolean isUp) {
        if (priceAnimator != null && priceAnimator.isRunning()) {
            priceAnimator.cancel();
            priceAnimator = null;
        }
        vAnimator.setBackgroundColor(isUp ? upAnimatorColor : downAnimatorColor);
        priceAnimator = new ObjectAnimator().ofFloat(vAnimator, "translationY", 0, vAnimator.getHeight() * (isUp ? -1 : 1));
        priceAnimator.setInterpolator(new AccelerateInterpolator());
        priceAnimator.setDuration(300);
        priceAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                vAnimator.setBackground(null);
                priceAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        priceAnimator.start();

    }

    private String getDefText(boolean isUpdata, int position) {
        return isUpdata ? mItemDatas[position] : "--";
    }


}
