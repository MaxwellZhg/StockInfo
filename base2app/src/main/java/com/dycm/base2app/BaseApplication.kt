package com.dycm.base2app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.dycm.base2app.config.Config
import com.dycm.base2app.infra.LogInfra
import com.dycm.base2app.infra.SharedPreManager
import com.dycm.base2app.infra.StorageInfra
import com.dycm.base2app.network.Network
import com.dycm.base2app.util.AppActivityLifecycleImp
import com.dycm.base2app.util.AppUtil
import com.dycm.base2app.util.ThreadPoolUtil
import com.qw.soul.permission.SoulPermission
import me.yokeyword.fragmentation.Fragmentation
import me.yokeyword.fragmentation.helper.ExceptionHandler
import okhttp3.Interceptor
import java.lang.Exception

/**
 * Create by xieyingwu on 2018/4/3.
 * 类描述：基础的Application类信息
 */
abstract class BaseApplication : MultiDexApplication(), AppActivityLifecycleImp.AppCloseListener {

    var config: Config? = null
    var appActivityLifecycleImp: AppActivityLifecycleImp? = null

    /**
     * 获取默认的Log的Tag标签
     *
     * @return TAG
     */
    protected abstract val logTag: String

    /**
     * 获取默认的Sp存储文件目录名称
     *
     * @return defaultSpName
     */
    protected abstract val defaultSpName: String

    val createdAcNum: Int?
        get() = appActivityLifecycleImp?.createdACNum

    /**
     * 是否处于后台
     */
    val isInBackground: Boolean?
        get() = appActivityLifecycleImp?.isInBackground

    /**
     * 返回当前显示的Activity
     *
     * @return
     */
    val topActivity: Activity?
        get() = appActivityLifecycleImp?.topActivity

    override fun onCreate() {
        super.onCreate()
        context = this
        AppUtil.init(this)
//        ThreadPoolUtil.getThreadPool().run {
            initConfig()
            initInfra()
            initNetwork()
            initPermission()
            initFragmentation()

            beforeInit()
//        }
        /*注册ActivityLifeCycle*/
        appActivityLifecycleImp = AppActivityLifecycleImp(this)
        registerActivityLifecycleCallbacks(appActivityLifecycleImp)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initFragmentation() {
        Fragmentation.builder()
            // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(true)
            /**
             * 可以获取到[me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning]
             * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
             */
            .handleException { e -> LogInfra.Log.e(logTag, e.message, e) }
            .install()
    }

    private fun initPermission() {
        SoulPermission.init(this)
    }

    private fun initConfig() {
        config = Config(this)
    }

    protected abstract fun beforeInit()

    /**
     * 初始化日志和SP存储配置
     */
    protected fun initInfra() {
        val logLevel = config?.logLevel()
        val debug = config?.isDebug
        if (debug != null && logLevel != null)
            LogInfra.init(logTag, debug, logLevel)
        SharedPreManager.getInstance().init(this, defaultSpName)
        StorageInfra.init(defaultSpName)
    }

    /**
     * 初始化网络配置
     */
    protected fun initNetwork() {
        val domain = config?.domain()
        val debug = config?.isDebug
        val writeTimeout = config?.writeTimeout()
        val readTimeout = config?.readTimeout()
        val connectTimeout = config?.connectTimeout()
        Network.initRetrofit(domain, debug, writeTimeout?.toLong(), readTimeout?.toLong(), connectTimeout?.toLong(), header)
    }

    /**
     * 获取网络请求的header
     *
     * @return defaultSpName
     */
    protected abstract val header: Interceptor

    override fun appClose() {

    }

    override fun app2Foreground() {

    }

    override fun app2Background() {

    }

    fun finishOtherAndStartNewAc(excludeFinishAcClass: Class<*>, startNewAcClass: Class<*>, bundle: Bundle) {
        appActivityLifecycleImp?.finishOtherAndStartNewAc(excludeFinishAcClass, startNewAcClass, bundle)
    }

    fun finishAllAndStartNewAc(startNewAcClass: Class<*>) {
        appActivityLifecycleImp?.finishAllAndStartNewAc(startNewAcClass)
    }

    fun finishOther(onlyKeepAcClass: Class<*>) {
        appActivityLifecycleImp?.finishOther(onlyKeepAcClass)
    }

    /**
     * 是否已启动Ac
     */
    fun hadAcCreated(): Boolean? {
        return appActivityLifecycleImp?.hadAcCreated()
    }

    /**
     * 返回当前显示的Activity
     *
     * @return
     */
    fun findActivity(acClass: Class<*>): Activity? {
        return appActivityLifecycleImp?.findActivity(acClass)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            protected set

        val baseApplication: BaseApplication
            get() = context as BaseApplication
    }
}
