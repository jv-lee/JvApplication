package com.jv.sms.ui.sms;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jv.sms.base.mvp.BasePresenter;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.SmsBean;

import java.util.LinkedList;
import java.util.List;

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
public class SmsPresenter extends BasePresenter<SmsContract.Model, SmsContract.View> implements SmsContract.Presenter {

    @Inject
    public SmsPresenter() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void findSmsAll() {
        mModel.findSmsAll(mApplication)
                .subscribe(new Observer<LinkedList<SmsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.setDataError();
                        Log.e("错误信息：", e.getMessage());
                    }

                    @Override
                    public void onNext(LinkedList<SmsBean> smsBean) {
                        if (smsBean.size() == 0) {
                            mView.setDataError();
                        } else {
                            mView.setDataSuccess();
                        }
                        mView.setData(smsBean);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });

    }

    @Override
    public void removeSmsByThreadId(String id, final int position) {
        mModel.removeSmsByThreadId(id)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mView.removeDataSuccess(position);
                        } else {
                            mView.removeDataError();
                        }
                    }
                });
    }

    @Override
    public void getNewSms() {
        mModel.getNewSms()
                .subscribe(new Observer<SmsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mApplication, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("错误信息", e.getMessage());
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mView.setNewSms(smsBean);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });

    }

    @Override
    public void updateSmsState(SmsBean smsBean) {
        mModel.updateSmsState(smsBean);
    }


    @Override
    public void insertSmsNotification(String[] ids) {
        Observable.just(ids).map(new Function<String[], Boolean>() {
            @Override
            public Boolean apply(@NonNull String[] strings) throws Exception {
                return mModel.insertSmsDB(strings);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.insertSmsNotificationError();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.insertSmsNotificationSuccess();
                        } else {
                            mView.insertSmsNotificationError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    @Override
    public void deleteSmsNotification(String[] ids) {

    }
}
