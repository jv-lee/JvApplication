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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.adapter.SmsUiAdapter;
import com.jv.sms.utils.SmsUtils;


public class SmsActivity extends AppCompatActivity implements SearchView.OnSuggestionListener,
        View.OnClickListener, SearchView.OnCloseListener{

    private TabLayout mTab;
    private ViewPager mViewPager;
    private SearchView mSearchView;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initView();
        initUi();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_content);
        mTab = (TabLayout) findViewById(R.id.tab_Layout);
        mSearchView = (SearchView) findViewById(R.id.main_search);
        mTitle = (TextView) findViewById(R.id.main_title);
        mSearchView.setOnSuggestionListener(this);
        mSearchView.setOnSearchClickListener(this);
        mSearchView.setOnCloseListener(this);
    }

    private void initUi() {
        SmsUiAdapter adapter = new SmsUiAdapter(getSupportFragmentManager(), this);
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
        }
    }

    @Override
    public boolean onClose() {
        mTitle.setText(R.string.app_name);
        return false;
    }


}
