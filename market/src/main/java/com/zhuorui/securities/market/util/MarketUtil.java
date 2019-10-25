package com.zhuorui.securities.market.util;

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
}
