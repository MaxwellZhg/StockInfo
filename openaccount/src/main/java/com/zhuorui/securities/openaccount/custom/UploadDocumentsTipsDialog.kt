package com.zhuorui.securities.openaccount.custom

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.zhuorui.securities.base2app.dialog.BaseDialog
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.openaccount.R
import com.zhuorui.securities.openaccount.R2

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/26 15:56
 *    desc   : 拍摄银行卡提醒对话框
 */
class UploadDocumentsTipsDialog(context: Context) :
    BaseDialog(context, ResUtil.getDimensionDp2Px(265f), WindowManager.LayoutParams.WRAP_CONTENT),
    View.OnClickListener {

    @BindView(R2.id.tv_close)
    lateinit var tv_close: TextView
    @BindView(R2.id.tv_title)
    lateinit var tv_title: TextView
    @BindView(R2.id.iv_image)
    lateinit var iv_image: ImageView
    @BindView(R2.id.tv_tips)
    lateinit var tv_tips: TextView

    override val layout: Int
        get() = R.layout.dialog_take_photo_bank_card_tips

    override fun init() {
        tv_close.setOnClickListener(this)
        iv_image.setImageResource(R.mipmap.example_idcard)
        tv_tips.setText(R.string.idcard_tips)
    }

    override fun onClick(p0: View?) {
        dialog?.dismiss()
    }

    fun show(title: String) {
        tv_title.setText(title)
        super.show()

    }
}