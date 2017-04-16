package com.jv.daily.ui.content.inject;

import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.ui.content.ContentContract;
import com.jv.daily.ui.content.ContentModel;
import com.jv.daily.ui.content.ContentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 64118 on 2017/4/16.
 */
@Module
public class ContentModule {

    private ContentContract.View view;

    public ContentModule(ContentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ContentContract.Model provideModel(ContentModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    ContentContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ContentContract.Presenter providePresenter(ContentPresenter presenter) {
        return presenter;
    }

}
