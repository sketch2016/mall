package com.example.sketchxu.mall.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.Tab;
import com.example.sketchxu.mall.fragment.CartFragment;
import com.example.sketchxu.mall.fragment.CategoryFramgent;
import com.example.sketchxu.mall.fragment.HomeFragment;
import com.example.sketchxu.mall.fragment.HotFragment;
import com.example.sketchxu.mall.fragment.MineFramgent;
import com.example.sketchxu.mall.widget.CnToolbar;
import com.example.sketchxu.mall.widget.FragmentTabHost;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    FragmentTabHost mTabHost;
    LayoutInflater mInflator;

    @ViewInject(R.id.toolbar)
    private CnToolbar toolbar;

    public static final int TAB_COUNT = 5;

    List<Tab> tabs = new ArrayList<>(TAB_COUNT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);

        initToolbar();
        initTab();

        Log.d(TAG, "address of object:" + new Object().toString());
    }

    private void initTab() {
        Tab mTabHome = new Tab(R.string.title_home, R.drawable.selector_icon_home, HomeFragment.class);
        Tab mTabHot = new Tab(R.string.title_hot, R.drawable.selector_icon_hot, HotFragment.class);
        Tab mTabCart = new Tab(R.string.title_cart, R.drawable.selector_icon_cart, CartFragment.class);
        Tab mTabCategory = new Tab(R.string.title_category, R.drawable.selector_icon_category, CategoryFramgent.class);
        Tab mTabMine = new Tab(R.string.title_mine, R.drawable.selector_icon_mine, MineFramgent.class);

        tabs.add(mTabHome);
        tabs.add(mTabHot);
        tabs.add(mTabCategory);
        tabs.add(mTabCart);
        tabs.add(mTabMine);

        mInflator = LayoutInflater.from(this);
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realcontent);


        for (Tab tab : tabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tabId);
                if (fragment != null && fragment instanceof CartFragment) {
                    CartFragment cartFragment = (CartFragment) fragment;
                    cartFragment.refreshData();
                    cartFragment.changeToolbar();
                } else {
                    toolbar.showSearchView();
                    toolbar.hideTitleView();
                    toolbar.hideRightButton();
                    if (fragment != null && fragment instanceof MineFramgent) {
                        MineFramgent cartFragment = (MineFramgent) fragment;
                        cartFragment.showUser();
                    }
                }
            }
        });

    }

    private View buildIndicator(Tab tab) {
        View view = mInflator.inflate(R.layout.tab_indicator, null);
        ImageView img = view.findViewById(R.id.icon_tab);
        TextView title = view.findViewById(R.id.txt_indicator);

        img.setImageDrawable(getDrawable(tab.getIcon()));
        title.setText(tab.getTitle());

        return view;
    }

    private void initToolbar() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        toolbar.inflateMenu(R.menu.menu_home);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.add:
//                        mAdapter.addData(0, "hehe");
//                        break;
//                    case R.id.remove:
//                        mAdapter.removeData(0);
//                        break;
//                    case R.id.listview:
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                        break;
//                    case R.id.gridview:
//                        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
//                        break;
//                    case R.id.horizontal_gridview:
//                        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
//                        break;
//                }
//                mRecyclerView.setAdapter(mAdapter);
//                return true;
//            }
//        });
    }
}
