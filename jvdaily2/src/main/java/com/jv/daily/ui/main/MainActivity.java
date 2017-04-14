package com.jv.daily.ui.main;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jv.bannerlib.Banner;
import com.jv.bannerlib.BannerConfig;
import com.jv.daily.R;
import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.mvp.BaseActivity;
import com.jv.daily.bean.StoriesBean;
import com.jv.daily.bean.TopStoriesBean;
import com.jv.daily.constant.Constant;
import com.jv.daily.ui.main.adapter.NewsAdapter;
import com.jv.daily.ui.main.inject.DaggerMainComponent;
import com.jv.daily.ui.main.inject.MainModule;
import com.jv.daily.utils.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.jv.bannerlib.BannerConfig.RIGHT;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refreshView;

    private NewsAdapter newsAdapter;


    @Override
    protected int bindRootView() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindData() {
        setSupportActionBar(toolbar);
        //设置数据容器配置
        refreshView.setOnRefreshListener(this);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(new ArrayList<StoriesBean>());
        newsAdapter.setOnLoadMoreListener(this);
        newsAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvContent.setAdapter(newsAdapter);
        ininRvListener(rvContent);
        refreshView.post(new Runnable() {
            @Override
            public void run() {
                refreshView.setRefreshing(true);
                mPresenter.refreshNews();
            }
        });

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    /**
     * 通过下拉刷新 或 首次进入初始化
     * 通过 Presenter调用刷新数据接口
     * 回调 initBanner()  initContent()
     */
    @Override
    public void onRefresh() {
        mPresenter.refreshNews();
    }

    @Override
    public void initBanner(List<TopStoriesBean> list, List<String> images, List<String> titles) {
        banner.setIndicatorGravity(RIGHT)
                .setImages(images)
                .setBannerTitles(titles)
                .setImageLoader(new GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .start();
    }

    @Override
    public void initContent(List<StoriesBean> list) {
        newsAdapter.getData().addAll(list);
        newsAdapter.notifyDataSetChanged();
    }


    /**
     * 通过滑动到底部 监听
     * 通过Presenter调用 加载更多接口
     * 回调 loadNews();
     */
    @Override
    public void onLoadMoreRequested() {
        Log.w(TAG, "onLoadMoreRequested");
        mPresenter.loadNews();
    }

    @Override
    public void loadNews(List<StoriesBean> list) {
        newsAdapter.getData().addAll(list);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshEvent(String code, String message) {
        switch (code) {
            case Constant.REFRESH_COMPLETE:
                Log.d(TAG, "refreshEvent() - > setRefreshing(true)");
                refreshView.setRefreshing(false);
                break;
            case Constant.REFRESH_FAIL:
                Log.d("android", message);
                refreshView.setRefreshing(false);
//                newsAdapter.setEmptyView(R.layout.item_news_empty);
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                break;
            case Constant.LOAD_COMPLETE:
                newsAdapter.loadMoreComplete();
                break;
            case Constant.LOAD_FAIL:
                newsAdapter.loadMoreFail();
                break;
            case Constant.LOAD_EDN:
                newsAdapter.loadMoreEnd();
                break;
        }
    }

    private void ininRvListener(RecyclerView rvContent) {
        rvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(MainActivity.this, "click item position ->" + position, Toast.LENGTH_SHORT).show();
            }
        });
        rvContent.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_news_pic:
                        Toast.makeText(MainActivity.this, "click pic position -> " + position, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tv_news_text:
                        Toast.makeText(MainActivity.this, "click text position -> " + position, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

}
