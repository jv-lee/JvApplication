package com.jv.daily.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.jv.daily.adapter.MultiTypeAdapter;
import com.jv.daily.adapter.item.HeaderItem;
import com.jv.daily.adapter.item.NewsDateItem;
import com.jv.daily.adapter.item.NewsItem;
import com.jv.daily.constant.Constant;
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

    private Context context;
    private IMainView mainView;
    private MultiTypeAdapter mAdapter;

    private RetrofitSubscriber<NewsBean> newsRefreshApi, newsLoadApi;

    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();


    public MainPresenter(Context context, IMainView mainView) {
        this.context = context;
        this.mainView = mainView;
        mAdapter = new MultiTypeAdapter(context);
    }

    /**
     * 刷新 首页新闻 和 banner广告 网络请求
     */
    public void refreshNews() {

//        String defaultTime = (String) SPUtil.get(Constant.LATEST_DATE, "null");
//        if (defaultTime.equals(TimeUtil.milliseconds2StringSimple(System.currentTimeMillis())) && !defaultTime.equals("null")) {
//            mainView.refreshNews();
//            return;
//        }

        newsRefreshApi = new RetrofitSubscriber<>(new RetrofitSubscriber.SubscriberOnNextListener<NewsBean>() {

            @Override
            public void onNext(NewsBean newsBean) {

                if (hasFist == true) {
                    mainView.refreshNews();
                    return;
                }
                hasFist = true;

                //首次进入保存当日时间
//                SPUtil.save(Constant.LATEST_DATE, newsBean.getDate());
//                String date = (String) SPUtil.get(Constant.LATEST_DATE, TimeUtil.milliseconds2StringSimple(System.currentTimeMillis()));
                String time = TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(newsBean.getDate()) + ConstUtil.DAY);
                SPUtil.save(Constant.BEFORE_DATE, time);

                //top广告轮播
                for (NewsBean.TopStoriesBean bean : newsBean.getTop_stories()) {
                    if (!images.contains(bean.getImage())) {
                        images.add(bean.getImage());
                    }
                    if (!titles.contains(bean.getTitle())) {
                        titles.add(bean.getTitle());
                    }
                }
                mainView.insertBannerView(newsBean.getTop_stories(), images, titles);


                //添加数据内容 创建Item
                mAdapter.addItem(new NewsDateItem(mAdapter, "今日新闻"));
                for (NewsBean.StoriesBean bean : newsBean.getStories()) {
                    mAdapter.addItem(new NewsItem(mAdapter, bean));
                }
                mainView.insertContentData(mAdapter);

                mainView.refreshNews();

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
        String date = (String) SPUtil.get(Constant.BEFORE_DATE, TimeUtil.milliseconds2StringSimple(System.currentTimeMillis()));
        String time = TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(date) - ConstUtil.DAY);
        SPUtil.save(Constant.BEFORE_DATE, time);
        Log.i("TIME", date + "->" + time);
        //获取前日信息
        newsLoadApi = new RetrofitSubscriber<>(new RetrofitSubscriber.SubscriberOnNextListener<NewsBean>() {

            @Override
            public void onNext(NewsBean newsBean) {
                long time = TimeUtil.string2MillisecondsSimple(newsBean.getDate());
                String dateStr = TimeUtil.milliseconds2String(time, new SimpleDateFormat("MM月dd日"));
                mAdapter.addItem(new NewsDateItem(mAdapter, dateStr));
                for (NewsBean.StoriesBean bean : newsBean.getStories()) {
                    mAdapter.addItem(new NewsItem(mAdapter, bean));
                }
                mAdapter.notifyDataSetChanged();
                mainView.loadNews();
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
