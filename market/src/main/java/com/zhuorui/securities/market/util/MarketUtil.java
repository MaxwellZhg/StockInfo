package com.zhuorui.securities.market.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.commonwidget.config.StocksThemeColor;
import com.zhuorui.securities.base2app.util.ResUtil;
import com.zhuorui.securities.base2app.util.TimeZoneUtil;
import com.zhuorui.securities.market.R;
import com.zhuorui.securities.personal.config.LocalAccountConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

/**
 * author : liuwei
 * e-mail : vsanliu@foxmail.com
 * date   : 2019-10-25 17:05
 * desc   :
 */
public class MarketUtil {

    public static int getStockTSIcon(String ts) {
        int ic = 0;
        if (!TextUtils.isEmpty(ts)) {
            switch (ts) {
                case "HK":
                    ic = R.mipmap.ic_ts_hk;
                    break;
            }
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

    /**
     * 涨跌动画
     *
     * @param animator 正在执行的动画
     * @param view     执行动画的view
     * @param isUp     是否涨
     * @return
     */
    public static ObjectAnimator showUpDownAnim(ObjectAnimator animator, View view, boolean isUp) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        view.setBackgroundColor(getUpDownAnimColor(isUp));
        animator = new ObjectAnimator().ofFloat(view, "alpha", 0, 1, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setBackground(null);
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        view.setVisibility(View.VISIBLE);
        return animator;
    }

    /**
     * 获取涨跌动画颜色
     * @param isUp
     * @return
     */
    public static int getUpDownAnimColor(boolean isUp) {
        return isUp ? 0x33D9001B : 0x3300CC00;
    }

    /**
     * 计算一组数据使用的单位基础数(分为1,万，亿三个等级)
     *
     * @param data
     * @return
     */
    public static BigDecimal getUnitBigDecimal(List<BigDecimal> data) {
        BigDecimal unit;
        if (data.size() > 1) {
            BigDecimal min = data.get(0).abs();
            BigDecimal max = min;
            BigDecimal zroe = new BigDecimal(0);
            for (BigDecimal d : data.subList(1, data.size())) {
                BigDecimal absD = d.abs();
                if (absD.compareTo(zroe) == 1 && (min.compareTo(zroe) < 1 || absD.compareTo(min) == -1)) {
                    min = absD;
                }
                if (absD.compareTo(max) == 1) {
                    max = absD;
                }
            }
            BigDecimal minUnit = getUnitBigDecimal(min);
            BigDecimal maxUnit = getUnitBigDecimal(max);
            BigDecimal divide = maxUnit.divide(minUnit, 0);
            if (divide.compareTo(H) == 0) {
                unit = minUnit;
            }else  if (divide.compareTo(W) == 0){
                unit = maxUnit;
            }else {
                unit = W;
            }
        } else {
            unit = getUnitBigDecimal(data.get(0).abs());
        }
        return unit;
    }

    /**
     * 计算一组数据使用的单位基础数(分为1,万，亿三个等级)
     *
     * @param data
     * @return
     */
    public static BigDecimal getUnitBigDecimal(BigDecimal... data) {
        BigDecimal unit;
        if (data.length > 1) {
            BigDecimal min = data[0].abs();
            BigDecimal max = min;
            BigDecimal zroe = new BigDecimal(0);
            for (int i = 1; i < data.length; i++) {
                BigDecimal d = data[i];
                BigDecimal absD = d.abs();
                if (absD.compareTo(zroe) == 1 && (min.compareTo(zroe) < 1 || absD.compareTo(min) == -1)) {
                    min = d;
                }
                if (absD.compareTo(max) == 1) {
                    max = d;
                }
            }
            BigDecimal minUnit = getUnitBigDecimal(min);
            BigDecimal maxUnit = getUnitBigDecimal(max);
            BigDecimal divide = maxUnit.divide(minUnit, 0);
            if (divide.compareTo(H) == 0) {
                unit = minUnit;
            }else  if (divide.compareTo(W) == 0){
                unit = maxUnit;
            }else {
                unit = W;
            }
        } else {
            unit = getUnitBigDecimal(data[0].abs());
        }
        return unit;
    }

    /**
     * W代表万，Y代表亿
     */
    private static BigDecimal H = BigDecimal.valueOf(1);
    private static BigDecimal W = BigDecimal.valueOf(10000);
    private static BigDecimal Y = BigDecimal.valueOf(100000000);

    /**
     * 计算单位基础数，分为1,万，亿三个等级
     *
     * @param data
     * @return
     */
    public static BigDecimal getUnitBigDecimal(BigDecimal data) {
        if (data.abs().compareTo(Y) >= 0) {
            return Y;
        } else if (data.abs().compareTo(W) >= 0) {
            return W;
        } else {
            return H;
        }
    }

    /**
     * 获取单位中文
     *
     * @param data
     * @return
     */
    public static String getUnitName(BigDecimal data) {
        if (data.compareTo(Y) >= 0) {
            return ResUtil.INSTANCE.getString(R.string.unit_y);
        } else if (data.compareTo(W) >= 0) {
            return ResUtil.INSTANCE.getString(R.string.unit_w);
        } else {
            return "";
        }

    }
}
