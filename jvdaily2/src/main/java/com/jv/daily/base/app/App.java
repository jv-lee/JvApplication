package com.jv.daily.base.app;

import android.app.Application;


/**
 * Created by Administrator on 2017/4/10.
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
