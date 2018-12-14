package com.example.sketchxu.mall.utils;

import android.content.Context;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.sketchxu.mall.bean.Page;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class Pager {

    private static Builder builder;

    private OkHttpHelper okHttpHelper;


    private static final int STATE_NORMAL = 1;
    private static final int STATE_REFRESH = 2;
    private static final int STATE_LOAD_MORE = 3;
    private int state = STATE_NORMAL;


    private Pager() {

        okHttpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder() {
        builder = new Builder();

        return builder;
    }

    private void initRefreshLayout() {
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (builder.curPage < builder.totalPage) {
                    loadMore();
                } else {
                    builder.mRefreshLayout.finishRefreshLoadMore();
                }

            }
        });
    }

    private void refresh() {
        builder.curPage = 1;
        state = STATE_REFRESH;
        requestData();
    }

    private void loadMore() {
        builder.curPage++;
        state = STATE_LOAD_MORE;
        requestData();
    }

    private void requestData() {

        okHttpHelper.get(buildUrl(), new RequestCallback(builder.mContext));
    }

    private <T> void showData(List<T> datas, int totalPage, int totalCount) {

        switch (state) {
            case STATE_NORMAL:
                if (builder.listener != null) {
                    builder.listener.load(datas, totalPage, totalCount);
                }
                break;
            case STATE_REFRESH:
                if (builder.listener != null) {
                    builder.listener.refresh(datas, totalPage, totalCount);
                }
                builder.mRefreshLayout.finishRefresh();
                break;
            case STATE_LOAD_MORE:
                if (builder.listener != null) {
                    builder.listener.loadMore(datas, totalPage, totalCount);
                }
                builder.mRefreshLayout.finishRefreshLoadMore();
                break;
        }
        state = STATE_NORMAL;

    }

    public void setParam(String key, Object value) {
        builder.params.put(key, value);
    }

    private String buildUrl() {
        return builder.url + "?" + buildParams();
    }

    private String buildParams() {
        HashMap<String, Object> params = builder.params;
        params.put("curPage", builder.curPage);
        params.put("pageSize", builder.pageSize);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entity : params.entrySet()) {
            sb.append(entity.getKey() + "=" + entity.getValue());
            sb.append("&");
        }
        String str = sb.toString();
        if (str.endsWith("&")) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public void request() {
        builder.curPage = 1;
        requestData();
    }

    public static class Builder {

        private String url;

        private MaterialRefreshLayout mRefreshLayout;
        private boolean canLoadMore;

        private int curPage = 1;
        private int pageSize = 10;
        private int totalPage = 3;

        private OnPageListener listener;

        private Context mContext;

        private Type mType;

        private HashMap<String, Object> params = new HashMap<>();

        public Builder setUrl(String url) {
            this.url = url;

            return this;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.mRefreshLayout = refreshLayout;

            return this;
        }

        public Builder putParam(String key, Object value) {
            params.put(key, value);

            return this;
        }

        public Builder setCanLoadMore(boolean canLoadMore) {
            this.canLoadMore = canLoadMore;

            return this;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;

            return this;
        }

        public Builder setOnPageListener(OnPageListener listener) {
            this.listener = listener;

            return this;
        }

        public Pager build(Context context, Type type) {
            this.mContext = context;
            this.mType = type;

            valid();

            return  new Pager();
        }

        private void valid() {
            if (mContext == null)
                throw new IllegalArgumentException("context can not be null");

            if (mRefreshLayout == null)
                throw new IllegalArgumentException("MaterialRefreshLayout can not be null");

            if (mContext == null)
                throw new IllegalArgumentException("context can not be null");
        }

    }

    public interface OnPageListener<T> {

        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);
    }

    class RequestCallback<T> extends SpotsCallback<Page<T>> {

        public RequestCallback(Context mContext) {
            super(mContext);

            this.mType = builder.mType;
        }

        @Override
        public void onSuccess(Response response, Page<T> pages) {
            showData(pages.getList(), builder.totalPage, pages.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {

        }
    }
}
