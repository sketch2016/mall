package com.example.sketchxu.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.User;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.example.sketchxu.mall.msg.LoginMessage;
import com.example.sketchxu.mall.utils.CountTimeView;
import com.example.sketchxu.mall.utils.ToastUtils;
import com.example.sketchxu.mall.widget.ClearEditText;
import com.example.sketchxu.mall.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

public class RegSecondActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private CnToolbar toolbar;

    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;

    @ViewInject(R.id.edittxt_code)
    private ClearEditText mCode;

    @ViewInject(R.id.btn_reSend)
    private Button mSend;

    private OkHttpHelper helper;

    private String countryCode;

    private String phone;

    private String pwd;

    private SMSEventHandler handler;

    //private SpotsDialog dialog;

    public RegSecondActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        ViewUtils.inject(this);

        initToolbar();

        helper = OkHttpHelper.getInstance();
        Intent intent = getIntent();
        countryCode = intent.getStringExtra("countryCode");
        phone = intent.getStringExtra("phone");
        pwd = intent.getStringExtra("pwd");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);

        String text = getString(R.string.smssdk_send_mobile_detail)+formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));

        CountTimeView timer = new CountTimeView(mSend);
        timer.start();

        handler = new SMSEventHandler();
        SMSSDK.registerEventHandler(handler);

        //dialog = new SpotsDialog(this, "正在校验验证码");
        //dialog.setMessage("正在校验验证码");
    }

    /** 分割电话号码 */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }

    private void initToolbar() {
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitCode() {
        String vCode = mCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, "验证码不能为空");
            return;
        }

        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
        //dialog.show();
    }

    @OnClick(R.id.btn_reSend)
    private void reSendCode(View view) {
        SMSSDK.getVerificationCode("+" + countryCode, phone);

        //dialog.setMessage("正在重新获取验证码");
        //dialog.show();
    }

    class SMSEventHandler extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //if (dialog != null && dialog.isShowing()) {
                    //    dialog.dismiss();
                    //}

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            doReg();
                        }

                    } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegSecondActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }

                }
            });
        }
    }

    private void doReg() {
        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", pwd);

        //dialog.setMessage("正在提交注册信息");
        //dialog.show();
        helper.post(Contants.API.REG, params, new SpotsCallback<LoginMessage<User>>(this) {
            @Override
            public void onSuccess(Response response, LoginMessage<User> loginMessage) {
                //if (dialog != null && dialog.isShowing()) {
                //    dialog.dismiss();
                //}

                if(loginMessage.getStatus()==loginMessage.STATUS_ERROR){
                    ToastUtils.show(RegSecondActivity.this,"注册失败:"+loginMessage.getMsg());
                    return;
                }
                MyApplication application = MyApplication.getInstance();
                application.putUser(loginMessage.getData(), loginMessage.getToken());

                Intent intent = new Intent(RegSecondActivity.this, MainActivity.class);
                startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSSDK.unregisterEventHandler(handler);
    }
}
