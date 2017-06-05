package com.jv.daily.api;

import com.jv.daily.mvp.module.NewsBean;
import com.jv.daily.mvp.module.NewsContentBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/2/24.
 */

public interface NewsService {

    //首页top广告轮播 接口
    @GET("news/latest")
    Observable<NewsBean> getNewsLatest();

    //首页广告列表 过往日期新闻 接口
    @GET("news/before/{date}")
    Observable<NewsBean> getNewsBeforeByDate(@Path("date") String date);

    //查询新闻详细消息
    @GET("news/{id}")
    Observable<NewsContentBean> getNewsContent(@Path("id") String id);

}
