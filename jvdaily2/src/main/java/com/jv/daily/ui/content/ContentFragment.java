package com.jv.daily.ui.content;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jv.daily.R;
import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.mvp.BaseFragment;
import com.jv.daily.entity.NewsContentBean;
import com.jv.daily.utils.ShareUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/18.
 */

@SuppressLint("ValidFragment")
public class ContentFragment extends BaseFragment<ContentContract.Presenter> implements ContentContract.View {

    @BindView(R.id.wv_content)
    WebView wvContent;
    @BindView(R.id.pb_web)
    ProgressBar pbWeb;

    NewsContentBean newsContentBean;
    boolean hasLoadUrl = false;

    @Override
    protected void componentInject(AppComponent appComponent) {
    }

    @Override
    protected View bindRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, null, false);
    }

    @Override
    protected void bindData() {
        setHasOptionsMenu(true);
        initWebView();
        mPresenter.loadWeb(getArguments().getString("id"));
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
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:function setTop(){document.querySelector('.header-for-mobile').style.display=\"none\";}setTop();");
            }
        });
        wvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                view.loadUrl("javascript:function setTop(){document.querySelector('.header-for-mobile').style.display=\"none\";}setTop();");

                if (newProgress == 100) {
                    if (pbWeb != null && wvContent != null) {
                        pbWeb.setProgress(100);
                        pbWeb.setVisibility(View.GONE);
                        wvContent.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (pbWeb != null && wvContent != null) {
                        pbWeb.setVisibility(View.VISIBLE);
                        wvContent.setVisibility(View.GONE);
                        pbWeb.setProgress(newProgress);//设置加载进度
                    }
                }
            }
        });
    }

    @Override
    public void loadWeb(NewsContentBean bean) {
        Log.w(TAG, "loadWeb");
        hasLoadUrl = true;
        newsContentBean = bean;
        wvContent.loadUrl(newsContentBean.getShare_url());
//        wvContent.loadDataWithBaseURL(null, bean.getBody(), "text/html", "utf-8", null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.content_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                if (hasLoadUrl) {
                    ShareUtil.shareText(mActivity, "新闻：" + newsContentBean.getTitle() + "\n URL：" + newsContentBean.getShare_url());
                } else {
                    Toast.makeText(mActivity, "新闻页面未加载完成", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
