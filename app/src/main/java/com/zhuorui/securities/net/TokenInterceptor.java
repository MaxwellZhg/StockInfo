package com.zhuorui.securities.net;

import com.zhuorui.securities.TokenOverdueEvent;
import com.zhuorui.securities.base2app.rxbus.RxBus;
import com.zhuorui.securities.base2app.util.JsonUtil;
import com.zhuorui.securities.personal.config.LocalAccountConfig;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * author : Pengxianglin
 * e-mail : peng_xianglin@163.com
 * date   : 2019-05-20 14:13
 * desc   : 在网络请求拦截器中判断token是否失效
 */
public class TokenInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // 获取网络返回Response
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        // 拿到返回结果
        String bodyString = buffer.clone().readString(charset);
        JSONObject jsonObject = JsonUtil.toJSONObject(bodyString);
        try {
            // 判断token是否失效
            if (jsonObject != null && jsonObject.has("code") && jsonObject.getString("code").equals("000102")) {
                LocalAccountConfig.Companion.clear();
                RxBus.getDefault().post(new TokenOverdueEvent());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}