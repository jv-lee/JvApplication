package com.jv.sms.ui.newsms;

import android.util.Log;

import com.jv.sms.base.mvp.BasePresenter;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.ContactsBean;
import com.jv.sms.entity.LinkmanBean;
import com.jv.sms.entity.SmsBean;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
        mModel.findContactsAll()
                .subscribe(new Observer<List<ContactsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
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

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    @Override
    public void findLinkmanAll() {
        mModel.findLinkmanAll()
                .subscribe(new Observer<List<LinkmanBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
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

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    @Override
    public void findLinkmanByPhoneNumber(String phoneNumber) {
        mModel.findLinkmanByPhoneNumber(phoneNumber)
                .subscribe(new Observer<SmsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("错误日志", e.getMessage());
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mView.startListSmsBySms(smsBean);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }
}
