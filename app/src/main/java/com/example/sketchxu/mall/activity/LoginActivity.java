package com.example.sketchxu.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.User;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.example.sketchxu.mall.msg.LoginMessage;
import com.example.sketchxu.mall.utils.DESUtil;
import com.example.sketchxu.mall.utils.ToastUtils;
import com.example.sketchxu.mall.widget.ClearEditText;
import com.example.sketchxu.mall.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private CnToolbar toolbar;

    @ViewInject(R.id.text_phone)
    private ClearEditText mPhone;

    @ViewInject(R.id.text_pwd)
    private ClearEditText mPassword;

    private OkHttpHelper mOkHttpHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mOkHttpHelper = OkHttpHelper.getInstance();

        ViewUtils.inject(this);

        initToolbar();
    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_login)
    private void toLogin(View view) {
        String phone = mPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }
        String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }

        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, password));

        mOkHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallback<LoginMessage<User>>(this) {
            @Override
            public void onSuccess(Response response, LoginMessage<User> userLoginMessage) {
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginMessage.getData(), userLoginMessage.getToken());
                setResult(RESULT_OK);

                Intent intent = application.getIntent();
                if (intent != null) {
                    application.jumptToTargetActivity(LoginActivity.this);
                }

                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);

            }
        });
    }

    @OnClick(R.id.txt_toReg)
    private void toReg(View view) {
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
    }
}
