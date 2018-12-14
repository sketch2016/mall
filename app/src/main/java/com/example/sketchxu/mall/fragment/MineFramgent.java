package com.example.sketchxu.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.AddressListActivity;
import com.example.sketchxu.mall.activity.LoginActivity;
import com.example.sketchxu.mall.bean.User;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

public class MineFramgent extends BaseFragment {

    @ViewInject(R.id.img_head)
    private ImageView mHead;

    @ViewInject(R.id.txt_username)
    private TextView mUsername;

    @ViewInject(R.id.btn_logout)
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        showUser();
    }

    @OnClick({R.id.img_head, R.id.txt_username})
    private void login(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);

        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @OnClick(R.id.btn_logout)
    private void toLogout(View view) {
        MyApplication.getInstance().clearUser();
        showUser();
    }

    @OnClick(R.id.myaddr)
    private void toAddr(View view) {
        Intent intent = new Intent(getActivity(), AddressListActivity.class);

        startActivity(intent, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        showUser();
    }

    public void showUser() {
        User user = MyApplication.getInstance().getUser();

        if (user != null) {
            mUsername.setText(user.getUsername());
            Picasso.with(getContext()).load(user.getLogo_url()).into(mHead);

            btnLogout.setVisibility(View.VISIBLE);
        } else {
            mHead.setImageResource(R.mipmap.default_head);
            mUsername.setText("点击登陆");
            btnLogout.setVisibility(View.GONE);
        }
    }
}
