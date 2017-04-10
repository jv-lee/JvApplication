package com.jv.daily.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jv.daily.R;
import com.jv.daily.databinding.ActivityContentBinding;
import com.jv.daily.mvp.module.NewsContentBean;
import com.jv.daily.net.RetrofitSubscriber;
import com.jv.daily.net.RetrofitUtils;
import com.jv.daily.utils.ShareUtil;


public class ContentActivity extends AppCompatActivity {

    private ActivityContentBinding binding;
    private RetrofitSubscriber<NewsContentBean> contentApi;
    private Toolbar toolbar;

    private WebView webView;
    private ProgressBar pbWeb;

    String title;
    String url;
    boolean hasLoadUrl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content);
        String id = getIntent().getStringExtra("id");
        initToolbar();
//        initSwipeBackLayout();
        initWebView();

        contentApi = new RetrofitSubscriber<>(new RetrofitSubscriber.SubscriberOnNextListener<NewsContentBean>() {

            @Override
            public void onNext(NewsContentBean newsContentBean) {
                hasLoadUrl = true;
                title = newsContentBean.getTitle();
                url = newsContentBean.getShare_url();
//                webView.loadDataWithBaseURL(null, newsContentBean.getBody(), "text/html", "utf-8", null);
                webView.loadUrl(newsContentBean.getShare_url());
            }

            @Override
            public void onError(String msg) {

            }

            @Override
            public void onCompleted() {

            }
        });
        RetrofitUtils.getInstance().getNewsContentById(contentApi, id);

    }

//    private void initSwipeBackLayout() {
//        SwipeBackLayout mSwipeBackLayout = getSwipeBackLayout();
//        //设置可以滑动的区域，推荐用屏幕像素的一半来指定
//        mSwipeBackLayout.setEdgeSize(200);
//        //设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
//        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//    }

    private void initToolbar() {
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        //toolbar Navigation键 关闭当前Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWebView() {

        webView = binding.wvContent;
        pbWeb = binding.pbWeb;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setHorizontalScrollbarOverlay(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress == 100) {
                    pbWeb.setVisibility(View.VISIBLE);
                    pbWeb.setProgress(100);
                    //progressBar.setProgress(newProgress);
                } else {
                    pbWeb.setVisibility(View.VISIBLE);
                    pbWeb.setProgress(newProgress);//设置加载进度
                }

            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                if (hasLoadUrl) {
                    ShareUtil.shareText(this, "新闻：" + title + "\n URL：" + url);
                } else {
                    Toast.makeText(this, "新闻页面未加载完成", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 防止新闻直接回退
     *
     * @param keyCode
     * @param keyEvent
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == keyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, keyEvent);
    }

}
