package com.zhuorui.securities.market.ui.presenter

import android.content.Context
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import com.zhuorui.commonwidget.dialog.DevComfirmDailog
import com.zhuorui.securities.base2app.ui.fragment.AbsEventPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.base2app.util.ToastUtil
import com.zhuorui.securities.market.R
import com.zhuorui.securities.market.model.StockMarketInfo
import com.zhuorui.securities.market.ui.view.RemindSettingView
import com.zhuorui.securities.market.ui.viewmodel.RemindSettingViewModel
import java.util.regex.Pattern

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/8/22 14:28
 *    desc   :
 */
class RemindSettingPresenter(context: Context) : AbsEventPresenter<RemindSettingView, RemindSettingViewModel>(),
    DevComfirmDailog.CallBack {
    val pattern = "^([1-9]\\d*(\\.\\d*[1-9])?)|(0\\.\\d*[1-9])\$"
    /* 加载对话框 */
    private lateinit var phoneDevDailog:DevComfirmDailog

    override fun init() {
        super.init()
    }

    fun deatilSave(upprice: String, downprice: String, uprate: String, downrate: String, stockinfo: StockMarketInfo?) {
        if (!TextUtils.isEmpty(upprice) && upprice.toBigDecimal() < stockinfo?.price) {
            context?.let { ResUtil.getString(R.string.up_setting_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        } else if (!TextUtils.isEmpty(downprice) && downprice.toBigDecimal() > stockinfo?.price) {
            context?.let { ResUtil.getString(R.string.down_setting_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        } else if (!TextUtils.isEmpty(uprate) && !Pattern.compile(pattern).matcher(uprate).find()) {
            context?.let { ResUtil.getString(R.string.up_rate_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        } else if (!TextUtils.isEmpty(downrate) && !Pattern.compile(pattern).matcher(downrate).find()) {
            context?.let { ResUtil.getString(R.string.down_rate_tips)?.let { it1 -> setDailog(it, it1) } }
            phoneDevDailog.show()
        }else if(TextUtils.isEmpty(upprice)&&TextUtils.isEmpty(downprice)&&TextUtils.isEmpty(uprate)&&TextUtils.isEmpty(downrate)){
            ResUtil.getString(R.string.plaease_input_num)?.let { ToastUtil.instance.toastCenter(it) }
        }
    }

    override fun onCancel() {

    }

    override fun onConfirm() {
    }
    fun setDailog(context :Context,str:String){
       phoneDevDailog= DevComfirmDailog.
            createWidth255Dialog(context,true,true)
            .setNoticeText(R.string.tips)
            .setMsgText(str)
            .setCancelText(R.string.cancle)
            .setConfirmText(R.string.ensure)
            .setCallBack(this)
    }

    fun checkSetting():Boolean?{
        val notificationManager: NotificationManagerCompat? = context?.let { NotificationManagerCompat.from(it) }
        return notificationManager?.areNotificationsEnabled()
    }
}