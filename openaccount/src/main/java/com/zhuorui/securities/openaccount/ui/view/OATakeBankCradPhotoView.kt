package com.zhuorui.securities.openaccount.ui.view

import com.zhuorui.securities.base2app.ui.fragment.AbsView

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/26 17:13
 *    desc   :
 */
interface OATakeBankCradPhotoView : AbsView{
    fun onBankOcrSuccess(bankCardNo: String, bankCardName: String)
    fun showUpLoading()
    fun hideUpLoading()
    fun getBankCardNo(): String
    fun getBankName(): String
    fun toNext()
    fun showToast(message: String?)
}