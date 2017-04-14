package com.jv.daily.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.jv.daily.adapter.MultiTypeAdapter;
import com.jv.daily.adapter.item.NewsDateItem;
import com.jv.daily.adapter.item.NewsItem;
import com.jv.daily.constant.Constant;
import com.jv.daily.entity.DBManager;
import com.jv.daily.entity.StoriesBean;
import com.jv.daily.entity.TopStoriesBean;
import com.jv.daily.mvp.module.NewsBean;
import com.jv.daily.mvp.view.IMainView;
import com.jv.daily.net.RetrofitSubscriber;
import com.jv.daily.net.RetrofitUtils;
import com.jv.daily.utils.ConstUtil;
import com.jv.daily.utils.SPUtil;
import com.jv.daily.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.jv.daily.app.MyApplication.hasFist;

/**
 * Created by Administrator on 2017/2/27.
 */

public class MainPresenter {

    private final String TAG = MainPresenter.class.getSimpleName();

    private Context context;
    private IMainView mainView;
    private MultiTypeAdapter mAdapter;

    private RetrofitSubscriber<NewsBean> newsRefreshApi, newsLoadApi;

    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<TopStoriesBean> topStoriesBeans = new ArrayList<>();
    List<StoriesBean> storiesBeans = new ArrayList<>();


    public MainPresenter(Context context, IMainView mainView) {
        this.context = context;
        this.mainView = mainView;
        mAdapter = new MultiTypeAdapter(context);
    }

    /**
     * 刷新 首页新闻 和 banner广告 网络请求
     */
    public void refreshNews() {
        //  - 获取当日时间 并保存
        String time = TimeUtil.milliseconds2StringSimple(System.currentTimeMillis());


        //判断当前是否为重复加载
        if (hasFist) {
            mainView.refreshNews();
            return;
        }
        hasFist = true; //非首次进入则设置为 已进入

        //本地数据获取
        topStoriesBeans = DBManager.getInstance(context).queryTopStories(time);
        storiesBeans = DBManager.getInstance(context).queryStories(time);

        if (topStoriesBeans != null && storiesBeans != null) {
            Log.i(TAG, "进入refresh - >本地获取数据");
            for (TopStoriesBean bean : topStoriesBeans) {
                titles.add(bean.getTitle());
                images.add(bean.getImage());
            }
            mainView.insertBannerView(topStoriesBeans, images, titles);


            //添加数据内容 创建Item
            mAdapter.addItem(new NewsDateItem(mAdapter, "今日新闻"));
            for (StoriesBean bean : storiesBeans) {
                mAdapter.addItem(new NewsItem(mAdapter, bean));
            }
            mainView.insertContentData(mAdapter);
            //本地数据填充完毕 不执行网络请求
            mainView.refreshNews();
            SPUtil.save(Constant.BEFORE_DATE, TimeUtil.milliseconds2StringSimple(System.currentTimeMillis()));
            return;
        }

        //本地数据为空执行网络请求
        newsRefreshApi = new RetrofitSubscriber<>(new RetrofitSubscriber.SubscriberOnNextListener<NewsBean>() {

            @Override
            public void onNext(NewsBean newsBean) {
                Log.i(TAG, "进入refresh - >网络获取数据");
                topStoriesBeans = new ArrayList<>();
                storiesBeans = new ArrayList<>();


                //top广告轮播
                for (NewsBean.TopStoriesBean bean : newsBean.getTop_stories()) {
                    if (!images.contains(bean.getImage())) {
                        images.add(bean.getImage());
                    }
                    if (!titles.contains(bean.getTitle())) {
                        titles.add(bean.getTitle());
                    }
                    topStoriesBeans.add(new TopStoriesBean(bean.getId(), bean.getImage(), bean.getType(), bean.getGa_prefix(), bean.getTitle(), newsBean.getDate()));
                }
                //添加至本地存储
                DBManager.getInstance(context).insertTopStories(topStoriesBeans);
                mainView.insertBannerView(topStoriesBeans, images, titles);


                //添加数据内容 创建Item
                mAdapter.addItem(new NewsDateItem(mAdapter, "今日新闻"));
                for (NewsBean.StoriesBean bean : newsBean.getStories()) {
                    StoriesBean storiesBean = new StoriesBean(bean.getId(), bean.getTitle(), bean.getGa_prefix(), bean.isMultipic(), bean.getType(), bean.getImages().get(0), newsBean.getDate());
                    mAdapter.addItem(new NewsItem(mAdapter, storiesBean));
                    storiesBeans.add(storiesBean);
                }
                DBManager.getInstance(context).insertStories(storiesBeans);
                mainView.insertContentData(mAdapter);

                mainView.refreshNews();
                SPUtil.save(Constant.BEFORE_DATE, TimeUtil.milliseconds2StringSimple(System.currentTimeMillis()));
            }

            @Override
            public void onError(String msg) {
                mainView.errorBannerView();
            }

            @Override
            public void onCompleted() {

            }

        });
        RetrofitUtils.getInstance().getNewsLatest(newsRefreshApi);


    }

    public void loadNews() {
        //获取默认存储时间 通过默认存储时间 减去一天获取 字符串昨天时间
        String time = (String) SPUtil.get(Constant.BEFORE_DATE, TimeUtil.milliseconds2StringSimple(System.currentTimeMillis()));
        final String time2 = TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(time) - ConstUtil.DAY);

        //本地获取加载
        storiesBeans = DBManager.getInstance(context).queryStories(time2);
        if (storiesBeans != null) {
            Log.i(TAG, "进入 load -> 本地load");
            //添加数据内容 创建Item
            String dateStr = TimeUtil.milliseconds2String(TimeUtil.string2MillisecondsSimple(time2), new SimpleDateFormat("MM月dd日"));
            mAdapter.addItem(new NewsDateItem(mAdapter, dateStr));
            for (StoriesBean bean : storiesBeans) {
                mAdapter.addItem(new NewsItem(mAdapter, bean));
            }
            mAdapter.notifyDataSetChanged();
            mainView.loadNews();
            return;
        }

        //获取前日信息
        newsLoadApi = new RetrofitSubscriber<>(new RetrofitSubscriber.SubscriberOnNextListener<NewsBean>() {

            @Override
            public void onNext(NewsBean newsBean) {
                Log.i(TAG, "进入 load -> 网络load");
                storiesBeans = new ArrayList<>();

                long time = TimeUtil.string2MillisecondsSimple(newsBean.getDate());
                String dateStr = TimeUtil.milliseconds2String(time, new SimpleDateFormat("MM月dd日"));
                mAdapter.addItem(new NewsDateItem(mAdapter, dateStr));

                for (NewsBean.StoriesBean bean : newsBean.getStories()) {
                    StoriesBean storiesBean = new StoriesBean(bean.getId(), bean.getTitle(), bean.getGa_prefix(), bean.isMultipic(), bean.getType(), bean.getImages().get(0), newsBean.getDate());
                    mAdapter.addItem(new NewsItem(mAdapter, storiesBean));
                    storiesBeans.add(storiesBean);
                }
                DBManager.getInstance(context).insertStories(storiesBeans);
                mAdapter.notifyDataSetChanged();
                mainView.loadNews();
                SPUtil.save(Constant.BEFORE_DATE, time2); //存储时间
            }

            @Override
            public void onError(String msg) {
                Log.e("ERROR", msg);
            }

            @Override
            public void onCompleted() {

            }
        });

        RetrofitUtils.getInstance().getNewsBeforeByDate(newsLoadApi, time);
    }

}
