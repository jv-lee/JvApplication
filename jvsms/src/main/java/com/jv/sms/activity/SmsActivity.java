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
    @BindView(R.id.pb_sms_loading)
    ProgressBar pbSmsLoading;
    @BindView(R.id.ll_sms_nullLayout)
    LinearLayout llSmsNullLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private SearchView mSearchView;

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
        getSupportFragmentManager().beginTransaction().add(R.id.fl_sms_container, mFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setQueryHint("搜索信息");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                Toast.makeText(this, "设置功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_item_hideContacts:
                Toast.makeText(this, "屏蔽功能暂未开放", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDataBar() {
        pbSmsLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDataBar() {
        pbSmsLoading.setVisibility(View.GONE);
    }

    @Override
    public void showDataLayout() {
        llSmsNullLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDataLayout() {
        llSmsNullLayout.setVisibility(View.GONE);
    }

    @Override
    public int getToolbarHeight() {
        return toolbar.getMeasuredHeight();
    }

    @Override
    public SearchView getSearchBar() {
        return mSearchView;
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
