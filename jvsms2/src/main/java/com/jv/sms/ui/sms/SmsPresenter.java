package com.jv.sms.ui.sms;

import android.content.Context;
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
public class SmsPresenter extends BasePresenter<SmsContract.Model, SmsContract.View> implements SmsContract.Presenter {

    @Inject
    public SmsPresenter() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void findSmsAll() {

        Observable.just(mApplication)
                .map(new Func1<Context, LinkedList<SmsBean>>() {
                    @Override
                    public LinkedList<SmsBean> call(Context context1) {
                        return mModel.findSmsAll(context1);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LinkedList<SmsBean>>() {
                    @Override
                    public void onCompleted() {
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
                });
    }

    @Override
    public void removeSmsByThreadId(String id, int position) {
        if (mModel.removeSmsByThreadId(id)) {
            mView.removeDataSuccess(position);
        } else {
            mView.removeDataError();
        }
    }

    @Override
    public void getNewSms() {

        Observable.create(new Observable.OnSubscribe<SmsBean>() {
            @Override
            public void call(Subscriber<? super SmsBean> subscriber) {
                subscriber.onNext(mModel.getNewSms());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmsBean>() {
                    @Override
                    public void onCompleted() {

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
                });
    }

    @Override
    public void updateSmsState(SmsBean smsBean) {
        mModel.updateSmsState(smsBean);
    }


    @Override
    public void insertSmsNotification(String[] ids) {
        Observable.just(ids).map(new Func1<String[], Boolean>() {
            @Override
            public Boolean call(String[] strings) {
                return mModel.insertSmsDB(strings);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

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
                });
    }

    @Override
    public void deleteSmsNotification(String[] ids) {

    }
}
