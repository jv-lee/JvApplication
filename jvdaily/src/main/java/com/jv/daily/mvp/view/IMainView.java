package com.jv.daily.mvp.view;

import com.jv.daily.adapter.MultiTypeAdapter;
import com.jv.daily.entity.TopStoriesBean;
import com.jv.daily.mvp.module.NewsBean;
import com.jv.daily.widget.BannerView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public interface IMainView {
    void insertBannerView(List<TopStoriesBean> list, List<String> images, List<String> titles);

    void errorBannerView();

    void insertContentData(MultiTypeAdapter adapter);

    void loadNews();

    void refreshNews();

}
