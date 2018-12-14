package com.example.sketchxu.mall.http;

import android.content.Context;
import android.content.Intent;

import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.activity.LoginActivity;
import com.example.sketchxu.mall.utils.ToastUtils;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Response;

public abstract class SpotsCallback<T> extends BaseCallback<T> {

    private SpotsDialog dialog;

    private Context mContext;

    public SpotsCallback(Context mContext) {
        this.mContext = mContext;
        dialog = new SpotsDialog(mContext);
    }

    private void showDiaog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void dismissDiaog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void doRequestBefore() {
        showDiaog();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        dismissDiaog();
    }

    @Override
    public void onResponse(Call call, Response response) {
        dismissDiaog();
    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, "token错误:code=" + code);

        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyApplication.getInstance().clearUser();
    }
}
