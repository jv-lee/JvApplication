package com.jv.daily.base.app;

import android.app.Application;

import com.jv.daily.base.module.AppModule;
import com.jv.daily.base.module.ServiceModule;


/**
 * Created by Administrator on 2017/4/10.
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .serviceModule(new ServiceModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
