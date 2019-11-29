package com.zhuorui.securities.market

import com.zhuorui.commonwidget.dialog.NoticeDialog
import com.zhuorui.commonwidget.push.AbsPushManager
import com.zhuorui.securities.base2app.BaseApplication
import com.zhuorui.securities.base2app.ui.activity.AbsActivity
import com.zhuorui.securities.market.ui.SimulationTradingMainFragment

/**
 *    author : PengXianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019/11/27 17:21
 *    desc   : 管理市场行情模块的通知
 */
class MkPushManager : AbsPushManager {

    override fun checkPushData(data: String): Boolean {
        // TODO 暂时不判断消息类型
        return true
    }

    override fun onReceivePushData(data: String) {
        // TODO 暂时不判断消息类型
        // app不处于后台
        if (!BaseApplication.baseApplication.isInBackground!!) {
            // 在app内部上方弹出通知
            BaseApplication.baseApplication.topActivity?.let { activity ->
                NoticeDialog.createDialog(activity, object : NoticeDialog.CallBack {
                    override fun onMessageClicked() {
                        // TODO 假设消息通知需要打开开户
                        if (activity is AbsActivity) {
                            activity.start(SimulationTradingMainFragment.newInstance())
                        }
                    }
                }).setDisPlayNoticeInfo(R.mipmap.ic_push_notice, R.string.ntioc_str, "现在", "开户成功", data).show()
            }
        }
    }
}