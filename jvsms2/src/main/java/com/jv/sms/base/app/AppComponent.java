package com.jv.sms.base.app;

import android.app.Application;

import com.jv.sms.base.module.AppModule;
import com.jv.sms.base.module.DbModule;
import com.jv.sms.base.module.RxModule;
import com.jv.sms.db.dao.SmsDaoImpl;
import com.jv.sms.rx.RxBus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2017/4/27.
 */
@Singleton
@Component(modules = {AppModule.class, DbModule.class, RxModule.class})
public interface AppComponent {

    Application application();

    SmsDaoImpl smsDao();

    RxBus rxBus();
}
