package com.jv.sms.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.fragment.SmsFragment;
import com.jv.sms.utils.SizeUtils;


public class SmsActivity extends AppCompatActivity implements
        View.OnClickListener, DataLoadLayoutListener {

    private FloatingActionButton fab;
    private ProgressBar mPbDataBar;
    private LinearLayout mDataLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        initView();
        getSupportFragmentManager().beginTransaction().add(R.id.act_sms_container, new SmsFragment(this)).commit();
    }

    private void initView() {
        mPbDataBar = (ProgressBar) findViewById(R.id.pb_sms_loading);
        mDataLayout = (LinearLayout) findViewById(R.id.ll_null_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
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
        mPbDataBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDataBar() {
        mPbDataBar.setVisibility(View.GONE);
    }

    @Override
    public void showDataLayout() {
        mDataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDataLayout() {
        mDataLayout.setVisibility(View.GONE);
    }

    @Override
    public int getToolbarHeight() {
        return mToolbar.getMeasuredHeight();
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
