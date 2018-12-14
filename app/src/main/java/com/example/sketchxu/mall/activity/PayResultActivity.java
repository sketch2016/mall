package com.example.sketchxu.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sketchxu.mall.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PayResultActivity extends BaseActivity {

    @ViewInject(R.id.img_status)
    private ImageView imgStatus;

    @ViewInject(R.id.txt_status)
    private TextView txtStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);

        ViewUtils.inject(this);
        showResult(getIntent());
    }

    private void showResult(Intent intent) {
        int status = intent.getIntExtra("status", -1);

        if (status == 1) {
            imgStatus.setImageResource(R.mipmap.icon_success_128);
            txtStatus.setText("支付成功");
        } else if (status == 2) {
            imgStatus.setImageResource(R.mipmap.icon_cancel_128);
            txtStatus.setText("支付失败");
        }
    }


    @Override
    public void onBackPressed() {

    }

    private void toIndex() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
