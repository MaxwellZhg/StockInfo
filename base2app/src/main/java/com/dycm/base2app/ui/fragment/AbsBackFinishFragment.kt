package com.dycm.base2app.ui.fragment

import com.dycm.base2app.BaseApplication
import com.dycm.base2app.R
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.util.AppUtil
import com.dycm.base2app.util.ResUtil

/**
 *    author : Pengxianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019-05-20 14:13
 *    desc   : 双击返回键退出Activity的Fragment
 *             适用场景：一级页面（如主页中的Tab）
 */
abstract class AbsBackFinishFragment : AbsFragment() {
    private var TOUCH_TIME: Long = 0

    /**
     * 处理回退事件
     *
     * @return
     */
    override fun onBackPressedSupport(): Boolean {
        LogInfra.Log.d(TAG, "onBackPressedSupport()")
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish()
        } else {
            TOUCH_TIME = System.currentTimeMillis()
            toast(ResUtil.getStringFormat(R.string.press_again_exit, AppUtil.getAppName(BaseApplication.context!!) ?: ""))
        }
        return true
    }

    companion object {
        // 再点一次退出程序时间设置
        private val WAIT_TIME = 2000L
    }
}
