package com.jv.sms.ui.newsms.inject;

import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.newsms.NewSmsContract;
import com.jv.sms.ui.newsms.NewSmsModel;
import com.jv.sms.ui.newsms.NewSmsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/5/3.
 */
@Module
public class NewSmsModule {

    private NewSmsContract.View view;

    public NewSmsModule(NewSmsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    NewSmsContract.Model provideModel(NewSmsModel smsModel) {
        return smsModel;
    }

    @ActivityScope
    @Provides
    NewSmsContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    NewSmsContract.Presenter providePresenter(NewSmsPresenter smsPresenter) {
        return smsPresenter;
    }

}
