package com.jv.daily.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jv.daily.R;
import com.jv.daily.base.app.App;
import com.jv.daily.base.app.AppComponent;
import com.jv.daily.entity.LaunchBean;
import com.jv.daily.service.NewsService;
import com.jv.daily.ui.main.MainActivity;
import com.jv.daily.ui.welcome.inject.DaggerWelcomeComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/4/18.
 */

public class WelcomeActivity extends Activity {

    @Inject
    NewsService newsService;
    @BindView(R.id.iv_bg)
    ImageView ivBg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        registerComponent();

        newsService.getLaunch("1080", "1920")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LaunchBean>() {
                    @Override
                    public void onCompleted() {
                        startMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Glide.with(WelcomeActivity.this).load(R.drawable.welcome_icon).into(ivBg);
                        onCompleted();
                    }

                    @Override
                    public void onNext(LaunchBean launchBean) {
                        Log.w("WelcomeActivity", "onNext");
                        Glide.with(WelcomeActivity.this).load(launchBean.getCreatives().get(0).getUrl()).error(R.drawable.welcome_icon).into(ivBg);
                    }
                });
    }

    private void registerComponent() {
        ButterKnife.bind(this);
        DaggerWelcomeComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .build()
                .inject(this);
    }

    public void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }

}
