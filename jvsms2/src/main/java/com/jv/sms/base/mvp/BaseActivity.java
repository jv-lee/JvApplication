package com.jv.sms.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.jv.sms.base.app.App;
import com.jv.sms.base.app.AppComponent;
import com.jv.sms.rx.EventBase;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/10.
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder unBinder;


    @Inject
    protected P mPresenter;
    protected Context mContext;
    protected App mApplication;
    protected Observable<EventBase> mObservable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setThemes();
        super.onCreate(savedInstanceState);
        mApplication = (App) getApplication();
        mContext = this;

        setContentView(bindRootView());
        mObservable = getRxBus();
        unBinder = ButterKnife.bind(this);

        setupActivityComponent(mApplication.getAppComponent());
        bindData();
        rxEvent();
    }

    protected void setThemes(){

    }

    protected abstract int bindRootView();

    protected abstract void bindData();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    public abstract Observable<EventBase> getRxBus();

    protected abstract void rxEvent();
}
