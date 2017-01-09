package com.jv.sms.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.fragment.SmsFragment;
import com.jv.sms.fragment.SmsListFragment;
import com.jv.sms.interfaces.DataLoadLayoutListener;

import butterknife.BindView;
import butterknife.OnClick;


public class SmsActivity extends BaseActivity implements DataLoadLayoutListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_loadBar)
    ProgressBar pbLoadBar;
    @BindView(R.id.ll_nullLayout)
    LinearLayout llNullLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Fragment mFragment;

    @Override
    public int getContentViewId() {
        return R.layout.activity_sms;
    }

    @Override
    protected void setThemes() {
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        mFragment = new SmsFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, mFragment).commit();
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
//        Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        startActivity(new Intent(this, NewSmsActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFragment != null && mFragment instanceof SmsFragment) {
            //执行fragment 中onKeyDown事件返回false时消费事件 执行finish
            if (!((SmsFragment) mFragment).onKeyDown(keyCode, event)) {
                finish();
            } else {
                //否则不处理事件 拦截只执行fargment中取消选中状态事件
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
