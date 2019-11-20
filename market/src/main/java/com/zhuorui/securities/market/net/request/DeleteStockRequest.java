package com.zhuorui.securities.market.net.request;

import com.zhuorui.securities.base2app.network.BaseRequest;
import com.zhuorui.securities.market.model.SearchStockInfo;
import org.jetbrains.annotations.NotNull;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/8/23 14:19
 * desc   : 删除自选股
 */
public class DeleteStockRequest extends BaseRequest {

    private String ts;
    private String[] codes;
    private SearchStockInfo stockInfo;

    public DeleteStockRequest(@NotNull String transaction, String ts, String... codes) {
        super(transaction);
        
        this.ts = ts;
        this.codes = codes;
        
        generateSign();
    }

    public DeleteStockRequest(@NotNull String transaction, SearchStockInfo stockInfo, String ts, String... codes) {
        super(transaction);

        this.stockInfo = stockInfo;
        this.ts = ts;
        this.codes = codes;

        generateSign();
    }


    public String getTs() {
        return ts;
    }

    public String[] getCodes() {
        return codes;
    }

    public SearchStockInfo getStockInfo() {
        return stockInfo;
    }
}