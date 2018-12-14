package com.example.sketchxu.mall.fragment;

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
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.DetailActivity;
import com.example.sketchxu.mall.adapter.BaseAdapter;
import com.example.sketchxu.mall.adapter.HWAdapter;
import com.example.sketchxu.mall.adapter.HomeAdapter;
import com.example.sketchxu.mall.adapter.HotWareAdapter;
import com.example.sketchxu.mall.bean.Banner;
import com.example.sketchxu.mall.bean.HomeCampaign;
import com.example.sketchxu.mall.bean.Page;
import com.example.sketchxu.mall.bean.Ware;
import com.example.sketchxu.mall.http.BaseCallback;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.example.sketchxu.mall.utils.Pager;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class HotFragment extends BaseFragment {

    private static final String TAG = "HotFragment";

    @ViewInject(R.id.refresh)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private HWAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        x.view().inject(this, view);

        Pager pager = Pager.newBuilder().setCanLoadMore(true)
                .setPageSize(10)
                .setRefreshLayout(mRefreshLayout)
                .setUrl(Contants.API.HOT_WARES)
                .setOnPageListener(new Pager.OnPageListener<Ware>() {
                    @Override
                    public void load(List<Ware> datas, int totalPage, int totalCount) {
                        mAdapter = new HWAdapter(getContext(), datas);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Intent intent = new Intent(getActivity(), DetailActivity.class);
                                Ware ware = mAdapter.getItem(pos);
                                intent.putExtra(Contants.WARE, ware);
                                startActivity(intent, false);
                            }
                        });
                    }

                    @Override
                    public void refresh(List<Ware> datas, int totalPage, int totalCount) {
                        mAdapter.clearData();
                        mAdapter.addData(datas);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void loadMore(List<Ware> datas, int totalPage, int totalCount) {
                        mAdapter.addData(mAdapter.getDatas().size(), datas);
                        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                    }
                }).build(getContext(), new TypeToken<Page<Ware>>(){}.getType());

        pager.request();

        return view;
    }

}
