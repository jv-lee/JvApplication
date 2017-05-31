package com.jv.sms.ui.newsms;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jv.sms.R;
import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.mvp.BaseActivity;
import com.jv.sms.rx.EventBase;
import com.jv.sms.swipe.SwipeBackLayout;
import com.jv.sms.ui.newsms.inject.DaggerNewSmsComponent;
import com.jv.sms.ui.newsms.inject.NewSmsModule;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/28.
 */

public class NewSmsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int bindRootView() {
        return R.layout.activity_newsms;
    }

    @Override
    protected void bindData() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        Fragment fragment = new NewSmsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, fragment).commit();
        DaggerNewSmsComponent
                .builder()
                .appComponent(appComponent)
                .newSmsModule(new NewSmsModule((NewSmsContract.View) fragment))
                .build()
                .inject((NewSmsFragment) fragment);
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return null;
    }

    @Override
    protected void rxEvent() {
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
