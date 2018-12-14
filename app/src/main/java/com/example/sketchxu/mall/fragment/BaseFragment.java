package com.example.sketchxu.mall.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.activity.LoginActivity;
import com.example.sketchxu.mall.bean.User;

public class BaseFragment extends Fragment {

    public void startActivity(Intent intent, boolean isNeedLogin) {
        if (isNeedLogin) {
            User user = MyApplication.getInstance().getUser();
            if (user != null) {
                super.startActivity(intent);
            } else {
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(loginIntent);
            }

        } else {
            super.startActivity(intent);
        }

    }


    public void startActivityForResult(Intent intent, int requestCode, boolean isNeedLogin) {
        if (isNeedLogin) {
            User user = MyApplication.getInstance().getUser();
            if (user != null) {
                super.startActivityForResult(intent, requestCode);
            } else {
                Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                MyApplication.getInstance().putIntent(intent);
                startActivity(loginIntent);
            }

        } else {
            super.startActivityForResult(intent, requestCode);
        }

    }
}
