package com.zhuorui.securities.market.socket.push

import com.zhuorui.securities.market.socket.request.SocketHeader
import com.zhuorui.securities.market.socket.response.SocketResponse

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/4 11:29
 *    desc   : websocket订阅自选股推送买卖经纪席位数据(买卖盘席位)
 */
class StocksTopicOrderBrokerResponse : SocketResponse() {
    var header: SocketHeader? = null
    var body: Body? = null

    // {
    //"code": "1810",
    //"content": ["4377,巴克莱亚", "4098,瑞士信贷", "4086,瑞士信贷", "1194,瑞士信贷", "4098,瑞士信贷", "1450,", "1194,瑞士信贷", "4373,巴克莱亚", "6698,盈透证券", "-1", "4086,瑞士信贷", "1194,瑞士信贷", "4098,瑞士信贷", "0141,海通国际", "3439,高盛(亚", "6310,第一上海", "6997,中国投资", "6999,中国投资", "4085,瑞士信贷", "4085,瑞士信贷", "4098,瑞士信贷", "8577,汇丰证券", "9033,招商证券", "1450,", "4098,瑞士信贷", "4086,瑞士信贷", "4085,瑞士信贷", "3439,高盛(亚", "3439,高盛(亚", "6698,盈透证券", "3439,高盛(亚", "5998,中国创盈", "4086,瑞士信贷", "8304,富途证券", "7356,", "6997,中国投资", "4086,瑞士信贷", "3439,高盛(亚", "3439,高盛(亚", "3439,高盛(亚"],
    //"recordId": "183289ad286148e298e28ad3a94524c1",
    //"tradeMark": 1,
    //"ts": "HK",
    //"type": 2
    //}
    data class Body(
        val ts: String,
        val type: Int,
        val code: String,
        val tradeMark: Int, // 1-买方经纪 2-卖方经纪
        val content: List<String>, // 经纪商
        val recordId: String // 记录唯一id 32位字符串
    )
}
