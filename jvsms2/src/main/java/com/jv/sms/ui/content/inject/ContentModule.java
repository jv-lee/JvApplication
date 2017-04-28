package com.jv.sms.ui.content.inject;

import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.content.ContentContract;
import com.jv.sms.ui.content.ContentModel;
import com.jv.sms.ui.content.ContentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/28.
 */
@Module
public class ContentModule {

    private ContentContract.View view;

    public ContentModule(ContentContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ContentContract.Model provideModel(ContentModel smsModel) {
        return smsModel;
    }

    @ActivityScope
    @Provides
    ContentContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ContentContract.Presenter providePresenter(ContentPresenter smsPresenter) {
        return smsPresenter;
    }

}
