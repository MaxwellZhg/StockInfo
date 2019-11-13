package com.zhuorui.securities.market.util;

import android.text.TextUtils;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.commonwidget.config.StocksThemeColor;
import com.zhuorui.securities.base2app.util.TimeZoneUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.personal.config.LocalAccountConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-25 17:05
 * desc   :
 */
public class MarketUtil {

    public static int getStockTSIcon(String ts) {
        int ic;
        switch (ts) {
            case "HK":
                ic = R.mipmap.ic_ts_hk;
                break;
            default:
                ic = 0;
                break;
        }
        return ic;
    }

    public static int getUpIcon() {
        StocksThemeColor stocksThemeColor = LocalSettingsConfig.Companion.getInstance().getStocksThemeColor();
        if (stocksThemeColor == StocksThemeColor.redUpGreenDown) {
            return R.mipmap.ic_stock_up_arrow_red;
        } else {
            return R.mipmap.ic_stock_up_arrow_green;
        }
    }

    public static int getDownIcon() {
        StocksThemeColor stocksThemeColor = LocalSettingsConfig.Companion.getInstance().getStocksThemeColor();
        if (stocksThemeColor == StocksThemeColor.redUpGreenDown) {
            return R.mipmap.ic_stock_down_arrow_green;
        } else {
            return R.mipmap.ic_stock_down_arrow_red;
        }
    }

    public static boolean isBMP(String ts) {
        return TextUtils.equals("HK", ts) && !LocalAccountConfig.Companion.getInstance().isLogin();
    }

    /**
     * @param ts                股票市场
     * @param closingTimeMillis 收盘时间（今天不是交易日：上个交易日收盘时间。是交易日，未开盘：上个交易日收盘时间，未收盘：0，已收盘：今日收盘时间）
     * @param isTrading         是否交易
     * @return
     */
    @NotNull
    public static String getStockStatusTxt(@Nullable String ts, long closingTimeMillis, boolean isTrading) {
        String txt = "--";
        if (TextUtils.isEmpty(ts)) return txt;
        switch (ts) {
            case "HK":
                if (isTrading) {
                    txt = getHKStockStatusTxt(closingTimeMillis);
                } else {
                    txt = String.format("已收盘 %s", TimeZoneUtil.timeFormat(closingTimeMillis, "MM-dd HH:mm:ss"));
                }
                break;
        }
        return txt;
    }

    /**
     * 获取股票当日交易状态
     *
     * @param closingTimeMillis
     * @return
     */
    private static String getHKStockStatusTxt(long closingTimeMillis) {
        long timeMillis = TimeZoneUtil.currentTimeMillis();
        String txt;
        if (closingTimeMillis != 0 && timeMillis >= closingTimeMillis) {//收盘-24:00
            txt = String.format("已收盘 %s", TimeZoneUtil.timeFormat(closingTimeMillis, "MM-dd HH:mm:ss"));
        } else {
            String dateStart = TimeZoneUtil.timeFormat(timeMillis, "yyyy-MM-dd") + "00:00:00";
            long dateMillis = TimeZoneUtil.parseTime(dateStart, "yyyy-MM-ddHH:mm:ss");
            long s = (timeMillis - dateMillis) / 1000;//当前时间是今天的第几秒
            if (s < 32400) {
                // 00:00-9:00
                txt = String.format("未开盘 %s", TimeZoneUtil.timeFormat(closingTimeMillis, "MM-dd HH:mm:ss"));
            } else if (s < 33600) {
                // 09:00-9:20
                txt = String.format("盘前竞价 %s 09:00:00", TimeZoneUtil.timeFormat(timeMillis, "MM-dd"));
            } else if (s < 34200) {
                // 9:20-9:30
                txt = String.format("等待开盘 %s 09:20:00", TimeZoneUtil.timeFormat(timeMillis, "MM-dd"));
            } else if (s < 43200) {
                // 9:30-12:00
                txt = String.format("交易中 %s", TimeZoneUtil.timeFormat(timeMillis, "MM-dd HH:mm:ss"));
            } else if (s < 46800) {
                // 12:00-13:00
                txt = String.format("午间休市 %s 12:00:00", TimeZoneUtil.timeFormat(timeMillis, "MM-dd"));
            } else if (s < 57600) {
                // 13:00-16:00
                txt = String.format("交易中 %s", TimeZoneUtil.timeFormat(timeMillis, "MM-dd HH:mm:ss"));
            } else {
                // 16:00-收盘
                txt = String.format("收盘竞价 %s", TimeZoneUtil.timeFormat(timeMillis, "MM-dd HH:mm:ss"));
            }
        }
        return txt;
    }

    /**
     * 根据TS获取货币代码
     *
     * @param ts
     * @return
     */
    public static String getCurrencyCodeByTs(String ts) {
        String code = "";
        switch (ts) {
            case "HK":
                code = "HKD";
                break;
        }
        return code;
    }
}
