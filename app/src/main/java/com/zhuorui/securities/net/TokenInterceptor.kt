package com.zhuorui.securities.net

import com.zhuorui.securities.TokenOverdueEvent
import com.zhuorui.securities.base2app.rxbus.RxBus
import com.zhuorui.securities.base2app.util.JsonUtil
import com.zhuorui.securities.personal.config.LocalAccountConfig
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import java.io.IOException
import java.nio.charset.Charset

/**
 * author : Pengxianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019-05-20 14:13
 * desc   : 在网络请求拦截器中判断token是否失效
 */
class TokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 获取网络返回Response
        val response = chain.proceed(request)
        val responseBody = response.body()
        val source = responseBody!!.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()
        var charset: Charset? = UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(UTF8)
        }
        // 拿到返回结果
        val bodyString = buffer.clone().readString(charset!!)
        val jsonObject = JsonUtil.toJSONObject(bodyString)
        try {
            // 判断token是否失效
            if (jsonObject != null && jsonObject.has("code") && jsonObject.getString("code") == "000102") {
                LocalAccountConfig.clear()
                RxBus.getDefault().post(TokenOverdueEvent())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return response
    }

    companion object {

        private val UTF8 = Charset.forName("UTF-8")
    }
}