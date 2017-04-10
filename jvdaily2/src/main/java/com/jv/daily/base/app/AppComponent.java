package com.jv.daily.base.app;

import android.app.Application;

import com.google.gson.Gson;
import com.jv.daily.base.module.AppModule;
import com.jv.daily.base.module.ServiceModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2017/4/10.
 */
@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface AppComponent {

    Application application();

    Gson gson();

}
