package com.example.sketchxu.mall.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

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


    public BaseCallback()
    {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void doRequestBefore();

    public abstract void onFailure(Call call, IOException e);

    public abstract void onResponse(Call call, Response response);

    public abstract void onSuccess(Response response, T t);

    public abstract void onError(Response response, int code, Exception e);

    public abstract void onTokenError(Response response, int code);
}
