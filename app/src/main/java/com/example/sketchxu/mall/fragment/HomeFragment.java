package com.example.sketchxu.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.WareListActivity;
import com.example.sketchxu.mall.adapter.HomeAdapter;
import com.example.sketchxu.mall.adapter.MyAdapter;
import com.example.sketchxu.mall.bean.Banner;
import com.example.sketchxu.mall.bean.HomeCampaign;
import com.example.sketchxu.mall.bean.HomeCategory;
import com.example.sketchxu.mall.http.BaseCallback;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderShow;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private OkHttpHelper okHttpHelper;

    private HomeAdapter mAdapter;

    private List<Banner> banners;

    private Gson mGson;

    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        x.view().inject(this, view);

        mGson = new Gson();

        okHttpHelper = OkHttpHelper.getInstance();

        initData();

        requestImage();

        mHandler = new Handler(Looper.getMainLooper());
        return view;
    }




    private void initData() {
        okHttpHelper.get(Contants.API.HOMECAMPAIGN, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void doRequestBefore() {

            }

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                showData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }

    private void showData(final List<HomeCampaign> homeCampaigns) {
        mAdapter = new HomeAdapter(homeCampaigns, getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int pos, String info) {
                int campaignId = homeCampaigns.get(pos).getId();
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.CAMPAIGN_ID, campaignId);

                startActivity(intent, false);
            }
        });
    }

    private void initSlider() {

        if (banners != null) {
            for (Banner banner : banners) {
                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView.description(banner.getName());
                textSliderView.image(banner.getImgUrl());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                final String title = banner.getName();
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
                    }
                });

                mSliderShow.addSlider(textSliderView);
            }
        }

        mSliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //mSliderShow.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mSliderShow.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        //mSliderShow.setCustomAnimation(new DescriptionAnimation());
        mSliderShow.setDuration(3000);
    }

    private void requestImage() {
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("type", "1")
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
        okHttpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> bannerList) {
                banners = bannerList;
                initSlider();
                Log.d(TAG, "onSuccess");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.d(TAG, "onError");
            }
        });


    }
}
