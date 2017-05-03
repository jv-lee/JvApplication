package com.jv.sms.ui.settings;

import android.util.Log;
import android.widget.Toast;

import com.jv.sms.base.mvp.BasePresenter;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.SettingBean;
import com.jv.sms.entity.SmsAppBean;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/3.
 */
@ActivityScope
public class SettingsPresenter extends BasePresenter<SettingsContract.Model, SettingsContract.View> implements SettingsContract.Presenter {

    @Inject
    public SettingsPresenter() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void findSettingBeans() {
        Observable.create(new Observable.OnSubscribe<List<SettingBean>>() {
            @Override
            public void call(Subscriber<? super List<SettingBean>> subscriber) {
                subscriber.onNext(mModel.findSettingBeans());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SettingBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<SettingBean> settingBeen) {
                        mView.setSettingsBeans(settingBeen);
                    }
                });
    }

    @Override
    public void clickDefaultSmsApplication() {
        Observable.create(new Observable.OnSubscribe<List<SmsAppBean>>() {
            @Override
            public void call(Subscriber<? super List<SmsAppBean>> subscriber) {
                subscriber.onNext(mModel.hasDefaultSmsApplication());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SmsAppBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<SmsAppBean> smsAppBeans) {
                        if (smsAppBeans == null) {
                            mView.notDefaultApplication();
                        } else {
                            mView.isDefaultApplication(smsAppBeans);
                        }
                    }
                });
    }
}
