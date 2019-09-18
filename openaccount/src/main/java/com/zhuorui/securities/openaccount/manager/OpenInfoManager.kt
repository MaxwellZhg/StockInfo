package com.zhuorui.securities.openaccount.manager

import android.text.TextUtils
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.net.response.BankCardVerificationResponse
import com.zhuorui.securities.openaccount.net.response.SubBasicsInfoResponse
import com.zhuorui.securities.openaccount.net.response.SubIdentityResponse
import com.zhuorui.securities.openaccount.net.response.SubRiskDisclosureResponse
import com.zhuorui.securities.openaccount.ui.*
import me.yokeyword.fragmentation.ISupportFragment

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/29
 * Desc:
 */
open class OpenInfoManager {
    var info: OpenAccountInfo? = null

    companion object {
        private var instance: OpenInfoManager? = null

        fun getInstance(): OpenInfoManager? {//使用同步锁
            if (instance == null) {
                synchronized(OpenInfoManager::class.java) {
                    if (instance == null) {
                        instance = OpenInfoManager()
                    }
                }
            }
            return instance
        }

    }

    fun readSubIdentityResponse(data: SubIdentityResponse.Data) {
        info?.openStatus = data.openStatus
        info?.cardNo = data.cardNo
        info?.cardName = data.cardName
        info?.cardSex = data.cardSex
        info?.cardNation = data.cardNation
    }

    fun readSubBasicsInfoResponse(data: SubBasicsInfoResponse.Data) {
        info?.openStatus = data.openStatus
    }

    fun readSubRiskDisclosureResponse(data: SubRiskDisclosureResponse.Data) {
        info?.openStatus = data.openStatus
    }

    fun readBankCardVerificationResponse(data: BankCardVerificationResponse.Data) {
        info?.openStatus = data.openStatus
        info?.bankCardNo = data.bankCardNo
        info?.bankCardName = data.bankCardName
    }

    /**
     * 获取下一步步骤页面
     * */
    fun getNextFragment(): ISupportFragment? {
        return when (info?.openStatus) {
            //未开户
            0 -> {
                OASelectRegionFragment.newInstance()
            }
            //已做身份证ocr
            10 -> {
                if (TextUtils.isEmpty(info?.cardFrontPhoto) || TextUtils.isEmpty(info?.cardBackPhoto))
                    OASelectRegionFragment.newInstance()
                else
                    OAConfirmDocumentsFragment.newInstance()
            }
            //上传身份信息完成
            11 -> {
                OABiopsyFragment.newInstance()
            }
            //人脸核身通过
            12 -> {
                OATakeBankCradPhotoFragment.newInstance()
            }
            //银行卡验证通过
            13 -> {
                OAPersonalInformationFragment.newInstance()
            }
            //完善资料完成
            14 -> {
                OARiskDisclosureFragment.newInstance()
            }
            //风险披露完成
            15 -> {
                OASignatureFragment.newInstance()
            }
            21 -> {
                OAWaitAuditFragment.newInstance()
            }
            //审核不通过
            22 -> {
                null
            }
            //开户完成
            31 -> {
                null
            }
            else -> {
                null
            }
        }

    }

    fun destroy() {
        instance = null
    }


}