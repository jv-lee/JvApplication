package com.jv.sms.ui.content;

import android.app.PendingIntent;
import android.util.Log;
import android.widget.Toast;

import com.jv.sms.base.mvp.BasePresenter;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.SmsBean;

import java.util.LinkedList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/28.
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
    public void findSmsBeansAll(String thread_id) {
        Observable.just(thread_id)
                .map(new Func1<String, LinkedList<SmsBean>>() {
                    @Override
                    public LinkedList<SmsBean> call(String thread_id) {
                        return mModel.findSmsBeansAll(thread_id);
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
                        mView.setSmsBeansAll(bean);
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
    public void sendSms(final PendingIntent sentPI, final String phoneNumber, final String content, final long time) {
        Observable.create(new Observable.OnSubscribe<SmsBean>() {
            @Override
            public void call(Subscriber<? super SmsBean> subscriber) {
                subscriber.onNext(mModel.sendSms(sentPI, phoneNumber, content, time));
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
                        Log.i("错误信息", e.getMessage());
                        Toast.makeText(mApplication, "发送短信启动代码错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mView.sendSmsLoading(smsBean);
                    }
                });
    }

    @Override
    public void sendSmsSuccess(SmsBean smsBean) {
        mView.sendSmsSuccess(smsBean);
    }

    @Override
    public void sendSmsError(final SmsBean smsbean) {
        Observable.create(new Observable.OnSubscribe<SmsBean>() {
            @Override
            public void call(Subscriber<? super SmsBean> subscriber) {
                subscriber.onNext(mModel.updateSmsStatus(smsbean));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("错误信息", e.getMessage());
                        Toast.makeText(mApplication, "db update sms status error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mView.sendSmsError(smsbean);
                    }
                });
    }

    @Override
    public void deleteSmsByThreadId(final String thread_id) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(mModel.removeSmsByThreadId(thread_id));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.deleteThreadError();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.deleteThreadSuccess();
                        } else {
                            mView.deleteThreadError();
                        }
                    }
                });
    }

    @Override
    public void reSendSms(final String id, final int position) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(mModel.deleteSmsListById(id));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mApplication, "重发短信 删除失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.reSendSms(aBoolean, position);
                    }
                });
    }
}