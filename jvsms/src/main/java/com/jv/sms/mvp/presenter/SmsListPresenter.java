package com.jv.sms.mvp.presenter;

import android.app.Activity;
import android.app.PendingIntent;
import android.util.Log;

import com.jv.sms.bean.SmsBean;
import com.jv.sms.mvp.model.ISmsListModel;
import com.jv.sms.mvp.model.SmsListModel;
import com.jv.sms.mvp.view.ISmsListView;
import com.jv.sms.mvp.view.ISmsView;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/5.
 */

public class SmsListPresenter implements ISmsListPresenter {

    private ISmsListModel mModel;
    private ISmsListView mView;

    public SmsListPresenter() {
    }

    public SmsListPresenter(ISmsListView smsView) {
        mView = smsView;
        mModel = new SmsListModel();
    }

    @Override
    public void refreshSmsList(String thread_id) {
        Observable.just(thread_id)
                .map(new Func1<String, LinkedList<SmsBean>>() {
                    @Override
                    public LinkedList<SmsBean> call(String thread_id) {
                        return mModel.refreshSmsList(thread_id);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LinkedList<SmsBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showSmsListSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showSmsError();
                        Log.i("错误信息", e.getMessage());
                    }

                    @Override
                    public void onNext(LinkedList<SmsBean> bean) {
                        mView.refreshSmsList(bean);
                    }
                });
    }

    @Override
    public void removeSmsById(String id) {
        if (mModel.deleteSmsListById(id)) {
            mView.deleteSmsSuccess();
        } else {
            mView.deleteSmsError();
        }
    }

    @Override
    public void sendSms(final PendingIntent sentPI, final String phoneNumber, final String content) {
        Observable.create(new Observable.OnSubscribe<SmsBean>() {
            @Override
            public void call(Subscriber<? super SmsBean> subscriber) {
                subscriber.onNext(mModel.sendSms(sentPI, phoneNumber, content));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.sendSmsError();
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mView.sendSmsLoading(smsBean);
                    }
                });
    }

    @Override
    public void sendSmsSuccess() {
        mView.sendSmsSuccess();
    }

    @Override
    public void sendSmsError() {
        mView.sendSmsError();
    }
}
