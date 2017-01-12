package com.jv.sms.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.mvp.model.ISmsModel;
import com.jv.sms.mvp.model.SmsModel;
import com.jv.sms.mvp.view.ISmsView;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/2.
 */

public class SmsPresenter implements ISmsPresenter {

    private ISmsModel mSmsModel;
    private ISmsView mSmsView;

    public SmsPresenter(ISmsView smsView) {
        mSmsModel = new SmsModel();
        mSmsView = smsView;
    }


    @Override
    public void findSmsAll() {

        Observable.just(JvApplication.getInstance())
                .map(new Func1<Context, LinkedList<SmsBean>>() {
                    @Override
                    public LinkedList<SmsBean> call(Context context1) {
                        return mSmsModel.findSmsAll(context1);
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
                        mSmsView.setDataError();
                        Log.e("错误信息：", e.getMessage());
                    }

                    @Override
                    public void onNext(LinkedList<SmsBean> smsBean) {
                        if (smsBean.size() == 0) {
                            mSmsView.setDataError();
                        } else {
                            mSmsView.setDataSuccess();
                        }
                        mSmsView.setData(smsBean);
                    }
                });
    }

    @Override
    public void removeSmsByThreadId(String id, int position) {
        if (mSmsModel.removeSmsByThreadId(id)) {
            mSmsView.removeDataSuccess(position);
        } else {
            mSmsView.removeDataError();
        }
    }

    @Override
    public void getNewSms() {

        Observable.create(new Observable.OnSubscribe<SmsBean>() {
            @Override
            public void call(Subscriber<? super SmsBean> subscriber) {
                subscriber.onNext(mSmsModel.getNewSms());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(JvApplication.getInstance(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("错误信息", e.getMessage());
                    }

                    @Override
                    public void onNext(SmsBean smsBean) {
                        mSmsView.setNewSms(smsBean);
                    }
                });
    }

    @Override
    public void updateSmsState(SmsBean smsBean) {
        mSmsModel.updateSmsState(smsBean);
    }


    @Override
    public void insertSmsNotification(String[] ids) {
        Observable.just(ids).map(new Func1<String[], Boolean>() {
            @Override
            public Boolean call(String[] strings) {
                return mSmsModel.insertSmsDB(strings);
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
                        mSmsView.insertSmsNotificationError();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mSmsView.insertSmsNotificationSuccess();
                        } else {
                            mSmsView.insertSmsNotificationError();
                        }
                    }
                });
    }

    @Override
    public void deleteSmsNotification(String[] ids) {

    }

}
