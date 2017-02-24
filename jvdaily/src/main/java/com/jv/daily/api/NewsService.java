package com.jv.daily.api;

import com.jv.daily.bean.NewsBean;

import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/24.
 */

public interface NewsService {

    @GET("news/latest")
    Observable<NewsBean> getNewsTop();

}
