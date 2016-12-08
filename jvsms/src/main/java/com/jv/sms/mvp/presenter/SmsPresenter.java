package com.jv.sms.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SmsBean;
import com.jv.sms.mvp.model.ISmsModel;
import com.jv.sms.mvp.model.SmsModel;
import com.jv.sms.mvp.view.ISmsView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(new Func1<Context, List<SmsBean>>() {
                    @Override
                    public List<SmsBean> call(Context context1) {
                        return mSmsModel.findSmsAll(context1);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SmsBean>>() {
                    @Override
                    public void onCompleted() {
                        mSmsView.setDataSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSmsView.setDataError();
                        Log.e("错误信息：", e.getMessage());
                    }

                    @Override
                    public void onNext(List<SmsBean> smsBean) {
                        mSmsView.setData(smsBean);
                    }
                });

    }

    @Override
    public void removeSmsByThreadId( String id) {
        if(mSmsModel.removeSmsByThreadId(id)){
            mSmsView.removeDataSuccess();
        }else{
            mSmsView.removeDataError();
        }
    }
}
