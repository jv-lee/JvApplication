package com.jv.daily.ui.main;

import com.google.gson.Gson;
import com.jv.daily.base.mvp.BaseModle;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.bean.NewsBean;
import com.jv.daily.manager.ServiceManager;


import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Administrator on 2017/4/11.
 */
@ActivityScope
public class MainModel extends BaseModle implements MainContract.Model {

    @Inject
    ServiceManager mServiceManager;
    @Inject
    Gson gson;

    @Inject
    public MainModel() {
    }

    @Override
    public Observable<NewsBean> initData() {
        return mServiceManager.mNewsService.getNewsLatest();
    }

    @Override
    public Observable<NewsBean> loadNews(String date) {
        return mServiceManager.mNewsService.getNewsBeforeByDate(date);
    }
}
