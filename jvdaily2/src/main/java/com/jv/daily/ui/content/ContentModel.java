package com.jv.daily.ui.content;

import com.jv.daily.base.mvp.BaseModel;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.entity.NewsContentBean;
import com.jv.daily.service.NewsService;

import javax.inject.Inject;

import io.reactivex.Observable;


/**
 * Created by 64118 on 2017/4/17.
 */
@ActivityScope
public class ContentModel extends BaseModel implements ContentContract.Model {

    @Inject
    NewsService newsService;

    @Inject
    public ContentModel() {
    }

    @Override
    public Observable<NewsContentBean> loadWebBean(String id) {
        return newsService.getNewsContent(id);
    }
}
