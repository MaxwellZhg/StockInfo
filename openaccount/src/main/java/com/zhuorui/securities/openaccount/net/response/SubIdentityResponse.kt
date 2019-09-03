package com.zhuorui.securities.openaccount.net.response

import com.zhuorui.securities.base2app.network.BaseResponse

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/29 18:06
 *    desc   : 上传电子签名
 */
class SubIdentityResponse(val data: Data) : BaseResponse() {

    data class Data(
        val id: String,//开户id
        val openStatus: Int,//开户状态
        val cardNo: String,//身份证号
        val cardName: String,//姓名
        val cardSex: Int,//性别
        val cardNation: String,//名族
        val cardBirth: String,//出生日期
        val cardAddress: String,//住址
        val cardAuthority: String,//发证机关
        val cardValidStartDate: String,//有效期开始时间
        val cardValidEndDate: String,//有效期结束时间
        val cardValidYear: Int//有效期
    )
}