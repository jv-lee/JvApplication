package com.jv.sms.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.jv.sms.R;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.fragment.SmsListFragment;

import butterknife.BindView;

public class SmsListActivity extends BaseActivity implements ToolbarSetListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_sms_list;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_smsList_container, new SmsListFragment(this)).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sms_list_menu, menu);
        return true;
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public int getToolbarHeight() {
        return toolbar.getMeasuredHeight();
    }

}
