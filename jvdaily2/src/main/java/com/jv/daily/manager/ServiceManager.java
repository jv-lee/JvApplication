package com.jv.daily.manager;

import com.jv.daily.bean.NewsBean;
import com.jv.daily.bean.NewsContentBean;
import com.jv.daily.service.NewsService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/11.
 */
@Singleton
public class ServiceManager {

    @Inject
    public NewsService mNewsService;

    @Inject
    public ServiceManager() {
    }

    public void getNewsLatest(Subscriber<NewsBean> subscriber) {
        mNewsService.getNewsLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getNewsBeforeByDate(String date, Subscriber<NewsBean> subscriber) {
        mNewsService.getNewsBeforeByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getNewsContentById(String id, Subscriber<NewsContentBean> subscriber) {
        mNewsService.getNewsContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
