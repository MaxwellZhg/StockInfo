package com.zhuorui.securities.openaccount.constants

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/29
 * Desc:
 */
class OpenAccountInfo {
    var id: String? = null //开户ID
    var userId: String? = null //用户ID
    var openStatus: Int? =
        null //开户状态0:未开户;10:身份证ocr; 11:上传身份信息完成; 12:人脸核身通过; 13:银行卡验证通过; 14:完善资料完成; 15:风险披露完成; 21:开户审核中; 22:审核不通过; 31:开户完成
    var cardType: Int? = null //证件类型：1身份证
    var cardNo: String? = null //证件号码
    var cardName: String? = null //证件姓名
    var cardSex: Int? = null //证件性别:（1男 2女）
    var cardNation: String? = null //证件民族
    var cardBirth: String? = null //证件生日(1994-05-03),必须年满18
    var cardAddress: String? = null //证件住址
    var cardAuthority: String? = null //证件发证机关
    var cardValidStartDate: String? = null //证件有效期起始日 (yyyy-mm-dd),小于等于当前时间
    var cardValidEndDate: String? = null //1、证件有效期结始日( yyyy-mm-dd),大于当前时间2、或者传入-1 表示长期
    var cardValidYear: String? = null //证件有效期年数 5:五年有效期 ;10:十年有效期; 20:二十年有效期; -1:长期
    var cardFrontPhoto: String? = null //证件正面照url（有效期为一个小时）
    var cardBackPhoto: String? = null //证件背面照url（有效期为一个小时）
    var video: String? = null //活体视频url（有效期为一个小时）
    var validateCode: String? = null //活体视频数字
    var bankCardNo: String? = null //验证的银行卡号
    var bankCardName: String? = null //验证银行卡的银行
    var signaturePhoto: String? = null //电子签名url（有效期为一个小时）
    var mailbox: String? = null //邮箱
    var occupation: Int? = null //就业状态 1:受雇; 2:自雇 ;3:退休 ;4:学生 ;5:其他
    var taxType: Int? = null //税务类型: 1:单一国家 ;2:多个国家
    var taxState: String? = null // 税务国家/地区
    var taxNumber: String? = null //税务编号
    var income: Int? = null // 财产状况： 1:1000万以上 ;2:500万-1000万; 3:100-500万 ;4:50-100万; 5:10-50万; 6:0-10万
    var rate: Int? = null //交易频次：1:大于20次;2:5-20次;3:0-5次
    var risk: Int? = null //风险承受能力：1:高;2:中;3:低
    var capitalSource: String? = null//资金来源： 多个用，号隔开，比如1，2表示用户选择薪金和存款 1:薪金; 2:存款; 3:租金 ;4:投资 ;5:借贷 ;6:其他
    var investShares: Int? = null //股票投资经验：1:五年之上; 2:三至五年; 3:一至三年; 4:一年之内; 5:没有经验
    var investBond: Int? = null//债券投资经验：(具体值同股票)
    var investGoldForeign: Int? = null//外币/黄金投资经验：(具体值同股票)
    var investFund: Int? = null//基金/理财投资经验：(具体值同股票)


}