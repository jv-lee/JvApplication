package com.jv.sms.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.jv.sms.R;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.fragment.NewSmsFragment;

public class NewSmsActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_newsms;
    }

    @Override
    protected void setThemes() {
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_newSms_container, new NewSmsFragment()).commit();
    }

}
