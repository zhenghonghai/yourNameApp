package com.example.gyt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    /**
     * 解析地址
     * 1.   https://jx.618g.com/?url=
     * 2.   https://vip.bljiex.com/?v=
     * 3.   https://www.heimijx.com/jx/api/?url=
     * 4.   https://api.v6.chat/?url=
     * 5.   http://cn.bjbanshan.cn/jiexi.php?url=
     * 6.   http://jx.drgxj.com/?url=
     * 7.   https://api.sigujx.com/?url=
     * 8.   http://jiexi.071811.cc/jx.php?url=
     * 9.
     * 10.  http://jsap.attakids.com/?url=
     */
    private final static String jx = "http://jsap.attakids.com/?url=";
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private Button mButton;
    private String uri;
    private boolean isAdFlag = false;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findView();
        init();
        setListeneries();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d("MainActivity","什么时候执行销毁");
//    }

    /**
     * 键盘返回
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            mButton.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void findView() {
        mProgressBar = findViewById(R.id.progressBar);
        mWebView = findViewById(R.id.webView);
        mButton = findViewById(R.id.button);
    }

    private void setListeneries() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl(jx + uri);
                isAdFlag = true;
                mButton.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void init() {
        mWebView.canGoBack();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(false);// 将图片调整到适合webview大小
        mWebView.getSettings().setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        mWebView.getSettings().setCacheMode(mWebView.getSettings().LOAD_CACHE_ELSE_NETWORK);//支持缓存
        mWebView.loadUrl("https://v.qq.com/x/search/");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://m.v.qq.com/x/cover") || url.startsWith("https://m.v.qq.com/cover")) {
//                    isAdFlag = true;
//                    uri = url;
                    mButton.setVisibility(View.VISIBLE);
                    mButton.setClickable(false);
                    mButton.setBackgroundColor(Color.parseColor("#808080"));
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                if (url.startsWith("https://m.v.qq.com/x/cover") || url.startsWith("https://m.v.qq.com/cover")) {
                    uri = url;
                    isAdFlag = true;
                    mButton.setVisibility(View.VISIBLE);
                    mButton.setClickable(true);
                    mButton.setBackgroundColor(Color.parseColor("#F4CD61"));
                }
                super.onPageFinished(view, url);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                if (isAdFlag) {
                    //判断是否是广告相关的资源链接
                    if (!AdFilterTool.isAd(getApplicationContext(), url)) {
                        //这里是不做处理的数据
                        return super.shouldInterceptRequest(view, url);
                    } else {
                        //有广告的请求数据，我们直接返回空数据，注：不能直接返回null
                        return new WebResourceResponse(null, null, null);
                    }
//                }
//                return super.shouldInterceptRequest(view, url);
            }
        });
    }

    /**
     * 去除广告工具
     */
    public static class AdFilterTool {
        public static boolean isAd(Context context, String url) {
            Resources res = context.getResources();
            String[] filterUrls = res.getStringArray(R.array.adUrls);
            for (String adUrl : filterUrls) {
                if (url.contains(adUrl)) {
                    return true;
                }
            }
            return false;
        }
    }
}