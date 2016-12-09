package com.jv.sms.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.adapter.SmsUiAdapter;
import com.jv.sms.utils.SmsUtils;


public class SmsActivity extends AppCompatActivity implements SearchView.OnSuggestionListener,
        View.OnClickListener, SearchView.OnCloseListener, DataLoadLayoutListener {

    private TabLayout mTab;
    private ViewPager mViewPager;
    private SearchView mSearchView;
    private FloatingActionButton fab;
    private TextView mTitle;
    private ProgressBar mPbDataBar;
    private LinearLayout mDataLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initView();
        initUi();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_content);
        mTab = (TabLayout) findViewById(R.id.tab_Layout);
        mSearchView = (SearchView) findViewById(R.id.main_search);
        mTitle = (TextView) findViewById(R.id.main_title);
        mPbDataBar = (ProgressBar) findViewById(R.id.pb_sms_loading);
        mDataLayout = (LinearLayout) findViewById(R.id.ll_null_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initUi() {
        mSearchView.setOnSuggestionListener(this);
        mSearchView.setOnSearchClickListener(this);
        mSearchView.setOnCloseListener(this);
        SmsUiAdapter adapter = new SmsUiAdapter(getSupportFragmentManager(), this,this);
        mViewPager.setAdapter(adapter);
        mTab.setupWithViewPager(mViewPager);
        adapter.setTabIcon(mTab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_search:
                mTitle.setText("");
                break;
            case R.id.fab:
                Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }

    @Override
    public boolean onClose() {
        mTitle.setText(R.string.app_name);
        return false;
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
}
