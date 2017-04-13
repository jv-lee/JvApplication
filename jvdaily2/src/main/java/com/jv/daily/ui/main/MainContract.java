package com.jv.daily.ui.main;

import com.jv.daily.base.mvp.IModle;
import com.jv.daily.base.mvp.IPresenter;
import com.jv.daily.base.mvp.IView;
import com.jv.daily.bean.NewsBean;
import com.jv.daily.bean.StoriesBean;
import com.jv.daily.bean.TopStoriesBean;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface MainContract {

    interface View extends IView {
        void initBanner(List<TopStoriesBean> list, List<String> images, List<String> titles);

        void initContent(List<StoriesBean> list);

        void refreshEvent(int code, String message);

        void loadNews(List<StoriesBean> list);

    }

    interface Presenter extends IPresenter {
        void refreshNews();

        void loadNews();
    }

    interface Model extends IModle {
        boolean inertTopDataToDb(List<TopStoriesBean> list, String date);

        boolean insertDataToDb(List<StoriesBean> list, String date);

        NewsBean initDataToDb(String date);

        int findDataCount(String date);

        Observable<NewsBean> initData();

        Observable<NewsBean> loadNews(String date);
    }
}
