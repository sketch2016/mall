package com.example.sketchxu.mall.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.MainActivity;

public class CnToolbar extends Toolbar {

    public static final String TAG = "CnToolbar";

    private LayoutInflater mInflater;
    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightImageButton;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public CnToolbar(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public CnToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")
    public CnToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Log.d(TAG, "attrs = " + attrs);
        initView();
        setContentInsetsRelative(10, 20);

        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(this.getContext(), attrs, R.styleable.CnToolbar, defStyleAttr, 0);
            Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchview, false);
            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
            }

            String rightButtonText = a.getString(R.styleable.CnToolbar_rightButtonText);
            setRightButtonText(rightButtonText);

            a.recycle();

        }
    }

    private void setRightButtonText(String rightButtonText) {
        if (mRightImageButton != null) {
            mRightImageButton.setText(rightButtonText);
            mRightImageButton.setVisibility(View.VISIBLE);
        }
    }


    private void initView() {

        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());

            mView = mInflater.inflate(R.layout.toolbar, null);
            mTextTitle = mView.findViewById(R.id.toolbar_title);
            mSearchView = mView.findViewById(R.id.toolbar_searchview);
            mRightImageButton = mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, params);
        }

    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();

        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon) {
        if (mRightImageButton != null) {
            mRightImageButton.setBackgroundDrawable(icon);
            mRightImageButton.setVisibility(View.VISIBLE);
        }
    }

    public void setRightButtonOnClickListener(View.OnClickListener listener) {
        if (mRightImageButton != null) {
            mRightImageButton.setOnClickListener(listener);
        }
    }

    public Button getRightButton() {
        return mRightImageButton;
    }

    public void showRightButton() {
        if (mRightImageButton != null) {
            mRightImageButton.setVisibility(View.VISIBLE);
        }
    }

    public void hideRightButton() {
        if (mRightImageButton != null) {
            mRightImageButton.setVisibility(View.GONE);
        }
    }

    public void showSearchView() {
        if (mSearchView != null) {
            mSearchView.setVisibility(View.VISIBLE);
        }
        //mRightImageButton.setVisibility(View.VISIBLE);
    }

    public void hideSearchView() {
        if (mSearchView != null) {
            mSearchView.setVisibility(View.GONE);
        }
        //mRightImageButton.setVisibility(View.GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null) {
            mTextTitle.setVisibility(View.VISIBLE);
        }
        if (mRightImageButton != null) {
            mRightImageButton.setVisibility(View.VISIBLE);
        }
    }

    public void hideTitleView() {
        if (mTextTitle != null) {
            mTextTitle.setVisibility(View.GONE);
        }
        if (mRightImageButton != null) {
            mRightImageButton.setVisibility(View.GONE);
        }
    }
}
