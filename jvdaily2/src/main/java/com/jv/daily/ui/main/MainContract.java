package com.jv.daily.ui.main;

import com.jv.daily.base.mvp.IModle;
import com.jv.daily.base.mvp.IPresenter;
import com.jv.daily.base.mvp.IView;
import com.jv.daily.entity.NewsBean;
import com.jv.daily.entity.StoriesBean;
import com.jv.daily.entity.TopStoriesBean;

import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface MainContract {

    interface View extends IView {
        void initBanner(List<TopStoriesBean> list, List<String> images, List<String> titles);

        void initContent(List<StoriesBean> list);

        void refreshEvent(String code, String message);

        void loadNews(List<StoriesBean> list);

    }

    interface Presenter extends IPresenter {
        void refreshNews();

        void loadNews();
    }

    interface Model extends IModle {
        boolean inertTopDataToDb(List<TopStoriesBean> list, String date);

        boolean insertDataToDb(List<StoriesBean> list, String date);

        NewsBean refreshDataToDb(String date);

        List<StoriesBean> loadDataToDb(String date);

        int findRefreshDataCount(String date);

        int findLoadDataCount(String date);

        Observable<NewsBean> refreshData();

        Observable<NewsBean> loadNews(String date);
    }
}
