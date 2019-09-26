package com.zhuorui.securities.openaccount.manager

import android.text.TextUtils
import com.zhuorui.securities.alioss.service.OssService
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.net.response.*
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
    var bucket: BucketResponse.Data = BucketResponse.Data("", "", "")

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
     * 开户流程开始页面
     *
     */
    fun getStartFragment(): ISupportFragment? {
        return when (info?.openStatus) {
            //未开户
            0 -> {
                OASelectRegionFragment.newInstance()
            }
            //审核中
            21 -> {
                OAWaitAuditFragment.newInstance()
            }
            //审核不通过
            22 -> {
                OAWaitAuditFragment.newInstance(info?.reasonsFail.toString())
            }
            //开户完成
            31 -> {
                //目前不确实要做什么操作
                null
            }
            else -> {
                OADataSupplementaryTispFragment.newInstance()
            }
        }
    }

    /**
     * 审核不通用户修改资料流程开始页面
     *
     */
    fun getFailStartFragment(): ISupportFragment? {
        return when (info?.openStatus) {
            //身份证ocr
            10 -> {
                OAUploadDocumentsFragment.newInstance()
            }
            //上传身份信息
            11 -> {
                OAConfirmDocumentsFragment.newInstance()
            }
            //人脸核身
            12 -> {
                OABiopsyFragment.newInstance()
            }
            //银行卡验证
            13 -> {
                OATakeBankCradPhotoFragment.newInstance()
            }
            //完善资料
            14 -> {
                OAPersonalInformationFragment.newInstance()
            }
            //风险披露
            15 -> {
                OARiskDisclosureFragment.newInstance()
            }
            16 -> {
                OASignatureFragment.newInstance()
            }
            else -> {
                null
            }
        }
    }

    /**
     * 获取继续开户下一步页面
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
                    OAUploadDocumentsFragment.newInstance()
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
            else -> {
                null
            }
        }

    }

    fun destroy() {
        instance = null
    }


}