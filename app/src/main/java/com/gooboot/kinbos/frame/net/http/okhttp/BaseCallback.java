package com.gooboot.kinbos.frame.net.http.okhttp;

import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.internal.$Gson$Types;

/**
 * Created by gooboot on 2017/7/25.
 */

public abstract class BaseCallback<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback(){
        mType = getSuperclassTypeParameter(getClass());
    }

    /**
     * 在服务器未返回结果前做相应处理，如弹出进度条
     * @param request
     */
    public abstract void onBeforeRequest(Request request);

    /**
     * 请求失败时，被OkHttpClient的回调方法onFailure调用
     * @param request
     * @param e
     */
    public abstract void onFailrue(Request request,Exception e);

    /**
     * 当状态码大于200，小于300 时调用此方法
     * @param response
     * @param t
     */
    public abstract void onSuccess(Response response,T t);

    /**
     * 当状态码400，404，403，500等时调用此方法
     * @param response
     * @param httpCode
     * @param e
     */
    public abstract void onError(Response response,int httpCode,Exception e);
}
