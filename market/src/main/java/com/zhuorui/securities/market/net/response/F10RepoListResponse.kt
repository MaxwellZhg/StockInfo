package com.zhuorui.securities.market.net.response

import com.zhuorui.securities.base2app.network.BaseResponse
import com.zhuorui.securities.market.model.F10RepoModel

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/8 16:25
 *    desc   : F10获取回购列表分页
 */
class F10RepoListResponse : BaseResponse() {

    var data: Data? = null

    data class Data(val list: List<F10RepoModel>, val total: Int, val pageSize: Int, val currentPage: Int)
}