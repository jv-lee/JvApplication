package com.jv.sms.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jv.sms.R;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.fragment.NewSmsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewSmsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, new NewSmsFragment()).commit();
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

}
