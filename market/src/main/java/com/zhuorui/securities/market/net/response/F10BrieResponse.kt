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
        var company: Company?,
        var manager: ArrayList<F10ManagerModel>?,
        var shareHolderChange: List<F10ShareHolderModel>?,
        var dividend: List<F10DividendModel>?,
        var repo: List<F10RepoModel>?
    )

    /**
     * 公司简介
     */
    data class Company(
        var name: String?,// 公司名称/证券名称
        var industry: String?,// 所属行业
        var chairman: String?,// 主席
        var totalCapitalStock: BigDecimal?,// 总股本
        var listingDate: String?,// 上市日期
        var issuePrice: BigDecimal?, // 发行价格
        var issueNumber: BigDecimal?,// 发行数量
        var equityHK: BigDecimal?,// 港股股本
        var business: String?// 公司业务
    )
}