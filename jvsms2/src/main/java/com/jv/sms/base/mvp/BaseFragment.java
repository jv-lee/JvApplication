package com.jv.sms.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public abstract class BaseFragment<P extends IPresenter> extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected BaseActivity mActivity;
    protected View mRootView;

    protected Observable<EventBase> mObservable;

    @Inject
    protected P mPresenter;
    private Unbinder unBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate();
        mObservable = getRxBus();
        setHasOptionsMenu(true); // 在fragment中操作Menu
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = bindRootView(inflater, container, savedInstanceState);
        unBinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        bindData();
        rxEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != unBinder.EMPTY) {
            unBinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter = null;
        this.mActivity = null;
        this.mRootView = null;
        this.unBinder = null;
    }

    protected abstract void onCreate();

    protected abstract View bindRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void bindData();

    public abstract Observable<EventBase> getRxBus();

    protected abstract void rxEvent();

}
