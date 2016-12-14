package com.jv.sms.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/14.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getContentViewId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initAllView(savedInstanceState);
    }

    protected abstract void initAllView(Bundle savedInstanceState);

}
