package com.example.sketchxu.mall;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.sketchxu.mall.bean.User;
import com.example.sketchxu.mall.utils.UserLocalData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.mob.MobSDK;

public class MyApplication extends Application {

    private User user;

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initUser();

        Fresco.initialize(this);
        MobSDK.init(this);
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    private void initUser() {
        user = UserLocalData.getUser(this);
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return UserLocalData.getToken(this);
    }

    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    public void clearUser() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    private Intent intent;
    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return intent;
    }

    public void jumptToTargetActivity(Context context) {
        context.startActivity(intent);
        intent = null;
    }
}
