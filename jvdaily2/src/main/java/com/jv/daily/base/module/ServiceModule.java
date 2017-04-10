package com.jv.daily.base.module;

import com.jv.daily.service.NewsService;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/10.
 */

@Module
public class ServiceModule {

    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";
    private static final int DEFAULT_TIMEOUT = 5;

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    NewsService providesNewsService(Retrofit retrofit) {
        return retrofit.create(NewsService.class);
    }


}
