package com.jv.daily.ui.main;

import com.google.gson.Gson;
import com.jv.daily.base.mvp.BaseModel;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.entity.NewsBean;
import com.jv.daily.entity.StoriesBean;
import com.jv.daily.entity.TopStoriesBean;
import com.jv.daily.db.dao.NewsDao;
import com.jv.daily.service.NewsService;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * Created by Administrator on 2017/4/11.
 */
@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {

    @Inject
    Gson gson;
    @Inject
    NewsDao dao;
    @Inject
    NewsService newsService;


    @Inject
    public MainModel() {
    }

    @Override
    public boolean inertTopDataToDb(List<TopStoriesBean> list, String date) {
        return dao.saveTopStories(list, date);
    }

    @Override
    public boolean insertDataToDb(List<StoriesBean> list, String date) {
        return dao.saveStories(list, date);
    }

    @Override
    public NewsBean refreshDataToDb(String date) {
        List<TopStoriesBean> topList = dao.findTopStoriesAll(date);
        List<StoriesBean> storiesList = dao.findStoriesAll(date);
        NewsBean newsBean = new NewsBean();
        newsBean.setDate(date);
        newsBean.setStories(storiesList);
        newsBean.setTop_stories(topList);
        return newsBean;
    }

    @Override
    public List<StoriesBean> loadDataToDb(String date) {
        return dao.findStoriesAll(date);
    }

    @Override
    public int findRefreshDataCount(String date) {
        return dao.findTopStoriesCount(date);
    }

    @Override
    public int findLoadDataCount(String date) {
        return dao.findStoriesCount(date);
    }

    @Override
    public Observable<NewsBean> refreshData() {
        return newsService.getNewsLatest();
    }

    @Override
    public Observable<NewsBean> loadNews(String date) {
        return newsService.getNewsBeforeByDate(date);
    }
}
