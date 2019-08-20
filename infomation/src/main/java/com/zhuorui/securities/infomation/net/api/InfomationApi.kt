package com.zhuorui.securities.infomation.net.api

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:
 */
interface InfomationApi {
    companion object {
        /**
         * 发送登录短信
         */
        const val SEND_LOGIN_CODE = "/as-user/api/sms/v1/send_login_code"
        /**
         * 用户验证登录
         */
        const val USER_LOGIN_CODE = "/as-user/api/useraccount/v1/user_login_code"
        /**
         * 用户密码登录
         */
        const val USER_PWD_CODE = "/as-user/api/useraccount/v1/user_login_pwd"
        /**
         * 用户注册登录
         */
        const val USER_REGISTER_CODE = "/as-user/api/useraccount/v1/set_login_password"
        /**
         * 用户退出登录
         */
        const val USER_LOGIN_OUT = "/as-user/api/useraccount/v1/sign_out"
    }
}