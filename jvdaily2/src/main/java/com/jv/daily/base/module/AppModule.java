package com.jv.daily.base.module;

import android.app.Application;

import com.google.gson.Gson;
import com.jv.daily.base.app.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/10.
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

    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }

}
