package com.zhuorui.securities.openaccount.ui.net.api

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/28
 * Desc:
 */
interface OpenAccountApi {
    companion object {
        /**
         * 获取开户信息接口
         */
        const val GET_OPEN_INFO = "/as_user/api/open/v1/get_open_info"

        /**
         * 获取数字验证码
         */
        const val GET_LIVE_CODE= "/as_user/api/open/v1/get_live_code"

        /**
         * 活体人脸核身
         */
        const val LIVENESS_RECOGNITION= "/as_user/api/open/v1/live_recognition"
    }
}