package com.dycm.applib1.model

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/6 16:31
 *    desc   :
 */
class StockKlineTopic(dataType: StockTopicDataTypeEnum, ts: String, code: String, type: Int, val startTime: Long) :
    StockTopic(dataType, ts, code, type)