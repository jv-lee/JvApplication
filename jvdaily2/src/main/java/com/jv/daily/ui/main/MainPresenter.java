package com.jv.daily.ui.main;

import android.util.Log;

import com.jv.daily.base.mvp.BasePresenter;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.entity.NewsBean;
import com.jv.daily.entity.StoriesBean;
import com.jv.daily.entity.TopStoriesBean;
import com.jv.daily.constant.Constant;
import com.jv.daily.utils.ConstUtil;
import com.jv.daily.utils.NetworkUtils;
import com.jv.daily.utils.SPUtil;
import com.jv.daily.utils.TimeUtil;

import org.reactivestreams.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/4/11.
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> implements MainContract.Presenter {

    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();
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

        /**
         * 刷新接口为当天新闻 不重复刷新
         */
        if (!hasFirstRefresh) {
            mView.refreshEvent(Constant.REFRESH_COMPLETE, "");
            return;
        }
        hasFirstRefresh = false;

        //查询本地是否有今日新闻
        String date = TimeUtil.milliseconds2StringSimple(TimeUtil.getCurrentTimeMillis());
        Log.d(TAG, "refresh today time -> " + date);

        if (mModel.findRefreshDataCount(date) > 0) {

            Log.w(TAG, "into local refresh data .");
            mModel.refreshDataToDb(date)
                    .subscribe(new Observer<NewsBean>() {

                        @Override
                        public void onError(Throwable e) {
                            mView.refreshEvent(Constant.REFRESH_FAIL, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            mView.refreshEvent(Constant.REFRESH_COMPLETE, null);
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            if (d.isDisposed()) {
                                d.dispose();
                            }
                        }

                        @Override
                        public void onNext(NewsBean newsBean) {
                            newsBean.getStories().add(0, new StoriesBean(StoriesBean.TITLE, "今日新闻"));

                            Log.d(TAG, "local date -> " + newsBean.getDate());

                            for (TopStoriesBean top : newsBean.getTop_stories()) {
                                if (!images.contains(top.getImage())) images.add(top.getImage());
                                if (!titles.contains(top.getTitle())) titles.add(top.getTitle());
                            }
                            mView.initBanner(newsBean.getTop_stories(), images, titles);
                            mView.initContent(newsBean.getStories());
                        }
                    });
        } else {
            Log.w(TAG, "into network refresh data");

            if (!NetworkUtils.isConnected(getContext())) {
                mView.refreshEvent(Constant.REFRESH_FAIL, "网络未连接");
                return;
            }
            mModel.refreshData().map(new Function<NewsBean, NewsBean>() {
                @Override
                public NewsBean apply(NewsBean newsBean) {
                    Log.d(TAG, "network date -> " + newsBean.getDate());

                    for (TopStoriesBean top : newsBean.getTop_stories()) {
                        if (!images.contains(top.getImage())) images.add(top.getImage());
                        if (!titles.contains(top.getTitle())) titles.add(top.getTitle());
                    }
                    boolean b1 = mModel.insertDataToDb(newsBean.getStories(), newsBean.getDate());
                    boolean b2 = mModel.inertTopDataToDb(newsBean.getTop_stories(), newsBean.getDate());

                    Log.d(TAG, "insertDataToDb -> " + b1 + "\n insertTopDataToDb -> " + b2);

                    //添加头部设置到View上
                    newsBean.getStories().add(0, new StoriesBean(StoriesBean.TITLE, "今日新闻"));
                    return newsBean;
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<NewsBean>() {

                        @Override
                        public void onError(Throwable e) {
                            mView.refreshEvent(Constant.REFRESH_FAIL, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            mView.refreshEvent(Constant.REFRESH_COMPLETE, "onCompleted()");
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            if (d.isDisposed()) {
                                d.dispose();
                            }
                        }

                        @Override
                        public void onNext(NewsBean newsBean) {
                            mView.initBanner(newsBean.getTop_stories(), images, titles);
                            mView.initContent(newsBean.getStories());
                        }
                    });
        }
    }

    @Override
    public void loadNews() {
        //首次进入 加载昨天的更多新闻 - 非首次 直接加载存储时间新闻 api原因 按当天时间获取昨天时间新闻
        if (hasFirstLoad) {
            LoadDate = TimeUtil.milliseconds2StringSimple(TimeUtil.getCurrentTimeMillis() - ConstUtil.DAY);
            hasFirstLoad = false;
        } else {
            LoadDate = TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple((String) SPUtil.get(Constant.SELECT_DATE, TimeUtil.milliseconds2StringSimple(TimeUtil.getCurrentTimeMillis()))) - ConstUtil.DAY);
        }
        Log.d(TAG, "load date - >" + LoadDate);

        //判断当前数据库是否有当前日期数据存储
        if (mModel.findLoadDataCount(LoadDate) > 0) {
            Log.w(TAG, "into local load data");
            mModel.loadDataToDb(LoadDate)
                    .subscribe(new Observer<List<StoriesBean>>() {

                        @Override
                        public void onError(Throwable e) {
                            mView.refreshEvent(Constant.LOAD_FAIL, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            //加载成功后把下次刷新时间保存
                            SPUtil.save(Constant.SELECT_DATE, TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(LoadDate)));
                            mView.refreshEvent(Constant.LOAD_COMPLETE, "onCompleted()");
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            if (d.isDisposed()) {
                                d.dispose();
                            }
                        }

                        @Override
                        public void onNext(List<StoriesBean> storiesBeen) {
                            //添加头部
                            String dateStr = TimeUtil.milliseconds2String(TimeUtil.string2MillisecondsSimple(storiesBeen.get(0).getDate()), new SimpleDateFormat("MM月dd日"));
                            storiesBeen.add(0, new StoriesBean(StoriesBean.TITLE, dateStr));
                            mView.loadNews(storiesBeen);
                        }
                    });
            //使用网络获取当前日期数据
        } else {
            Log.w(TAG, "into network load data");
            if (!NetworkUtils.isConnected(getContext())) {
                mView.refreshEvent(Constant.REFRESH_FAIL, "网络未连接");
                return;
            }

//            Log.w(TAG, TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(LoadDate) + ConstUtil.DAY));
            mModel.loadNews(TimeUtil.milliseconds2StringSimple(TimeUtil.string2MillisecondsSimple(LoadDate) + ConstUtil.DAY))
                    .map(new Function<NewsBean, List<StoriesBean>>() {
                        @Override
                        public List<StoriesBean> apply(NewsBean newsBean) {
                            //网络获取数据后做磁盘存储
                            boolean bo1 = mModel.insertDataToDb(newsBean.getStories(), newsBean.getDate());
                            Log.w(TAG, "insert stories - date " + newsBean.getDate() + " is ->" + bo1);
                            //设置头部数据
                            String dateStr = TimeUtil.milliseconds2String(TimeUtil.string2MillisecondsSimple(newsBean.getDate()), new SimpleDateFormat("MM月dd日"));
                            newsBean.getStories().add(0, new StoriesBean(StoriesBean.TITLE, dateStr));
                            return newsBean.getStories();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<StoriesBean>>() {
                        @Override
                        public void onComplete() {
                            SPUtil.save(Constant.SELECT_DATE, LoadDate);
                            mView.refreshEvent(Constant.LOAD_COMPLETE, "loadCompleted()");
                        }

                        @Override
                        public void onError(final Throwable e) {
                            Observable.timer(1, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(@NonNull Long aLong) throws Exception {
                                            mView.refreshEvent(Constant.LOAD_FAIL, e.getMessage());
                                        }
                                    });
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            if (d.isDisposed()) {
                                d.dispose();
                            }
                        }

                        @Override
                        public void onNext(List<StoriesBean> list) {
                            mView.loadNews(list);
                        }
                    });
        }
    }
}
