package com.jv.daily.base.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jv.daily.base.app.App;
import com.jv.daily.base.app.AppComponent;
import com.jv.daily.utils.StatusBarUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (App) getApplication();
        mContext = this;
        StatusBarUtils.setStatusBar(this);
        setContentView(bindRootView());
        unBinder = ButterKnife.bind(this);

        setFragment();

        setupActivityComponent(mApplication.getAppComponent());
        bindData();
    }

    protected void setFragment() {
    }

    protected abstract int bindRootView();

    protected abstract void bindData();

    protected abstract void setupActivityComponent(AppComponent appComponent);
}
