package com.zhuorui.securities.personal.net.api

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/15
 * Desc:
 */
interface PersonalApi {
    companion object {
        /**
         * 发送登录短信
         */
        const val SEND_LOGIN_CODE = "/as_user/api/sms/v1/send_login_code"
        /**
         * 用户验证登录
         */
        const val USER_LOGIN_CODE = "/as_user/api/user_account/v1/user_login_code"
        /**
         * 用户密码登录
         */
        const val USER_PWD_CODE = "/as_user/api/user_account/v1/user_login_pwd"
        /**
         * 用户注册登录
         */
        const val USER_REGISTER_CODE = "/as_user/api/user_account/v1/set_login_password"
        /**
         * 用户退出登录
         */
        const val USER_LOGIN_OUT = "/as_user/api/user_account/v1/sign_out"
        /**
         * 发送忘记密码短信
         *
         */
        const val SEND_FORGET_CODE = "/as_user/api/sms/v1/send_forget_code"

        /**
         * 验证忘记密码发送短信
         *
         */
        const val VERIFY_FORGET_CODE = "/as_user/api/user_account/v1/forgot_password_code"
        /**
         * 重置登录密码
         *
         */
        const val REST_LOGIN_PSW = "/as_user/api/user_account/v1/reset_login_password"

        /**
         *获取用户信息
         *
         */
        const val GET_USER_INFO = "/as_user/api/user_account/v1/user_info"
        /**
         *修改手机号发送旧号码短信
         *
         */
        const val SEND_OLD_REPLACE_CODE = "/as_user/api/sms/v1/send_old_replace_code"
        /**
         *验证旧手机号码
         *
         */
        const val MODIFY_PHONE_OLD = "/as_user/api/user_account/v1/modify_phone_v1"
        /**
         *获取新手机号码验证码
         *
         */
        const val SEND_NEW_REPLACE_CODE = "/as_user/api/sms/v1/send_new_replace_code"
        /**
         *验证新手机号验证码
         *
         */
        const val MODIFY_PHONE_NEW = "/as_user/api/user_account/v1/modify_phone_v2"
        /**
         *验证新的登录密码
         *
         */
        const val MODIFY_LOGIN_PASSWORD = "/as_user/api/user_account/v1/modify_login_password"
        /**
         *验证新的交易密码
         *
         */
        const val MODIFY_CAPITAL_PASSWORD = "/as_user/api/user_account/v1/modify_capital_password"

        /**
         *获取用户信息
         *
         */
        const val USER_INFO = "/as_user/api/user_account/v1/user_info"

    }
}