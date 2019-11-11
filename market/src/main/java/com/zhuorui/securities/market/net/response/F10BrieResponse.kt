package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.F10DividendModel
import com.zhuorui.securities.market.model.F10ManagerModel
import com.zhuorui.securities.market.model.F10RepoModel
import com.zhuorui.securities.market.model.F10ShareHolderModel
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
        val manager: ArrayList<F10ManagerModel>,
        val shareHolderChange: List<F10ShareHolderModel>,
        val dividend: List<F10DividendModel>,
        val repo: List<F10RepoModel>
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
}