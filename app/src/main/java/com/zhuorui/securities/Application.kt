package com.zhuorui.securities

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import com.zhuorui.securities.base2app.BaseApplication
import com.zhuorui.securities.custom.view.ClassicsFooter
import com.zhuorui.securities.custom.view.ClassicsHeader
import com.zhuorui.securities.net.HeaderInterceptor
import com.zhuorui.securities.net.TokenInterceptor
import com.zhuorui.securities.personal.util.MultiLanguageUtil
import okhttp3.Interceptor

/**
 *    author : Pengxianglin
 *    e-mail : peng_xianglin@163.com
 *    date   : 2019-05-20 14:13
 *    desc   :
 */
class Application : BaseApplication() {

    override val logTag: String
        get() = this.javaClass.simpleName

    override val defaultSpName: String
        get() = packageName

    override val header: Interceptor
        get() = HeaderInterceptor()

    override val checkToken: Interceptor
        get() = TokenInterceptor()

    override fun beforeInit() {
        // TODO 在App需要初始化的东西写在这里

        // 必须在主线程中初始化bugly，在测试阶段设置成true，发布时设置为false
        CrashReport.initCrashReport(this, "e77c9add36", BuildConfig.DEBUG)
    }

    override fun afterInit() {
        MultiLanguageUtil.init(this)

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader.REFRESH_HEADER_PULLING = "下拉刷新"
            ClassicsHeader.REFRESH_HEADER_REFRESHING = "加载中"
            ClassicsHeader.REFRESH_HEADER_RELEASE = "释放刷新"
            ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成"
            ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败"

            //指定为经典Header，默认是 贝塞尔雷达Header
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载..."
            ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成"
            ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败"
            ClassicsFooter.REFRESH_FOOTER_NOTHING = "-已全部加载完毕-"

            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context)
        }
    }
}