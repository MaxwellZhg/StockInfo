package com.zhuorui.securities.market.customer.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zhuorui.commonwidget.ZRDividerItemDecoration;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.market.socket.vo.StockHandicapData;
import com.zhuorui.securities.market.util.MarketUtil;
import com.zhuorui.securities.market.util.MathUtil;

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
    private ImageView vDiffLogo;//涨跌logo
    private TextView vCurrencyCode;//货币代码
    private TextView vMarket;//股票板块
    private TextView vMarketDiffPrice;//板块涨跌价
    private View vAnimator;
    private TextView vMoreBtn;
    private RecyclerView vRv;
    private TextView vStatis;
    private MyAdapter mAdapter;
    private final int CLOSE_LINE = 3;
    private final int SPAN_COUNT = 3;
    private int upColor;
    private int downColor;
    private int closeNum;
    private String[] mItemTitles;
    private String[] mItemDatas;
    private ArrayMap<Integer, Integer> mItemColor;//特殊条目颜色
    private Float mPrice;
    private Float mPreClosePrice;
    private Float mOpenPrice;
    private Float mHighPricel;
    private Float mLowPricel;
    private ObjectAnimator priceAnimator;

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
        vPrice = findViewById(R.id.tv_price);
        vDiffPrice = findViewById(R.id.tv_diff_price);
        vDiffRate = findViewById(R.id.tv_diff_rate);
        vDiffLogo = findViewById(R.id.iv_diff_logo);
        vCurrencyCode = findViewById(R.id.tv_currency_code);
        vAnimator = findViewById(R.id.v_price_cahnge);
        vMarket = findViewById(R.id.tv_market);
        vMarketDiffPrice = findViewById(R.id.tv_market_diff_price);
        vMoreBtn = findViewById(R.id.more_btn);
        vStatis = findViewById(R.id.tv_status);
        vMoreBtn.setOnClickListener(v -> changeMore());
        vRv = findViewById(R.id.recycler_view);
        vRv.setNestedScrollingEnabled(false);
        vRv.setHasFixedSize(true);
        vRv.setFocusable(false);
        vRv.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        ZRDividerItemDecoration decoration = new ZRDividerItemDecoration(getContext());
        decoration.setDrawable(getResources().getDrawable(R.drawable.stock_detail_item_divider));
        vRv.addItemDecoration(decoration);
        vRv.setAdapter(mAdapter = new MyAdapter(getContext()));
        vDiffPrice.setOnClickListener(v -> priceAnimator = MarketUtil.showUpDownAnim(priceAnimator, vAnimator, true));
        vDiffRate.setOnClickListener(v -> priceAnimator = MarketUtil.showUpDownAnim(priceAnimator, vAnimator, false));
    }

    private void changeMore() {
        if (mAdapter.getItemCount() != closeNum) {
            mAdapter.setItemCount(closeNum);
            vMoreBtn.setText(R.string.see_more);
            vMoreBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_arrow_down_53a0fd, 0, 0, 0);
        } else {
            mAdapter.setItemCount(mItemTitles.length);
            vMoreBtn.setText(R.string.str_retract);
            vMoreBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_arrow_up_53a0fd, 0, 0, 0);
        }
    }

    public void setData(StockHandicapData data) {
        setPrice(data);
        readData(data, mPreClosePrice, false);
        mAdapter.notifyDataSetChanged();
    }

    public void upData(StockHandicapData data) {
        setPrice(data);
        readData(data, mPreClosePrice, true);
        mAdapter.notifyDataSetChanged();
    }

    private void setPrice(StockHandicapData data) {
        if (upColor == 0) {
            LocalSettingsConfig config = LocalSettingsConfig.Companion.getInstance();
            upColor = config.getUpColor();
            downColor = config.getDownColor();
        }
        final Float price = data != null && data.getLast() != null ? data.getLast() : mPrice;
        final Float preClosePrice = data != null && data.getPreClose() != null ? data.getPreClose() : mPreClosePrice;
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

    private void readData(StockHandicapData data, Float preClosePrice, boolean isUpdata) {
        mItemColor.clear();
        //最高
        Float highPrice = data != null && data.getHigh() != null ? data.getHigh() : mHighPricel;
        if (highPrice != null) {
            mHighPricel = highPrice;
            mItemDatas[ITEMPOS_HIGH_PRICE] = String.format("%.3f", mHighPricel);
        } else {
            mItemDatas[ITEMPOS_HIGH_PRICE] = getDefText(isUpdata, ITEMPOS_HIGH_PRICE);
        }
        if (highPrice != null && preClosePrice != null) {
            mItemColor.put(ITEMPOS_HIGH_PRICE, getUpDownColor(highPrice, preClosePrice, Color.WHITE));
        }
        //今开
        Float openPrice = data != null && data.getOpen() != null ? data.getOpen() : mOpenPrice;
        if (openPrice != null) {
            mOpenPrice = openPrice;
            mItemDatas[ITEMPOS_OPEN_PRICE] = String.format("%.3f", openPrice);
        } else {
            mItemDatas[ITEMPOS_OPEN_PRICE] = getDefText(isUpdata, ITEMPOS_OPEN_PRICE);
        }
        if (openPrice != null && preClosePrice != null) {
            mItemColor.put(ITEMPOS_OPEN_PRICE, getUpDownColor(openPrice, preClosePrice, Color.WHITE));
        }
        //成交量
        mItemDatas[ITEMPOS_SHARESTRADED] = data == null || data.getSharestraded() == null
                ? getDefText(isUpdata, ITEMPOS_SHARESTRADED)
                : MathUtil.INSTANCE.convertToUnitString(data.getSharestraded(),2);
        //最低
        Float lowPrice = data != null && data.getLow() != null ? data.getLow() : mLowPricel;
        if (lowPrice != null) {
            mLowPricel = lowPrice;
            mItemDatas[ITEMPOS_LOW_PRICE] = String.format("%.3f", lowPrice);
        } else {
            mItemDatas[ITEMPOS_LOW_PRICE] = getDefText(isUpdata, ITEMPOS_LOW_PRICE);
        }
        if (lowPrice != null && preClosePrice != null) {
            mItemColor.put(ITEMPOS_LOW_PRICE, getUpDownColor(lowPrice, preClosePrice, Color.WHITE));
        }
        //昨收
        if (preClosePrice != null) {
            mItemDatas[ITEMPOS_PRE_CLOSE_PRICE] = String.format("%5.3f", preClosePrice);
        } else {
            mItemDatas[ITEMPOS_PRE_CLOSE_PRICE] = getDefText(isUpdata, ITEMPOS_PRE_CLOSE_PRICE);
        }
        //成交额
        mItemDatas[ITEMPOS_TURNOVER] = data == null || data.getTurnover() == null
                ? getDefText(isUpdata, ITEMPOS_TURNOVER)
                : MathUtil.INSTANCE.convertToUnitString(data.getTurnover(),1);
        //换手率
        mItemDatas[ITEMPOS_TURNOVER_RATE] = data == null || data.getTurnoverRate() == null
                        ? getDefText(isUpdata, ITEMPOS_TURNOVER_RATE)
                        : data.getTurnoverRate()+"%";
        //市盈率(静)
        mItemDatas[ITEMPOS_PE_RATIO_STATIC] = data == null || data.getPeRatioStatic() == null
                ? getDefText(isUpdata, ITEMPOS_PE_RATIO_STATIC)
                : data.getPeRatioStatic().toString();
        //总市值
        mItemDatas[ITEMPOS_TOTAL_MARK_VALUE] = data == null || data.getTotalMarkValue() == null
                ? getDefText(isUpdata, ITEMPOS_TOTAL_MARK_VALUE)
                : MathUtil.INSTANCE.convertToUnitString(data.getTotalMarkValue(),1);
        //振幅
        mItemDatas[ITEMPOS_AMPLITUDE] = data == null || data.getAmplitude() == null
                ? getDefText(isUpdata, ITEMPOS_AMPLITUDE)
                : data.getAmplitude()+"%";
        //市盈率(TTM)
        mItemDatas[ITEMPOS_PERATIO_TTM] = data == null || data.getPeRatioTTM() == null
                ? getDefText(isUpdata, ITEMPOS_PERATIO_TTM)
                : data.getPeRatioTTM().toString();
        //总股本
        mItemDatas[ITEMPOS_TOTAL_CAPITAL_STOCK] = data == null || data.getTotalCapitalStock() == null
                ? getDefText(isUpdata, ITEMPOS_TOTAL_CAPITAL_STOCK)
                : MathUtil.INSTANCE.convertToUnitString(data.getTotalCapitalStock(),1);
        //52周最高
        mItemDatas[ITEMPOS_FIFTY_TWO_WEEKS_HIGH] = data == null || data.getFiftyTwoWeeksHigh() == null
                ? getDefText(isUpdata, ITEMPOS_FIFTY_TWO_WEEKS_HIGH)
                : data.getFiftyTwoWeeksHigh().toString();
        //市净率
        mItemDatas[ITEMPOS_MARKET_RATE] = data == null || data.getMarketRate() == null
                ? getDefText(isUpdata, ITEMPOS_MARKET_RATE)
                : data.getMarketRate().toString();
        //流通值
        mItemDatas[ITEMPOS_CIRCULATION_VALUE] = getDefText(isUpdata, ITEMPOS_CIRCULATION_VALUE);
        //52周最低
        mItemDatas[ITEMPOS_FIFTY_TWO_WEEKS_LOW] = data == null || data.getFiftyTwoWeeksLow() == null
                ? getDefText(isUpdata, ITEMPOS_FIFTY_TWO_WEEKS_LOW)
                : data.getFiftyTwoWeeksLow().toString();
        //委比
        mItemDatas[ITEMPOS_COMPARISON] = data == null || data.getComparison() == null
                ? getDefText(isUpdata, ITEMPOS_COMPARISON)
                : data.getComparison()+"%";
        //流通股
        mItemDatas[ITEMPOS_CIRCULATING_SHARES] = getDefText(isUpdata, ITEMPOS_CIRCULATING_SHARES);
        //每手
        mItemDatas[ITEMPOS_HANDS] = data == null || data.getHands() == null
                ? getDefText(isUpdata, ITEMPOS_HANDS)
                : data.getHands().toString();
        //量比
        mItemDatas[ITEMPOS_VOLUME_RATIO] = data == null || data.getVolumeRatio() == null
                ? getDefText(isUpdata, ITEMPOS_VOLUME_RATIO)
                : data.getVolumeRatio();
        //股息率
        mItemDatas[ITEMPOS_DIVIDEND_RATE] = data == null || data.getDividendRateFLY() == null
                ? getDefText(isUpdata, ITEMPOS_DIVIDEND_RATE)
                : data.getDividendRateFLY()+"%";

    }

    private String getDefText(boolean isUpdata, int position) {
        return isUpdata ? mItemDatas[position] : "--";
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


}
