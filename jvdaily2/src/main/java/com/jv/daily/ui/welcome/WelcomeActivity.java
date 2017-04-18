package com.jv.daily.ui.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jv.daily.R;
import com.jv.daily.service.NewsService;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/4/18.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Inject
    NewsService newsService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }
}
