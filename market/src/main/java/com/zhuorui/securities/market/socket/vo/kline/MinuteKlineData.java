package com.zhuorui.securities.market.socket.vo.kline;

/**
 * 分时数据基础对象
 */
public class MinuteKlineData {

    /**
     * 交易量
     */
    public double sharestraded;

    /**
     * 成交额
     */
    public double turnover;

    /**
     * 均价
     */
    public double vwap;

    /**
     * 行情时间
     */
    public long time;

    /**
     * 不复权
     */
    public Adj adj;

    /**
     * 前复权
     */
    public Adj beforeAdj;

    /**
     * 后复权
     */
    public Adj afterAdj;

    public class Adj {

        /**
         * 开盘价
         */
        public double open;

        /**
         * 收盘价
         */
        public double close;

        /**
         * 最高价
         */
        public double high;

        /**
         * 最低价
         */
        public double low;

        /**
         * 上根K线收盘价
         */
        public double preClose;
    }
}
