package com.jv.sms.mvp.presenter;

import android.widget.Toast;

import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.SettingBean;
import com.jv.sms.bean.SmsAppBean;
import com.jv.sms.mvp.model.ISettingsModel;
import com.jv.sms.mvp.model.SettingsModel;
import com.jv.sms.mvp.view.ISettingsView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/1/11.
 */

public class SettingsPresenter implements ISettingsPresenter {

    private ISettingsView mView;
    private ISettingsModel mModel;

    public SettingsPresenter(ISettingsView view) {
        this.mView = view;
        mModel = new SettingsModel();
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
                        Toast.makeText(JvApplication.getInstance(), "错误信息：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(JvApplication.getInstance(), "错误信息：" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
