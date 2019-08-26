package com.zhuorui.securities.openaccount.custom

import android.content.Context
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.openaccount.R

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/26 15:56
 *    desc   : 拍摄银行卡提醒对话框
 */
class TakePhotoBankCardTipsDialog(context: Context) : BaseDialog(context) {

    override val layout: Int
        get() = R.layout.dialog_take_photo_bank_card_tips


}