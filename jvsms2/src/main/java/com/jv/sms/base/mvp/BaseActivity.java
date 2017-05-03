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
import swipebacklayout.SwipeBackActivity;
import swipebacklayout.SwipeBackLayout;

/**
 * Created by Administrator on 2017/4/10.
 */

public abstract class BaseActivity<P extends IPresenter> extends SwipeBackActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected Unbinder unBinder;
    protected SwipeBackLayout mSwipeBackLayout;


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

        mSwipeBackLayout = getSwipeBackLayout();
        //设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(200);
        //设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTTOM);

        setupActivityComponent(mApplication.getAppComponent());
        bindData();
        rxEvent();
    }

    protected void setThemes() {

    }

    protected abstract int bindRootView();

    protected abstract void bindData();

    protected abstract void setupActivityComponent(AppComponent appComponent);

    public abstract Observable<EventBase> getRxBus();

    protected abstract void rxEvent();
}
