package com.jv.daily.ui.content;

import com.jv.daily.base.module.ServiceModule;
import com.jv.daily.base.mvp.BasePresenter;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.entity.NewsContentBean;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 64118 on 2017/4/16.
 */
@ActivityScope
public class ContentPresenter extends BasePresenter<ContentContract.Model, ContentContract.View> implements ContentContract.Presenter {
    @Inject
    public ContentPresenter() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void loadWeb(String id) {
        mModel.loadWebBean(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsContentBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsContentBean bean) {
                        mView.loadWeb(bean);
                    }
                });
    }
}
