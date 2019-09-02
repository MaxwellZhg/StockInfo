package com.zhuorui.securities.openaccount.manager

import androidx.fragment.app.Fragment
import com.zhuorui.securities.base2app.infra.AbsConfig
import com.zhuorui.securities.openaccount.constants.OpenAccountInfo
import com.zhuorui.securities.openaccount.net.response.OpenInfoResponse
import com.zhuorui.securities.openaccount.ui.*

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

    /**
     * 获取下一步步骤页面
     * */
    fun getNextFragment(): Fragment? {
        var fragment: Fragment? = null
        when (info?.openStatus) {
            //未开户
            0 -> {
                fragment = OASelectRegionFragment.newInstance()
            }
            //已做身份证ocr
            10 -> {
                fragment = OAConfirmDocumentsFragment.newInstance()
            }
            //上传身份信息完成
            11 -> {
                fragment = OABiopsyFragment.newInstance()
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

            }
            //开户完成
            31 -> {

            }
        }

        return fragment
    }

    fun destroy() {
        instance = null
    }


}