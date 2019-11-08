package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import java.math.BigDecimal

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/7 10:27
 *    desc   : F10简况
 */
class F10BrieResponse(val data: Data) : BaseResponse() {


    data class Data(
        val company: Company,
        val dividend: List<Dividend>,
        val manager: List<Manager>,
        val shareHolderChange: List<ShareHolderChange>,
        val repo: List<Repo>
    )

    /**
     * 公司简介
     */
    data class Company(
        val name: String,// 公司名称/证券名称
        val industry: String,// 所属行业
        val chairman: String,// 主席
        val totalCapitalStock: BigDecimal,// 总股本
        val listingDate: String,// 上市日期
        val issuePrice: BigDecimal, // 发行价格
        val issueNumber: BigDecimal,// 发行数量
        val equityHK: BigDecimal,// 港股股本
        val business: String// 公司业务
    )

    /**
     * 股东变动
     */
    data class ShareHolderChange(
        val name: String,// 股东名称
        val holdStockNumber: BigDecimal,// 持股数(股)
        val holdStockRatio: BigDecimal,// 持股比例
        val changeNumber: BigDecimal,// 变动
        val date: String,// 日期
        val changeType: Int// 变化类型(1增持/2减持)
    )

    /**
     * 高管信息
     */
    data class Manager(
        val name: String,// 名称
        val jobTitle: String,// 职务
        val salary: BigDecimal,// 薪酬
        val currency: String// 币种
    )

    /**
     * 分红送配
     */
    data class Dividend(
        val exemptionDate: String,// 除权派息日
        val date: String,// 派息日
        val allocationPlan: String,// 分配方案
        val planType: String// 业绩期类型
    )

    /**
     * 公司回购
     */
    data class Repo(
        val date: String,// 回购日
        val number: BigDecimal,// 数量(股)
        val avgPrice: BigDecimal,// 均价
        val repoRatio: BigDecimal// 股份回购占总成交量比例
    )
}