package com.zhuorui.securities.openaccount.net.request

import com.zhuorui.securities.base2app.network.BaseRequest

/**
 *    desc   : 上传身份证
 */
class SubIdentityRequest(
    val cardNo: String, // 证件号码
    val cardName: String, // 证件姓名
//    val cardSex: Int, // 证件性别（1男 2女）
    val cardNation: String, // 证件民族
    val cardAddress: String, // 证件住址
    val cardAuthority: String, // 证件发证机关
//    val cardValidStartDate: String, // 证件有效期起始日 yyyy-mm-dd （小于等于当天）
//    val cardValidEndDate: String, // 证件有效期结始日  yyyy-mm-dd （大于当天） 如果证件有效期为长期   传 -1
//    val cardValidYear: String, // 证件有效期年数 5   五年有效期 10 十年有效期 20  二十年有效期 -1  长期
//    val cardBirth: String, // 证件生日（必须年满18）
    val id: String, //开户id
    transaction: String
) : BaseRequest(transaction) {

    init {
        generateSign()
    }



}