package com.jv.daily.net;

import com.jv.daily.api.NewsService;
import com.jv.daily.mvp.module.NewsBean;
import com.jv.daily.mvp.module.NewsContentBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/2/24.
 */

public class RetrofitUtils {
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private NewsService newsService;

    //私有化Retrofit 构造方法   创建Retrofit初始化  提供单列方法
    private RetrofitUtils() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder().client(httpClientBuilder)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        newsService = retrofit.create(NewsService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final RetrofitUtils INSTANCE = new RetrofitUtils();
    }

    //获取单例
    public static RetrofitUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getNewsLatest(Subscriber<NewsBean> subscriber) {
        newsService.getNewsLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getNewsBeforeByDate(Subscriber<NewsBean> subscriber, String date) {
        newsService.getNewsBeforeByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getNewsContentById(Subscriber<NewsContentBean> subscriber, String id) {
        newsService.getNewsContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
