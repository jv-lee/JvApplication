package com.jv.sms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2016/12/14.
 */

public abstract class BaseActivity extends SwipeBackActivity {

    public abstract int getContentViewId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setThemes();
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initAllView(savedInstanceState);
    }

    protected abstract void setThemes();

    protected abstract void initAllView(Bundle savedInstanceState);

}
