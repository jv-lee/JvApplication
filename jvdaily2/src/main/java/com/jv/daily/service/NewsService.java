package com.jv.daily.service;


import com.jv.daily.entity.LaunchBean;
import com.jv.daily.entity.NewsBean;
import com.jv.daily.entity.NewsContentBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/2/24.
 */

public interface NewsService {

    //首页top广告轮播 接口
    @GET("4/news/latest")
    Observable<NewsBean> getNewsLatest();

    //首页广告列表 过往日期新闻 接口
    @GET("4/news/before/{date}")
    Observable<NewsBean> getNewsBeforeByDate(@Path("date") String date);

    //查询新闻详细消息
    @GET("4/news/{id}")
    Observable<NewsContentBean> getNewsContent(@Path("id") String id);

    //启动页图片
    @GET("7/prefetch-launch-images/{width}*{height}")
    Observable<LaunchBean> getLaunch(@Path("width") String width, @Path("height") String height);
}

