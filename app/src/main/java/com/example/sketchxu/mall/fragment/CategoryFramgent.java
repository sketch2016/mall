package com.example.sketchxu.mall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.activity.DetailActivity;
import com.example.sketchxu.mall.adapter.BaseAdapter;
import com.example.sketchxu.mall.adapter.HWAdapter;
import com.example.sketchxu.mall.adapter.WaresAdapter;
import com.example.sketchxu.mall.adapter.decoration.DividerGridItemDecoration;
import com.example.sketchxu.mall.bean.Banner;
import com.example.sketchxu.mall.bean.Category;
import com.example.sketchxu.mall.bean.CategoryAdapter;
import com.example.sketchxu.mall.bean.Page;
import com.example.sketchxu.mall.bean.Ware;
import com.example.sketchxu.mall.http.BaseCallback;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Console;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CategoryFramgent extends BaseFragment {

    private static final String TAG = "CategoryFramgent";

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerview_category;

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerview_wares;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private WaresAdapter mAdapter;

    private int curPage = 1;
    private int pageSize = 10;
    private int totalPage = 3;
    private int category_id = 0;

    private static final int STATE_NORMAL = 1;
    private static final int STATE_REFRESH = 2;
    private static final int STATE_LOAD_MORE = 3;
    private int state = STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        x.view().inject(this, view);

        initRefreshLayout();
        requestCategoryData();
        requestSliderData();

        return view;
    }

    private void requestCategoryData() {
        okHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallback<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategory(categories);

                if (categories != null && categories.size() > 0) {
                    category_id = categories.get(0).getId();

                    requestWares(category_id);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategory(final List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
        mRecyclerview_category.setAdapter(adapter);
        mRecyclerview_category.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview_category.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerview_category.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Category category = categories.get(pos);
                requestWares(category.getId());
                category_id = category.getId();

                curPage = 1;
                state = STATE_NORMAL;
            }
        });
    }

    private void requestSliderData() {

        okHttpHelper.get(Contants.API.BANNER, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> bannerList) {
                showSliderView(bannerList);
                Log.d(TAG, "onSuccess");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.d(TAG, "onError");
            }
        });
    }

    private void showSliderView(List<Banner> banners) {

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

                mSliderLayout.addSlider(textSliderView);
            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //mSliderShow.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Tablet);
        //mSliderShow.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setDuration(3000);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (curPage < totalPage) {
                    loadMore();
                } else {
                    //Toast.makeText(getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }

            }
        });
    }

    private void refresh() {
        curPage = 1;
        state = STATE_REFRESH;
        requestWares(category_id);
    }

    private void loadMore() {
        curPage++;
        state = STATE_LOAD_MORE;
        requestWares(category_id);
    }

    private void requestWares(int category_id) {
        String url = Contants.API.WARES_LIST + "?curPage=" + curPage + "&pageSize=" + pageSize + "&categoryId=" + category_id;
        okHttpHelper.get(url, new BaseCallback<Page<Ware>>() {
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
            public void onSuccess(Response response, Page<Ware> warePage) {
                showWares(warePage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void showWares(List<Ware> wares) {

        switch (state) {
            case STATE_NORMAL:
                if (mAdapter == null) {
                    mAdapter = new WaresAdapter(getContext(), wares);
                    mRecyclerview_wares.setAdapter(mAdapter);
                    mRecyclerview_wares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerview_wares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                    mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
                } else {
                    mAdapter.clearData();
                    mAdapter.addData(wares);
                    mRecyclerview_wares.setAdapter(mAdapter);
                    mRecyclerview_wares.scrollToPosition(0);
                }

                break;
            case STATE_REFRESH:
                mAdapter.clearData();
                mAdapter.addData(wares);
                mRecyclerview_wares.setAdapter(mAdapter);
                mRecyclerview_wares.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_LOAD_MORE:
                mAdapter.addData(mAdapter.getDatas().size(), wares);
                //mRecyclerview_wares.setAdapter(mAdapter);
                mRecyclerview_wares.scrollToPosition(mAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }


    }
}
