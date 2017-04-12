package com.jv.daily.base.module;

import com.jv.daily.service.NewsService;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
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
    private static final long DEFAULT_TIMEOUT = 1; //连接时间为最小
    private static final long READ_TIMEOUT = 15;
    private static final long WRITE_TIMEOUT = 15;

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    @Singleton
    @Provides
    NewsService provideNewsService(Retrofit retrofit) {
        return retrofit.create(NewsService.class);
    }


}
