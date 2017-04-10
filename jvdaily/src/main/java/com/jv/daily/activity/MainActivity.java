package com.jv.daily.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.jv.bannerlib.Banner;
import com.jv.bannerlib.BannerConfig;
import com.jv.bannerlib.listener.OnBannerListener;
import com.jv.daily.R;
import com.jv.daily.adapter.MultiTypeAdapter;
import com.jv.daily.app.MyApplication;
import com.jv.daily.databinding.ActivityMainBinding;
import com.jv.daily.bean.TopStoriesBean;
import com.jv.daily.mvp.presenter.MainPresenter;
import com.jv.daily.mvp.view.IMainView;
import com.jv.daily.utils.GlideImageLoader;
import com.jv.recyclerlib.XRecyclerView;

import java.util.List;


import static com.jv.bannerlib.BannerConfig.RIGHT;


public class MainActivity extends AppCompatActivity implements IMainView, XRecyclerView.LoadingListener {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private MainPresenter mPresenter;

    private XRecyclerView rvContainer;
    private Banner banner;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        mPresenter = new MainPresenter(this, this);

        initView();
    }

    /**
     * 初始化 View
     */
    private void initView() {
        rvContainer = binding.rvContent;
        banner = binding.banner;
        fab = binding.fab;

        //设置数据容器配置
        rvContainer.setLayoutManager(new LinearLayoutManager(this));
        rvContainer.setLoadingListener(this);
        rvContainer.setArrowImageView(R.drawable.ic_loading_rotate);
        rvContainer.refresh();
    }


    /**
     * 添加广告轮播回调
     *
     * @param list
     */
    @Override
    public void insertBannerView(final List<TopStoriesBean> list, List<String> images, final List<String> titles) {
        banner.setIndicatorGravity(RIGHT)
                .setImages(images)
                .setBannerTitles(titles)
                .setImageLoader(new GlideImageLoader())
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                intent.putExtra("id", list.get(position).getId() + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void errorBannerView() {
        banner.setVisibility(View.GONE);
    }

    /**
     * 添加数据内容回调
     *
     * @param adapter
     */
    @Override
    public void insertContentData(MultiTypeAdapter adapter) {

        rvContainer.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void loadNews() {
        rvContainer.loadMoreComplete();
    }

    @Override
    public void refreshNews() {
        rvContainer.refreshComplete();
    }


    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh");
        mPresenter.refreshNews();
    }

    @Override
    public void onLoadMore() {
        Log.i(TAG, "onLoadMore");
        mPresenter.loadNews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.update:
//                if (SkinConfig.isDefaultSkin(this)) {
//                    SkinManager.getInstance().loadSkin("color.skin", new SkinLoaderListener() {
//                        @Override
//                        public void onStart() {
//
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(MainActivity.this, "夜间模式打开", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onFailed(String errMsg) {
////                            Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onProgress(int progress) {
//
//                        }
//                    });
//                } else {
//                    SkinManager.getInstance().restoreDefaultTheme();
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /******************************
     * 生命周期
     **************************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.hasFist = false;
    }

}
