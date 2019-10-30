package com.zhuorui.securities.market.util;

import com.zhuorui.commonwidget.config.LocalSettingsConfig;
import com.zhuorui.commonwidget.config.StocksThemeColor;
import com.zhuorui.securities.market.R;

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
        StocksThemeColor stocksThemeColor = LocalSettingsConfig.Companion.read().getStocksThemeColor();
        if (stocksThemeColor == StocksThemeColor.redUpGreenDown) {
            return R.mipmap.ic_stock_up_arrow_red;
        } else {
            return R.mipmap.ic_stock_up_arrow_green;
        }
    }

    public static int getDownIcon() {
        StocksThemeColor stocksThemeColor = LocalSettingsConfig.Companion.read().getStocksThemeColor();
        if (stocksThemeColor == StocksThemeColor.redUpGreenDown) {
            return R.mipmap.ic_stock_down_arrow_green;
        } else {
            return R.mipmap.ic_stock_down_arrow_red;
        }
    }


}
