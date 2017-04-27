package com.jv.sms.base.module;

import android.app.Application;

import com.jv.sms.base.app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/27.
 */
@Module
public class AppModule {

    private Application mApplication;

    public AppModule(App application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

}
