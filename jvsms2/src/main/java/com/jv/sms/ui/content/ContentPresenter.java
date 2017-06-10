package com.jv.sms.ui.content;

import android.app.PendingIntent;
import android.util.Log;
import android.widget.Toast;

import com.jv.sms.base.mvp.BasePresenter;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.SmsBean;

import java.util.LinkedList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


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
        mModel.findSmsBeansAll(thread_id)
                .subscribe(new Observer<LinkedList<SmsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onComplete() {
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
        mModel.deleteSmsListById(id).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    mView.deleteSmsSuccess();
                } else {
                    mView.deleteSmsError();
                }
            }
        });
    }

    @Override
    public void sendSms(final PendingIntent sentPI, final String phoneNumber, final String content, final long time) {
        mModel.sendSms(sentPI, phoneNumber, content, time)
                .subscribe(new Observer<SmsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mView.sendSmsLoading(smsBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("错误信息", e.getMessage());
                        Toast.makeText(mApplication, "发送短信启动代码错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    @Override
    public void sendSmsSuccess(SmsBean smsBean) {
        mView.sendSmsSuccess(smsBean);
    }

    @Override
    public void sendSmsError(final SmsBean smsbean) {
        mModel.updateSmsStatus(smsbean)
                .subscribe(new Observer<SmsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
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

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    @Override
    public void deleteSmsByThreadId(final String thread_id) {
        mModel.removeSmsByThreadId(thread_id)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onError(Throwable e) {
                        mView.deleteThreadError();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
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
        mModel.deleteSmsListById(id)
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mApplication, "重发短信 删除失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mView.reSendSms(aBoolean, position);
                    }
                });
    }
}
