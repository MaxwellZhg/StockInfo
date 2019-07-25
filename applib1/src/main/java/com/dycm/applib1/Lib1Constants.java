package com.dycm.applib1;

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/7/22 15:20
 * desc   : 定义常量
 */
public class Lib1Constants {

    /**
     * 定义socket中常量
     */
    public static final String SOCKET_URL = "ws://192.168.1.213:1949"; // 服务器地址
    public static final String AUTH = "auth.auth"; // 链接认证
    public static final String SOCKET_AUTH_SIGNATURE = "069de7990c0c4b8d87f516b7478e9f4a"; // 链接认证签名
    public static final String BIND = "topic.reBind"; // 订阅
    public static final String DATA = "stock.data"; // 推送股票数据

}
