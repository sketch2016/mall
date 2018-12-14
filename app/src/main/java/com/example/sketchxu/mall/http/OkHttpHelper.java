package com.example.sketchxu.mall.http;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.util.Log;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHelper {

    private static final String TAG = "OkHttpHelper";

    public static final int TOKEN_ERROR = 401;
    public static final int TOKEN_EXPIRE = 402;
    public static final int TOKEN_MISSING = 403;

    private static OkHttpHelper mInstance;
    private OkHttpClient client;

    private Gson mGson;

    private Handler mHandler;

    private OkHttpHelper() {
        client = new OkHttpClient();

        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized OkHttpHelper getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpHelper();
        }

        return mInstance;
    }

    public void get(String url, BaseCallback callback) {
        Handler mHandler = new Handler();
        mHandler.sendEmptyMessage(0);

        Request request = buildRequest(url, null, HttpMethodType.GET);

        doRequest(request, callback);
    }

    public void post(String url, Map<String, String> params, BaseCallback callback) {
        Request request = buildRequest(url, params, HttpMethodType.POST);

        doRequest(request, callback);
    }

    private Request buildRequest(String url, Map<String,String> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();

        if (methodType == HttpMethodType.GET) {
            String token = MyApplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token)) {
                int index = url.indexOf("?");
                if (index >= 0) {
                    url += "&" + Contants.TOKEN + "=" + token;
                } else {
                    url += "?" + Contants.TOKEN + "=" + token;
                }
            }

            builder.url(url);
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            builder.url(url);
            builder.post(buildFormData(params));
        }

        Log.d(TAG, "buildRequest:url = " + url);

        return builder.build();
    }

    private RequestBody buildFormData(Map<String,String> params) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String,String> entity : params.entrySet()) {
            bodyBuilder.add(entity.getKey(), entity.getValue());
        }

        String token = MyApplication.getInstance().getToken();
        if (token != null) {
            bodyBuilder.add(Contants.TOKEN, token);
        }

        return bodyBuilder.build();
    }

    private void doRequest(final Request request, final BaseCallback callback) {

        callback.doRequestBefore();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                callback.onResponse(call, response);
                Log.d(TAG, "onResponse:code = " + response.code());
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.d(TAG, "onResponse:json = " + json);
                    if (callback.mType == String.class) {
                        callbackSuccess(callback,response,json);
                    } else {
                        try {
                            Object obj = mGson.fromJson(json, callback.mType);
                            callbackSuccess(callback,response,obj);
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                            callbackError(callback,response,null);
                        }

                    }
                } else if (response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE || response.code() == TOKEN_MISSING){
                    callbackTokenError(callback,response);
                } else {
                    callbackError(callback,response,null);
                }

            }
        });

    }

    private void callbackTokenError(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());

            }
        });
    }


    private void callbackError(final BaseCallback callback, final Response response, Object o) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), null);

            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });

    }


    enum HttpMethodType {
        GET,
        POST
    }
}
