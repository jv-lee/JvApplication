package com.jv.sms.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.fragment.SmsFragment;
import com.jv.sms.interfaces.DataLoadLayoutListener;

import butterknife.BindView;


public class SmsActivity extends BaseActivity implements
        View.OnClickListener, DataLoadLayoutListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_sms_loading)
    ProgressBar pbSmsLoading;
    @BindView(R.id.ll_sms_nullLayout)
    LinearLayout llSmsNullLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    @Override
    public int getContentViewId() {
        return R.layout.activity_sms;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.fl_sms_container, new SmsFragment(this)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
        }
    }

}
