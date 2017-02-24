package com.jv.daily;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jv.daily.bean.NewsBean;
import com.jv.daily.databinding.ActivityMainBinding;
import com.jv.daily.net.ProgressSubscriber;
import com.jv.daily.net.RetrofitUtils;
import com.jv.daily.utils.SizeUtils;
import com.jv.daily.widget.BannerView;
import com.jv.daily.widget.MyRecyclerView;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MyRecyclerView rvContainer;
    private TwinklingRefreshLayout refreshLayout;
    private ProgressSubscriber<NewsBean> newsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        rvContainer = binding.rvContent;
        refreshLayout = binding.refreshContainer;
        final BannerView bannerView = new BannerView(this);
        bannerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(this, 200)));


        //发起网络请求 获取新闻 top Banner数据
        newsApi = new ProgressSubscriber<NewsBean>(this, new ProgressSubscriber.SubscriberOnNextListener<NewsBean>() {
            @Override
            public void onNext(NewsBean bean) {
                List<String> list = new ArrayList<>();
                for (NewsBean.TopStoriesBean beans : bean.getTop_stories()) {
                    list.add(beans.getImage());
                }
                bannerView.setList(list);
                refreshLayout.addFixedExHeader(bannerView);
            }
        });
        RetrofitUtils.getInstance().getNewsApi(newsApi);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newsApi.unsubscribe();
    }
}
