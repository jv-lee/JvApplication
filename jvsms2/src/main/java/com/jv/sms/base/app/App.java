package com.jv.sms.base.app;

import android.app.Application;

import com.jv.sms.base.module.AppModule;
import com.jv.sms.base.module.DbModule;
import com.jv.sms.base.module.RxModule;
import com.jv.sms.utils.SPUtil;


/**
 * Created by Administrator on 2017/4/27.
 */

public class App extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .rxModule(new RxModule())
                .build();
        SPUtil.getInstance(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
