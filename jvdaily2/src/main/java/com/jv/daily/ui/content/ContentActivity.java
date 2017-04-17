package com.jv.daily.ui.content;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jv.daily.R;
import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.mvp.BaseActivity;
import com.jv.daily.entity.NewsContentBean;
import com.jv.daily.ui.content.inject.ContentModule;
import com.jv.daily.ui.content.inject.DaggerContentComponent;

import butterknife.BindView;

/**
 * Created by 64118 on 2017/4/12.
 */

public class ContentActivity extends BaseActivity<ContentContract.Presenter> implements ContentContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wv_content)
    WebView wvContent;
    @BindView(R.id.pb_web)
    ProgressBar pbWeb;

    String title;
    String url;
    boolean hasLoadUrl = false;

    @Override
    protected int bindRootView() {
        return R.layout.activity_content;
    }

    @Override
    protected void bindData() {
        initToolbar();
        initWebView();
        mPresenter.loadWeb(getIntent().getStringExtra("id"));
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerContentComponent.builder()
                .appComponent(appComponent)
                .contentModule(new ContentModule(this))
                .build()
                .inject(this);
    }

    /**
     * 初始化toolbar 设置back键
     */
    public void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化webView 设置参数
     */
    private void initWebView() {
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvContent.setHorizontalScrollBarEnabled(false);
        wvContent.getSettings().setSupportZoom(true);
        wvContent.getSettings().setBuiltInZoomControls(true);
        wvContent.setHorizontalScrollbarOverlay(true);
        wvContent.setWebViewClient(new WebViewClient());
        wvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                if (newProgress == 100) {
                    pbWeb.setProgress(100);
                    pbWeb.setVisibility(View.GONE);
                } else {
                    pbWeb.setVisibility(View.VISIBLE);
                    pbWeb.setProgress(newProgress);//设置加载进度
                }
            }
        });
    }

    @Override
    public void loadWeb(NewsContentBean bean) {
        hasLoadUrl = true;
        title = bean.getTitle();
        url = bean.getShare_url();
//        wvContent.loadUrl(url);
        wvContent.loadDataWithBaseURL(null, bean.getBody(), "text/html", "utf-8", null);
    }
}
