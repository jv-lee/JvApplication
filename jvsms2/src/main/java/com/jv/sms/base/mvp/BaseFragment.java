package com.jv.sms.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jv.sms.base.app.App;
import com.jv.sms.base.app.AppComponent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/4/10.
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected BaseActivity mActivity;
    protected View mRootView;

    @Inject
    protected P mPresenter;
    private Unbinder unBinder;

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
        componentInject(((App) getActivity().getApplication()).getAppComponent());
        bindData();
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

    protected abstract void componentInject(AppComponent appComponent);

    protected abstract View bindRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void bindData();

}
