package com.example.sketchxu.mall.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import com.cjj.MaterialRefreshLayout;
import com.daimajia.slider.library.SliderLayout;
import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.adapter.HWAdapter;
import com.example.sketchxu.mall.adapter.decoration.DividerGridItemDecoration;
import com.example.sketchxu.mall.bean.Page;
import com.example.sketchxu.mall.bean.Ware;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.utils.Pager;
import com.example.sketchxu.mall.widget.CnToolbar;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class WareListActivity extends BaseActivity implements Pager.OnPageListener, View.OnClickListener {

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_PRICE = 1;
    public static final int TAG_SALES = 2;

    @ViewInject(R.id.toolbar)
    private CnToolbar toolbar;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.txt_summary)
    private TextView mTextSummary;

    private HWAdapter mAdapter;

    private int campaignId;

    private int orderBy = TAG_DEFAULT;

    private Pager pager;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_warelist);
        x.view().inject(this);

        initTabLayout();
        initToolbar();

        campaignId = getIntent().getIntExtra(Contants.CAMPAIGN_ID, 0);
        requestData();

    }

    private void requestData() {
        pager = Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIGN_LIST)
                .putParam("campaignId", campaignId)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setCanLoadMore(true)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Ware>>(){}.getType());

        pager.request();
    }

    private void initTabLayout() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALES);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                orderBy = (int) tab.getTag();
                pager.setParam("orderBy", orderBy);
                pager.request();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void load(List datas, int totalPage, int totalCount) {
        mTextSummary.setText("共有" + totalCount + "件商品");

        if (mAdapter == null) {
            mAdapter = new HWAdapter(this, datas);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.scrollToPosition(0);
        } else {
            mAdapter.clearData();
            mAdapter.addData(datas);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.scrollToPosition(0);
        }

    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mAdapter.clearData();
        mAdapter.addData(datas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mAdapter.addData(mAdapter.getDatas().size(), datas);
        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
    }

    private static final int ACTION_LIST = 1;
    private static final int ACTION_GRID = 2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setRightButtonIcon(getDrawable(R.mipmap.icon_grid_32));
        toolbar.setRightButtonOnClickListener(this);
        toolbar.getRightButton().setTag(ACTION_GRID);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (ACTION_LIST == action) {
            toolbar.setRightButtonIcon(getDrawable(R.mipmap.icon_grid_32));
            toolbar.getRightButton().setTag(ACTION_GRID);

            mAdapter.resetLayout(R.layout.hotware_item);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        } else if (ACTION_GRID == action) {
            toolbar.setRightButtonIcon(getDrawable(R.mipmap.icon_list_32));
            toolbar.getRightButton().setTag(ACTION_LIST);

            mAdapter.resetLayout(R.layout.template_grid_wares);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        }
    }
}
