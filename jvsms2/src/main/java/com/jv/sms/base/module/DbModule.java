package com.jv.sms.base.module;

import android.app.Application;

import com.jv.sms.db.DBHelper;
import com.jv.sms.db.dao.SmsDaoImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/27.
 */
@Module
public class DbModule {

    @Singleton
    @Provides
    DBHelper provideDBHelper(Application application) {
        return new DBHelper(application);
    }

    @Singleton
    @Provides
    SmsDaoImpl provideSmsDao(DBHelper dbHelper) {
        return new SmsDaoImpl(dbHelper);
    }

}
