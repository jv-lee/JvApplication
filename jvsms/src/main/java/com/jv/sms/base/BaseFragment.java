package com.jv.sms.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jv.sms.rx.RxBus;

import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/14.
 */

public abstract class BaseFragment extends Fragment {

    protected Observable<EventBase> mObservable;

    protected Context mContext;
    protected View mRootView;

    public abstract int getContentViewId();

    public abstract Observable<EventBase> getRxBus();

    protected abstract void initAllView(Bundle savedInstanceState);

    protected abstract boolean onKeyDown(int keyCode, KeyEvent event);

    protected abstract void rxEvent();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObservable = getRxBus();
        setHasOptionsMenu(true); // 在fragment中操作Menu
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        mContext = getActivity();

        ButterKnife.bind(this, mRootView);
        initAllView(savedInstanceState);
        rxEvent();
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(this);
    }

}
