package com.zhuorui.securities.personal.ui.presenter

import android.content.Context
import com.zhuorui.commonwidget.dialog.ProgressDialog
import com.zhuorui.securities.base2app.ui.fragment.AbsNetPresenter
import com.zhuorui.securities.base2app.util.ResUtil
import com.zhuorui.securities.personal.R
import com.zhuorui.securities.personal.ui.view.ForgetTradePassView
import com.zhuorui.securities.personal.ui.viewmodel.ForgetTradePassViewModel
import java.util.*

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/9/11
 * Desc:
 */
class ForgetTradePassPresenter(context: Context) :AbsNetPresenter<ForgetTradePassView,ForgetTradePassViewModel>(){
    internal var timer: Timer? = null
    private var recLen = 60//跳过倒计时提示5秒
    internal var task: TimerTask? = null

    override fun init() {
        super.init()
    }

    fun sendCapitalPhoneCode(){
        startTimeCountDown()
    }
    @Throws(InterruptedException::class)
    fun startTask() {
        if (task == null) {
            timer = Timer()
            task = object : TimerTask() {
                override fun run() {
                    recLen--
                    viewModel?.str?.set(recLen.toString()+"s")
                    if (recLen < 0) {
                        timer!!.cancel()
                        task = null
                        timer = null
                        viewModel?.str?.set(ResUtil.getString(R.string.get_verify_code))
                    }
                }
            }
        }
        timer!!.schedule(task, 1000, 1000)
    }

    private fun startTimeCountDown() {
        if (recLen == 60) {
            try {
                startTask()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        } else if (recLen < 0) {
            recLen = 60
            try {
                startTask()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }


}