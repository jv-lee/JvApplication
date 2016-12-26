package com.jv.sms.mvp.presenter;

import android.util.Log;

import com.jv.sms.bean.ContactsBean;
import com.jv.sms.bean.LinkmanBean;
import com.jv.sms.fragment.NewSmsFragment;
import com.jv.sms.mvp.model.INewSmsModel;
import com.jv.sms.mvp.model.NewSmsModel;
import com.jv.sms.mvp.view.INewSmsView;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/23.
 */

public class NewSmsPresenter implements INewSmsPresenter {

    private INewSmsModel mModel;
    private INewSmsView mView;

    public NewSmsPresenter(INewSmsView mView) {
        mModel = new NewSmsModel();
        this.mView = mView;
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
}
