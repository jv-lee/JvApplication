package com.jv.daily.ui.content;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.jv.daily.R;
import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.mvp.BaseActivity;
import com.jv.daily.ui.content.adapter.ContentPagerAdapter;
import com.jv.daily.ui.content.inject.ContentModule;
import com.jv.daily.ui.content.inject.DaggerContentComponent;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 64118 on 2017/4/12.
 */

public class ContentActivity extends BaseActivity<ContentContract.Presenter> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.vp_webContainer)
    ViewPager vpWebContainer;

    @Override
    protected int bindRootView() {
        return R.layout.activity_content;
    }

    @Override
    protected void bindData() {
        initToolbar();
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        //使用viewpager 所以在适配器中 依赖注入每一个fragment
        ContentPagerAdapter pagerApdater = new ContentPagerAdapter(getSupportFragmentManager(), (List<String>) getIntent().getSerializableExtra("ids"), getIntent().getStringExtra("id"), appComponent);
        vpWebContainer.setAdapter(pagerApdater);
        vpWebContainer.setCurrentItem(pagerApdater.getThisPosition());
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

}
