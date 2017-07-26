package com.gooboot.kinbos.frame.net.http.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gooboot on 2017/7/25.
 */

public class OkHttpHelper {
    public static final String TAG = "--OkHttpHelper--";
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private Handler mHandler;
    private static OkHttpHelper mInstance;
    enum HttpMethodType{
        GET,
        POST
    }

    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper(){
        this.mHttpClient = new OkHttpClient();
        this.mGson = new Gson();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance(){
        return mInstance;
    }

    /**
     * http的Get请求方法
     * @param url 请求地址
     * @param callback
     */
    public void get(String url,BaseCallback callback){
        Request request = buildGetRequest(url);
        this.doRequest(request,callback);
    }

    /**
     * http的Post请求方法
     * @param url 请求地址
     * @param params 请求参数
     * @param callback
     */
    public void post(String url,Map<String,String> params,BaseCallback callback){
        Request request = buildPostRequest(url,params);
        this.doRequest(request,callback);
    }

    /**
     * 向服务器发起请求
     * @param request
     * @param callback
     */
    public void doRequest(final Request request,final BaseCallback callback){
        callback.onBeforeRequest(request);
        this.mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(callback,request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String resultStr = response.body().string();
                    //如果返回结果为String类型则直接返回，不用再做Json解析操作
                    if (String.class == callback.mType){
                        callbackSuccess(callback,response,resultStr);
                    }else {
                        try {
                            //根据返回结果类型，做Json解析后返回对应结果类型
                            Object obj = mGson.fromJson(resultStr,callback.mType);
                            callbackSuccess(callback,response,obj);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            callback.onError(response,response.code(),e);
                        }
                    }
                }else {
                    callbackError(callback,response,null);
                }
            }
        });
    }

    /**
     * 为Get方法构建Request对象
     * @param url
     * @return
     */
    private Request buildGetRequest(String url){
        return buildRequest(url,HttpMethodType.GET,null);
    }

    /**
     * 构建Post方法的Request对象
     * @param url 请求地址
     * @param params 请求参数
     * @return 返回Request对象
     */
    private Request buildPostRequest(String url,Map<String,String> params){
        return buildRequest(url,HttpMethodType.POST,params);
    }

    /**
     * 为Get方法和Post方法构建Request对象
     * @param url 数据请求地址
     * @param methodType 请求方法类型
     * @param params 请求参数
     * @return 返回Request对象
     */
    private Request buildRequest(String url, HttpMethodType methodType, Map<String,String> params){
        Request.Builder builder = new Request.Builder().url(url);
        if (HttpMethodType.GET == methodType){
            builder.get();
        }else if (HttpMethodType.POST == methodType){
            RequestBody body = this.buildFormData(params);
            builder.post(body);
        }

        return builder.build();
    }

    /**
     * 构建Post方法的表单数据
     * @param params 请求参数
     * @return
     */
    private RequestBody buildFormData(Map<String,String> params){
        FormBody.Builder builder = new FormBody.Builder();
        if (null != params){
            for (Map.Entry<String,String> entry:params.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        return builder.build();
    }

    /**
     * 当请求失败时调用
     * @param callback
     * @param request
     * @param e
     */
    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e){
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailrue(request,e);
            }
        });
    }

    /**
     * 当响应码大于200且小于300时调用
     * @param callack
     * @param response
     * @param obj
     */
    private void callbackSuccess(final BaseCallback callack, final Response response, final Object obj){
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                callack.onSuccess(response,obj);
            }
        });
    }

    /**
     * 当请求失败，状态码400，404，403，500等时调用此方法
     * @param callback
     * @param response
     * @param e
     */
    private void callbackError(final BaseCallback callback, final Response response, final Exception e){
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);
            }
        });
    }
}
