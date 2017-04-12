package com.jv.daily.ui.main;

import android.util.Log;
import android.widget.Toast;

import com.jv.daily.base.mvp.BasePresenter;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.bean.NewsBean;
import com.jv.daily.bean.StoriesBean;
import com.jv.daily.bean.TopStoriesBean;
import com.jv.daily.ui.main.MainContract.Presenter;

import java.util.ArrayList;
import java.util.List;

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
    boolean hasFirst = true;

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
        if (!hasFirst) {
            mView.refreshEvent(0, null);
            return;
        }
        hasFirst = false;

        mModle.initData()
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
                        topStoriesBeans = newsBean.getTop_stories();
                        storiesBeans = newsBean.getStories();

                        for (TopStoriesBean top : topStoriesBeans) {
                            if (!images.contains(top.getImage())) images.add(top.getImage());
                            if (!titles.contains(top.getTitle())) titles.add(top.getTitle());
                        }
                        mView.initBanner(topStoriesBeans, images, titles);
                        mView.initContent(storiesBeans);
                    }
                });
    }

    @Override
    public void loadNews() {

        mModle.loadNews("20170411")
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
