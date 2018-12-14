package com.example.sketchxu.mall.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.example.sketchxu.mall.R;

public class CountTimeView extends CountDownTimer {

    public static final long COUNT_TIME = 61000;//防止从59开始
    public static final long INTERVAL = 1000;

    private TextView btn;
    private int endStrRid;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountTimeView(long millisInFuture, long countDownInterval, TextView btn, int endStrRid) {
        super(millisInFuture, countDownInterval);

        this.btn = btn;
        this.endStrRid = endStrRid;

    }

    public CountTimeView(TextView btn, int endStrRid) {
        super(COUNT_TIME, INTERVAL);
        this.btn = btn;
        this.endStrRid = endStrRid;

    }

    public CountTimeView(TextView btn) {
        super(COUNT_TIME, INTERVAL);
        this.btn = btn;
        this.endStrRid = R.string.smssdk_resend_identify_code;

    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (btn != null) {
            btn.setText(millisUntilFinished / 1000 + "秒后可重新发送");
            btn.setEnabled(false);
        }

    }

    @Override
    public void onFinish() {
        if (btn != null) {
            btn.setText(endStrRid);
            btn.setEnabled(true);
        }

    }
}
