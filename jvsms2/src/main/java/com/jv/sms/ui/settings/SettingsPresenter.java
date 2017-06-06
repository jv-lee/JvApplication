package com.jv.sms.ui.settings;

import android.util.Log;
import android.widget.Toast;

import com.jv.sms.base.mvp.BasePresenter;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.entity.SettingBean;
import com.jv.sms.entity.SmsAppBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        mModel.findSettingBeans()
                .subscribe(new Observer<List<SettingBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<SettingBean> settingBeen) {
                        mView.setSettingsBeans(settingBeen);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }

    @Override
    public void clickDefaultSmsApplication() {
        mModel.hasDefaultSmsApplication()
                .subscribe(new Observer<List<SmsAppBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                            d.dispose();
                        }
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

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }
}
