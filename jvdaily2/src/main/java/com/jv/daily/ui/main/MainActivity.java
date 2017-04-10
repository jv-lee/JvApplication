package com.jv.daily.ui.main;


import com.jv.daily.R;
import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.mvp.BaseActivity;

public class MainActivity extends BaseActivity<MainContract.Presenter> implements MainContract.View {

    @Override
    protected int bindRootView() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindData() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }
}
