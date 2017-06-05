package com.jv.daily.ui.content;

import com.jv.daily.base.mvp.IModel;
import com.jv.daily.base.mvp.IPresenter;
import com.jv.daily.base.mvp.IView;
import com.jv.daily.entity.NewsContentBean;

import io.reactivex.Observable;


/**
 * Created by 64118 on 2017/4/16.
 */

public interface ContentContract {

    interface View extends IView {
        void loadWeb(NewsContentBean bean);
    }

    interface Presenter extends IPresenter {
        void loadWeb(String id);
    }

    interface Model extends IModel {
        Observable<NewsContentBean> loadWebBean(String id);
    }


}
