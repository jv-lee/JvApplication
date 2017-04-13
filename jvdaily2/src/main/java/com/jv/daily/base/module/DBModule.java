package com.jv.daily.base.module;

import android.app.Application;

import com.jv.daily.db.DBHelper;
import com.jv.daily.db.dao.NewsDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/13.
 */
@Module
public class DBModule {

    @Singleton
    @Provides
    DBHelper provideDBHelper(Application application) {
        return new DBHelper(application);
    }

    @Singleton
    @Provides
    NewsDao provideNewsDao(DBHelper dbHelper) {
        return new NewsDao(dbHelper);
    }
}
