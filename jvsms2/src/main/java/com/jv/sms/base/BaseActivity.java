package com.jv.sms.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import com.jv.sms.base.app.App;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/27.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder unBinder;

    protected Context mContext;
    protected App mApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (App) getApplication();
        mContext = this;

        setContentView(bindRootView());
        unBinder = ButterKnife.bind(this);

        setFragment();

        bindData();
    }

    protected void setFragment() {
    }

    protected abstract int bindRootView();

    protected abstract void bindData();

}
