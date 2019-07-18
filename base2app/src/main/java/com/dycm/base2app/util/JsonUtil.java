package com.dycm.base2app.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Json解析器
 */
public class JsonUtil {
    private JsonUtil() {
    }

    private static Gson mGson = new Gson();

    public static String toJson(Object model) {
        if (model == null) {
            return null;
        }
        return mGson.toJson(model);
    }

    public static <T> T fromJson(@NonNull String json, Class<T> t) {
//        if (TextUtils.isEmpty(json)) return null;
        try {
            return mGson.fromJson(json, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(@NonNull String json, Type typeOfT) {
        if (TextUtils.isEmpty(json)) return null;

        try {
            return mGson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
