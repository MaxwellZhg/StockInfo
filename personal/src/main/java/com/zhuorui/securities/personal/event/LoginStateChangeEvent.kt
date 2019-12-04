package com.zhuorui.securities.personal.event

/**
 * author : PengXianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019/8/23 16:37
 * desc   : 登录状态发生改变
 */
class LoginStateChangeEvent {
    var isLogin: Boolean = false
    var transaction: String? = null

    constructor(isLogin: Boolean) {
        this.isLogin = isLogin
    }

    constructor(isLogin: Boolean, transaction: String?) {
        this.isLogin = isLogin
        this.transaction = transaction
    }
}