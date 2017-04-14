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
import java.util.concurrent.TimeUnit;
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
    String LoadDate;

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
            mView.refreshEvent(Constant.REFRESH_COMPLETE, "");
            return;
        }
        hasFirstRefresh = false;

        //查询本地是否有今日新闻
        String date = TimeUtil.milliseconds2StringSimple(System.currentTimeMillis());
        Log.d(TAG, "refresh today time -> " + date);

        if (mModle.findRefreshDataCount(date) > 0) {

            Log.w(TAG, "into local refresh data .");

            Observable.just(date).map(new Func1<String, NewsBean>() {
                @Override
                public NewsBean call(String s) {
                    return mModle.refreshDataToDb(s);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NewsBean>() {
                        @Override
                        public void onCompleted() {
                            mView.refreshEvent(Constant.REFRESH_COMPLETE, null);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.refreshEvent(Constant.REFRESH_FAIL, e.getMessage());
                        }

                        @Override
                        public void onNext(NewsBean newsBean) {
                            topStoriesBeans = newsBean.getTop_stories();
                            storiesBeans = newsBean.getStories();

                            Log.d(TAG, "local date -> " + newsBean.getDate());

                            for (TopStoriesBean top : topStoriesBeans) {
                                if (!images.contains(top.getImage())) images.add(top.getImage());
                                if (!titles.contains(top.getTitle())) titles.add(top.getTitle());
                            }
                            mView.initBanner(topStoriesBeans, images, titles);
                            mView.initContent(storiesBeans);
                        }
                    });
        } else {
            Log.w(TAG, "into network refresh data");
            mModle.refreshData().map(new Func1<NewsBean, NewsBean>() {
                @Override
                public NewsBean call(NewsBean newsBean) {
                    topStoriesBeans = newsBean.getTop_stories();
                    storiesBeans = newsBean.getStories();

                    Log.d(TAG, "network date -> " + newsBean.getDate());

                    for (TopStoriesBean top : topStoriesBeans) {
                        if (!images.contains(top.getImage())) images.add(top.getImage());
                        if (!titles.contains(top.getTitle())) titles.add(top.getTitle());
                    }
                    boolean b1 = mModle.insertDataToDb(storiesBeans, newsBean.getDate());
                    boolean b2 = mModle.inertTopDataToDb(topStoriesBeans, newsBean.getDate());

                    Log.d(TAG, "insertDataToDb -> " + b1 + "\n insertTopDataToDb -> " + b2);

                    return newsBean;
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NewsBean>() {
                        @Override
                        public void onCompleted() {
                            mView.refreshEvent(Constant.REFRESH_COMPLETE, "onCompleted()");
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.refreshEvent(Constant.REFRESH_FAIL, e.getMessage());
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

        //首次进入 加载昨天的更多新闻 - 非首次 直接加载存储时间新闻
        if (hasFirstLoad) {
            LoadDate = TimeUtil.milliseconds2StringSimple(System.currentTimeMillis() - ConstUtil.DAY);
            hasFirstLoad = false;
        } else {
            LoadDate = (String) SPUtil.get(Constant.SELECT_DATE, TimeUtil.milliseconds2StringSimple(System.currentTimeMillis() - ConstUtil.DAY));
        }

        Log.d(TAG, "load date - >" + LoadDate);

        if (mModle.findLoadDataCount(LoadDate) < 0) {
            Log.w(TAG, "into local load data");
//            Observable.just(LoadDate)
//                    .map(new Func1<String, List<StoriesBean>>() {
//                        @Override
//                        public List<StoriesBean> call(String s) {
//                            return mModle.loadDataToDb(s);
//                        }
//                    }).subscribeOn(Schedulers.io())
//                    .
//            mModle.loadDataToDb(LoadDate)


        } else {
            Log.w(TAG, "into network load data");
            mModle.loadNews(LoadDate)
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
                            SPUtil.save(Constant.SELECT_DATE, TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(LoadDate) - ConstUtil.DAY));
                            mView.refreshEvent(Constant.LOAD_COMPLETE, "loadCompleted()");
                        }

                        @Override
                        public void onError(final Throwable e) {
                            Observable.timer(1, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Long>() {
                                        @Override
                                        public void call(Long aLong) {
                                            mView.refreshEvent(Constant.LOAD_FAIL, e.getMessage());
                                        }
                                    });
                        }

                        @Override
                        public void onNext(List<StoriesBean> list) {
                            storiesBeans.addAll(list);
                            mView.loadNews(list);
                        }
                    });
        }
    }
}
