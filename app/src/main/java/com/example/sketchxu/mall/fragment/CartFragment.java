package com.example.sketchxu.mall.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.CreateOrderActivity;
import com.example.sketchxu.mall.activity.MainActivity;
import com.example.sketchxu.mall.activity.NewOrderActivity;
import com.example.sketchxu.mall.adapter.CartAdapter;
import com.example.sketchxu.mall.bean.ShoppingCart;
import com.example.sketchxu.mall.bean.User;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.example.sketchxu.mall.utils.CartProvider;
import com.example.sketchxu.mall.utils.ToastUtils;
import com.example.sketchxu.mall.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class CartFragment extends BaseFragment implements View.OnClickListener{

    public static final String TAG = "CartFragment";

    public static final int ACTION_EDIT = 1;

    public static final int ACTION_COMPLETE = 2;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    private CartAdapter mAdapter;
    private CartProvider mProvider;

    private CnToolbar mToolbar;

    @ViewInject(R.id.txt_total)
    private TextView textTotal;

    @ViewInject(R.id.checkbox_all)
    private CheckBox checkBox;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private OkHttpHelper mOkHttpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ViewUtils.inject(this, view);

        mProvider = new CartProvider(getContext());

        initData();

        return view;
    }

    private void initData() {
        List<ShoppingCart> carts = mProvider.getAll();
        if (carts == null) {
            carts = new ArrayList<>();
        }
        mAdapter = new CartAdapter(getContext(), carts, textTotal, checkBox);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void refreshData() {
        mAdapter.clearData();
        mAdapter.addData(mProvider.getAll());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.checkListen();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            mToolbar = activity.findViewById(R.id.toolbar);
            if (mToolbar != null) {
                changeToolbar();
            }
        }
    }

    public void changeToolbar() {
        if (mToolbar != null) {
            mToolbar.hideSearchView();
            mToolbar.setTitle(getString(R.string.title_cart));
            mToolbar.showRightButton();
            mToolbar.getRightButton().setText("编辑");
            mToolbar.getRightButton().setOnClickListener(this);
            mToolbar.getRightButton().setTag(ACTION_EDIT);
        }

    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();

        if (ACTION_EDIT == action) {
            showDelControl();
        } else if (ACTION_COMPLETE == action) {
            hideDelControl();
        }
    }

    @OnClick(R.id.btn_order)
    public void toOrder(View view) {
        List<ShoppingCart> carts = mProvider.getAll();
        if (carts == null || carts.size() == 0) {
            ToastUtils.show(getContext(), "购物车为空");
            return;
        }

        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
        startActivity(intent, true);
    }

    private void showDelControl() {
        mBtnDel.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.GONE);
        textTotal.setVisibility(View.GONE);
        mToolbar.getRightButton().setText("完成");
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);

        checkBox.setChecked(false);
        mAdapter.checkAll_None(false);
    }

    private void hideDelControl() {
        mBtnDel.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.VISIBLE);
        textTotal.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        checkBox.setChecked(true);
        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();

    }

    @OnClick(value={R.id.btn_del})
    private void delCart(View v) {
        mAdapter.delCart();
    }
}
