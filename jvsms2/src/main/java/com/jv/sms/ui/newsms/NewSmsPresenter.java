package com.jv.sms.ui.newsms;

import android.util.Log;

import com.jv.sms.base.mvp.BasePresenter;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.ContactsBean;
import com.jv.sms.entity.LinkmanBean;
import com.jv.sms.entity.SmsBean;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/3.
 */
@ActivityScope
public class NewSmsPresenter extends BasePresenter<NewSmsContract.Model, NewSmsContract.View> implements NewSmsContract.Presenter {

    @Inject
    public NewSmsPresenter() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void findContactsAll() {

        Observable.create(new Observable.OnSubscribe<List<ContactsBean>>() {
            @Override
            public void call(Subscriber<? super List<ContactsBean>> subscriber) {
                subscriber.onNext(mModel.findContactsAll());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ContactsBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("NewSmsPresenter", e.getMessage());
                        mView.findContactsAllError();
                    }

                    @Override
                    public void onNext(List<ContactsBean> contactsBeen) {
                        mView.setContactsAll(contactsBeen);
                    }
                });
    }

    @Override
    public void findLinkmanAll() {
        Observable.create(new Observable.OnSubscribe<List<LinkmanBean>>() {
            @Override
            public void call(Subscriber<? super List<LinkmanBean>> subscriber) {
                subscriber.onNext(mModel.findLinkmanAll());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<LinkmanBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("NewSmsPresenter", e.getMessage());
                        mView.findLinkmanAllError();
                    }

                    @Override
                    public void onNext(List<LinkmanBean> linkmanBeen) {
                        mView.setLinkmanAll(linkmanBeen);
                    }
                });
    }

    @Override
    public void findLinkmanByPhoneNumber(final String phoneNumber) {
        Observable.just(phoneNumber).map(new Func1<String, SmsBean>() {
            @Override
            public SmsBean call(String s) {
                return mModel.findLinkmanByPhoneNumber(phoneNumber);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("错误日志", e.getMessage());
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mView.startListSmsBySms(smsBean);
                    }
                });
    }
}
