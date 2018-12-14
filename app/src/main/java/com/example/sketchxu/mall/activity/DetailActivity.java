package com.example.sketchxu.mall.activity;

import android.animation.TimeAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.sketchxu.mall.Contants;
import com.example.sketchxu.mall.MyApplication;
import com.example.sketchxu.mall.R;
import com.example.sketchxu.mall.bean.ShoppingCart;
import com.example.sketchxu.mall.bean.User;
import com.example.sketchxu.mall.bean.Ware;
import com.example.sketchxu.mall.http.OkHttpHelper;
import com.example.sketchxu.mall.http.SpotsCallback;
import com.example.sketchxu.mall.msg.BaseMessage;
import com.example.sketchxu.mall.utils.CartProvider;
import com.example.sketchxu.mall.utils.ToastUtils;
import com.example.sketchxu.mall.widget.CnToolbar;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Response;

public class DetailActivity extends BaseActivity {

    @ViewInject(R.id.webview)
    private WebView webView;

    @ViewInject(R.id.toolbar)
    private CnToolbar toolbar;

    private JavaScriptInterface jsInterface;

    private Ware mWare;

    private CartProvider mProvider;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail);
        x.view().inject(this);

        initToolbar();

        mProvider = new CartProvider(this);

        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if (serializable == null) {
            finish();
        }

        mWare = (Ware) serializable;

        initWebView();

    }

    private void initToolbar() {
        toolbar.showRightButton();
        toolbar.getRightButton().setText("分享");
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);
        webView.loadUrl(Contants.API.WARES_DETAIL);
        webView.setWebViewClient(new WC());
        //webView.loadUrl("file:///android_asset/index.html");

        jsInterface = new JavaScriptInterface(this);
        webView.addJavascriptInterface(jsInterface, "appInterface");
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(mWare.getName());
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(mWare.getImgUrl());
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }


    class JavaScriptInterface {

        private Context mContext;

        public JavaScriptInterface(Context mContext) {
            this.mContext = mContext;
        }

        @JavascriptInterface
        public void buy(int id) {
            addToFavorite(id);
        }

        @JavascriptInterface
        public void addToCart(int id) {
            mProvider.add(mWare);
            ToastUtils.show(mContext, "加入购物车成功");
        }

        @JavascriptInterface
        public void showDetail(int id) {
            webView.loadUrl("javascript:showDetail(" + mWare.getId()+ ")");
        }

    }

    private void addToFavorite(int id) {
        long userId = MyApplication.getInstance().getUser().getId();
        String url = Contants.API.FAVORITE_CREATE + "?user_id=" + userId + "&ware_id=" + mWare.getId();
                okHttpHelper.get(url, new SpotsCallback<BaseMessage>(this) {
                    @Override
                    public void onSuccess(Response response, BaseMessage baseMessage) {
                        if (baseMessage.getStatus() == BaseMessage.STATUS_SUCCESS) {
                            ToastUtils.show(DetailActivity.this, "加入收藏夹成功");
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
    }

    class WC extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            jsInterface.showDetail(mWare.getId());
        }
    }
}
