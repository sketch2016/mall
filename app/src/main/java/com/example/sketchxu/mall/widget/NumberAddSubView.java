package com.example.sketchxu.mall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sketchxu.mall.R;

public class NumberAddSubView extends LinearLayout implements View.OnClickListener{

    public static final String TAG = "NumberAddSubView";

    private Button mBtnAdd;
    private Button mBtnSub;
    private TextView mTextNum;

    private OnButtonOnClickListener mOnButtonOnClickListener;

    private LayoutInflater mInflator;

    public void setOnButtonOnClickListener(OnButtonOnClickListener listener) {
        mOnButtonOnClickListener = listener;
    }

    private int value;

    public int getValue() {
        String val = mTextNum.getText().toString();
        if (!TextUtils.isEmpty(val)) {
            value = Integer.parseInt(val);
        }
        return value;
    }

    public void setValue(int value) {
        mTextNum.setText(value + "");
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    private int maxValue = 10;
    private int minValue;


    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflator = LayoutInflater.from(context);
        initView();

        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(this.getContext(), attrs, R.styleable.NumberAddSubView, defStyleAttr, 0);
            int value = a.getInt(R.styleable.NumberAddSubView_value, 0);
            setValue(value);

            int minValue = a.getInt(R.styleable.NumberAddSubView_minValue, 0);
            setMinValue(minValue);

            int maxValue = a.getInt(R.styleable.NumberAddSubView_maxValue, 0);
            setMaxValue(maxValue);

            Drawable btnAddbg = a.getDrawable(R.styleable.NumberAddSubView_btnAddBackgroud);
            Drawable btnSubbg = a.getDrawable(R.styleable.NumberAddSubView_btnSubBackgroud);
            Drawable numAbg = a.getDrawable(R.styleable.NumberAddSubView_numBackgroud);
            setBtnAddBackgroud(btnAddbg);
            setBtnSubBackgroud(btnSubbg);
            setNumBackgroud(numAbg);

            a.recycle();
        }
    }

    private void setBtnSubBackgroud(Drawable btnSubbg) {
        if (btnSubbg != null) {
            mBtnAdd.setBackgroundDrawable(btnSubbg);

        }
    }

    private void setBtnAddBackgroud(Drawable btnAddbg) {
        if (btnAddbg != null) {
            mBtnSub.setBackgroundDrawable(btnAddbg);

        }
    }

    private void setNumBackgroud(Drawable numAbg) {
        if (numAbg != null) {
            mTextNum.setBackgroundDrawable(numAbg);

        }
    }

    private void initView() {
        View view = mInflator.inflate(R.layout.layout_number_add_sub, this, true);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnSub = findViewById(R.id.btn_sub);
        mTextNum = findViewById(R.id.text_num);

        mBtnAdd.setOnClickListener(this);
        mBtnSub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addNumber();
                if (mOnButtonOnClickListener != null) {
                    mOnButtonOnClickListener.onButtonAddClick(v, value);
                }

                break;
            case R.id.btn_sub:
                subNumber();
                if (mOnButtonOnClickListener != null) {
                    mOnButtonOnClickListener.onButtonSubClick(v, value);
                }

                break;

        }
    }

    private void addNumber() {
        if (value < maxValue) {
            value += 1;
        }

        mTextNum.setText(value + "");
        Log.d(TAG, "addNumber:value = " + value);
    }

    private void subNumber() {
        if (value > minValue) {
            value -= 1;
        }

        mTextNum.setText(value + "");
        Log.d(TAG, "subNumber:value = " + value);
    }

    public interface OnButtonOnClickListener {
        void onButtonAddClick(View v, int value);
        void onButtonSubClick(View v, int value);
    }
}
