package com.jv.sms.base.mvp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.jv.sms.base.app.App;
import com.jv.sms.base.app.AppComponent;
import com.jv.sms.rx.EventBase;
import com.jv.sms.rx.RxBus;
import com.jv.sms.swipe.SwipeBackActivityBase;
import com.jv.sms.swipe.SwipeBackActivityHelper;
import com.jv.sms.swipe.SwipeBackLayout;
import com.jv.sms.swipe.SwipeBackUtils;
import com.jv.sms.utils.StatusBarUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/10.
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements SwipeBackActivityBase {
    protected final String TAG = this.getClass().getSimpleName();
    protected Unbinder unBinder;

    //声明swipe布局属性
    protected SwipeBackLayout mSwipeBackLayout;
    protected SwipeBackActivityHelper mHelper;


    @Inject
    protected P mPresenter;
    protected Context mContext;
    protected App mApplication;
    protected Observable<EventBase> mObservable;

    protected RxBus rxBus;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setThemes();
        super.onCreate(savedInstanceState);
        mApplication = (App) getApplication();
        mContext = this;
        rxBus = RxBus.getInstance();
        rxBus.register(this);
        StatusBarUtils.setStatusBar(this);//设置隐藏状态栏

        setContentView(bindRootView());

        //获取swipe
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mSwipeBackLayout = getSwipeBackLayout();//获取swipe实例

        mObservable = getRxBus();
        unBinder = ButterKnife.bind(this);

        setupActivityComponent(mApplication.getAppComponent());
        bindData();
        rxEvent();
    }

    /**
     * 以下5个为 退出swipe 必备函数
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        SwipeBackUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxBus.register(this);
        unBinder.unbind();
        mObservable = null;
        mApplication = null;
        mContext = null;
    }

    protected void setThemes() {

    }

    protected abstract int bindRootView();

    protected abstract void bindData();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    public abstract Observable<EventBase> getRxBus();

    protected abstract void rxEvent();
}
