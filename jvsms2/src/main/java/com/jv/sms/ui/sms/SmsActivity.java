package com.jv.sms.ui.sms;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jv.sms.R;
import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.mvp.BaseActivity;
import com.jv.sms.interfaces.DataLoadLayoutListener;
import com.jv.sms.rx.EventBase;
import com.jv.sms.ui.newsms.NewSmsActivity;
import com.jv.sms.ui.sms.inject.DaggerSmsComponent;
import com.jv.sms.ui.sms.inject.SmsModule;
import com.jv.sms.utils.IntentUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import swipebacklayout.SwipeBackLayout;

/**
 * Created by Administrator on 2017/4/28.
 */

public class SmsActivity extends BaseActivity<SmsContract.Presenter> implements DataLoadLayoutListener {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.pb_loadBar)
    ProgressBar pbLoadBar;
    @BindView(R.id.ll_nullLayout)
    LinearLayout llNullLayout;

    SmsFragment smsFragment;

    @Override
    protected int bindRootView() {
        return R.layout.activity_sms;
    }

    @Override
    protected void bindData() {
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.STATE_IDLE);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        smsFragment = new SmsFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, smsFragment).commit();
        DaggerSmsComponent
                .builder()
                .appComponent(appComponent)
                .smsModule(new SmsModule(smsFragment))
                .build()
                .inject(smsFragment);
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return null;
    }

    @Override
    protected void rxEvent() {

    }

    @Override
    public void showDataBar() {
        pbLoadBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDataBar() {
        pbLoadBar.setVisibility(View.GONE);
    }

    @Override
    public void showDataLayout() {
        llNullLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDataLayout() {
        llNullLayout.setVisibility(View.GONE);
    }

    @Override
    public int getToolbarHeight() {
        return toolbar.getMeasuredHeight();
    }

    @OnClick(R.id.fab)
    public void fabOnclick(View view) {
        IntentUtil.startActivity(this, NewSmsActivity.class);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (smsFragment != null && smsFragment instanceof SmsFragment) {
            //执行fragment 中onKeyDown事件返回false时消费事件 执行finish
            if (!(smsFragment).onKeyDown(keyCode, event)) {
                finish();
            } else {
                //否则不处理事件 拦截只执行fargment中取消选中状态事件
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
