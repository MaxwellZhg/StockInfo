package com.zhuorui.securities.market.model

/**
 * 分红送配
 */
data class F10DividendModel(
    val exemptionDate: String,// 除权派息日
    val date: String,// 派息日
    val allocationPlan: String,// 分配方案
    val planType: String// 业绩期类型
)