package com.jv.daily.net;

import android.content.Context;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/2/24.
 * 提供网络请求 订阅类 实现Progress进度框接口
 */

public class RetrofitSubscriber<T> extends Subscriber<T> implements CancelNetWorkListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;

    public RetrofitSubscriber( SubscriberOnNextListener subscriberOnNextListener) {
        this.mSubscriberOnNextListener = subscriberOnNextListener;
    }


    @Override
    public void onStart() {
    }

    @Override
    public void onCompleted() {
        mSubscriberOnNextListener.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        mSubscriberOnNextListener.onError(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        mSubscriberOnNextListener.onNext(t);
    }

    @Override
    public void onCancelProgress() {
        //在这里取消订阅
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    /**
     * 处理数据 回调接口
     *
     * @param <T>
     */
    public interface SubscriberOnNextListener<T> {
        void onNext(T t);

        void onError(String msg);

        void onCompleted();
    }

}
