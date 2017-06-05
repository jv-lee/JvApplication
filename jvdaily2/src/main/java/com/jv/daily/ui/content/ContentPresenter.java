package com.jv.daily.ui.content;

import android.util.Log;

import com.jv.daily.base.mvp.BasePresenter;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.entity.NewsContentBean;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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
        Log.w(TAG, "loadWeb");
        mModel.loadWebBean(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsContentBean>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onNext(NewsContentBean bean) {
                        mView.loadWeb(bean);
                    }
                });
    }
}
