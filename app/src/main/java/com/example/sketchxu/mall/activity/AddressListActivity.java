package com.example.sketchxu.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.adapter.AddressAdapter;
import com.example.sketchxu.mall.bean.Address;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.example.sketchxu.mall.msg.BaseMessage;
import com.example.sketchxu.mall.utils.ToastUtils;
import com.example.sketchxu.mall.widget.CnToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


public class AddressListActivity extends BaseActivity {

    public static final String TAG = "AddressListActivity";

    public static final int ACTION_CREATE = 0;
    public static final int ACTION_EDIT = 1;


    @ViewInject(R.id.toolbar)
    private CnToolbar mToolBar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;

    private AddressAdapter mAdapter;


    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ViewUtils.inject(this);

        initToolbar();

    }

    @Override
    protected void onResume() {
        super.onResume();

        initAddress();
    }

    private void initToolbar(){

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddActivity(ACTION_CREATE, null);
            }
        });

    }

    private void toAddActivity(int action, Address address) {

        Intent intent = new Intent(this,AddressAddActivity.class);
        intent.putExtra("action", action);
        intent.putExtra("size", mAdapter.getDatas().size());
        if (address != null) {
            intent.putExtra("address", address);
        }
        startActivityForResult(intent,Contants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();

    }

    private void initAddress(){

        Log.d(TAG, "initAddress", new Exception("initAddress"));

        String url = Contants.API.ADDRESS_LIST + "?user_id=" + MyApplication.getInstance().getUser().getId();

        mHttpHelper.get(url, new SpotsCallback<List<Address>>(this) {


            @Override
            public void onSuccess(Response response, List<Address> addresses) {
                showAddress(addresses);
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


    private void showAddress(List<Address> addresses) {

        Log.d(TAG, "showAddress:addresses.size=" + addresses.size(), new Exception("showAddress"));

        Collections.sort(addresses);
        if(mAdapter ==null) {
            mAdapter = new AddressAdapter(this, addresses, new AddressAdapter.AddressLisneter() {
                @Override
                public void setDefault(Address address) {

                    updateAddress(address);

                }

                @Override
                public void onEdit(Address address) {
                    toAddActivity(ACTION_EDIT, address);
                }

                @Override
                public void onDel(Address address) {
                    deleteAddr(address.getId());
                }
            });
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }
        else{
            mAdapter.clearData();
            mAdapter.addData(addresses);
            mRecyclerview.setAdapter(mAdapter);
        }

    }

    private void deleteAddr(Long id) {
        String url = Contants.API.ADDRESS_DELETE + "?id=" + id;
        mHttpHelper.get(url, new SpotsCallback<BaseMessage>(this) {

            @Override
            public void onSuccess(Response response, BaseMessage baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseMessage.STATUS_SUCCESS){
                    //
                    ToastUtils.show(AddressListActivity.this, "删除成功");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    public void updateAddress(Address address){

        Map<String,String> params = new HashMap<>(1);
        params.put("id",address.getId() + "");
        params.put("consignee",address.getConsignee());
        params.put("phone",address.getPhone());
        params.put("addr",address.getAddr());
        params.put("zip_code",address.getZipCode());
        params.put("is_default",address.getIsDefault() ? "true" : "false");

        mHttpHelper.post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallback<BaseMessage>(this) {

            @Override
            public void onSuccess(Response response, BaseMessage baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseMessage.STATUS_SUCCESS){

                    initAddress();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }






}
