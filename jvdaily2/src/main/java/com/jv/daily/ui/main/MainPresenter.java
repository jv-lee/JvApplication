package com.jv.daily.ui.main;

import android.util.Log;

import com.jv.daily.base.mvp.BasePresenter;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.bean.NewsBean;
import com.jv.daily.bean.StoriesBean;
import com.jv.daily.bean.TopStoriesBean;
import com.jv.daily.constant.Constant;
import com.jv.daily.ui.main.MainContract.Presenter;
import com.jv.daily.utils.ConstUtil;
import com.jv.daily.utils.SPUtil;
import com.jv.daily.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/11.
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> implements Presenter {

    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<TopStoriesBean> topStoriesBeans;
    List<StoriesBean> storiesBeans;
    boolean hasFirstRefresh = true;
    boolean hasFirstLoad = true;

    @Inject
    public MainPresenter() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void refreshNews() {

        final long time = System.currentTimeMillis();

        /**
         * 刷新接口为当天新闻 不重复刷新
         */
        if (!hasFirstRefresh) {
            mView.refreshEvent(0, null);
            return;
        }
        hasFirstRefresh = false;

        //查询本地是否有今日新闻
        String date = TimeUtil.milliseconds2StringSimple(System.currentTimeMillis());


        if (mModle.findDataCount(date) > 0) {

            Observable.just(date).map(new Func1<String, NewsBean>() {
                @Override
                public NewsBean call(String s) {
                    return mModle.initDataToDb(s);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NewsBean>() {
                        @Override
                        public void onCompleted() {
                            mView.refreshEvent(0, null);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.refreshEvent(1, e.getMessage());
                        }

                        @Override
                        public void onNext(NewsBean newsBean) {
                            topStoriesBeans = newsBean.getTop_stories();
                            storiesBeans = newsBean.getStories();

                            Log.d(TAG, newsBean.getDate());

                            for (TopStoriesBean top : topStoriesBeans) {
                                if (!images.contains(top.getImage())) images.add(top.getImage());
                                if (!titles.contains(top.getTitle())) titles.add(top.getTitle());
                            }
                            mView.initBanner(topStoriesBeans, images, titles);
                            mView.initContent(storiesBeans);
                        }
                    });
        } else {
            mModle.initData().map(new Func1<NewsBean, NewsBean>() {
                @Override
                public NewsBean call(NewsBean newsBean) {
                    topStoriesBeans = newsBean.getTop_stories();
                    storiesBeans = newsBean.getStories();

                    Log.d(TAG, newsBean.getDate());

                    for (TopStoriesBean top : topStoriesBeans) {
                        if (!images.contains(top.getImage())) images.add(top.getImage());
                        if (!titles.contains(top.getTitle())) titles.add(top.getTitle());
                    }
                    boolean b1 = mModle.insertDataToDb(storiesBeans, newsBean.getDate());
                    boolean b2 = mModle.inertTopDataToDb(topStoriesBeans, newsBean.getDate());

                    Log.d(TAG, b1 + "  -  " + b2);

                    return newsBean;
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NewsBean>() {
                        @Override
                        public void onCompleted() {
                            mView.refreshEvent(0, null);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.refreshEvent(1, e.getMessage());
                        }

                        @Override
                        public void onNext(NewsBean newsBean) {
                            mView.initBanner(topStoriesBeans, images, titles);
                            mView.initContent(storiesBeans);
                        }
                    });
        }
    }

    @Override
    public void loadNews() {
        //获取其当前查询时间 为当天 api 加载昨日新闻
        String date;
        if (hasFirstLoad) {
            date = TimeUtil.milliseconds2StringSimple(System.currentTimeMillis());
            SPUtil.save(Constant.SELECT_DATE, TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(date) - ConstUtil.DAY));
            hasFirstLoad = false;
        } else {
            date = (String) SPUtil.get(Constant.SELECT_DATE, TimeUtil.milliseconds2StringSimple(System.currentTimeMillis()));
            SPUtil.save(Constant.SELECT_DATE, TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(date) - ConstUtil.DAY));
        }

        Log.d(TAG, date);

        mModle.loadNews(date)
                .map(new Func1<NewsBean, List<StoriesBean>>() {
                    @Override
                    public List<StoriesBean> call(NewsBean newsBean) {
                        return newsBean.getStories();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<StoriesBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.refreshEvent(1, e.getMessage());
                    }

                    @Override
                    public void onNext(List<StoriesBean> list) {
                        storiesBeans.addAll(list);
                        mView.loadNews(list);
                    }
                });
    }
}
