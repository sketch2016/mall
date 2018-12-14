package com.example.sketchxu.mall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.sketchxu.mall.R;

public class NewOrderActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView view = new TextView(this);
        view.setText("提交订单");

        setContentView(R.layout.activity_order);
    }
}
