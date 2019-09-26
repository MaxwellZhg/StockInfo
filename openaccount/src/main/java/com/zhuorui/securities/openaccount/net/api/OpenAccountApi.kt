package com.zhuorui.securities.openaccount.net.api

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
        const val GET_OPEN_INFO = "as_user/api/open/v1/get_open_info"

        /**
         * 获取数字验证码
         */
        const val GET_LIVE_CODE = "as_user/api/open/v1/get_live_code"

        /**
         * 活体人脸核身
         */
        const val LIVENESS_RECOGNITION = "as_user/api/open/v1/live_recognition"

        /**
         * 上传电子签名
         */
        const val SUB_SIGNATURE = "as_user/api/open/v1/sub_signature"

        /**
         * 身份证OCR
         */
        const val ID_CARD_OCR = "as_user/api/open/v1/card_ocr"

        /**
         * 上传身份信息
         */
        const val SUB_IDENTITY = "as_user/api/open/v1/sub_identity"

        /**
         * 个人风险测评资料
         */
        const val SUB_BASICS_INFO = "as_user/api/open/v1/sub_basics_info"

        /**
         * 银行卡OCR
         */
        const val BANK_OCR = "as_user/api/open/v1/bank_ocr"

        /**
         * 银行卡三要素认证+一类卡认证
         */
        const val BANK_CARD_VERIFICATION = "as_user/api/open/v1/bank_card_verification"

        /**
         * 上传风险披露情况接口
         */
        const val SUB_RISK_DISCLOSURE = "as_user/api/open/v1/sub_risk_disclosure"

        /**
         * 获取所有存储空间
         */
        const val BUCKET_NAME = "as_common/api/sts/v1/bucket_name"

    }
}