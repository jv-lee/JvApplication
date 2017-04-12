package com.jv.daily.ui.main.inject;

import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.ui.main.MainContract;
import com.jv.daily.ui.main.MainModel;
import com.jv.daily.ui.main.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/11.
 */
@Module
public class MainModule {

    private MainContract.View view;

    public MainModule(MainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MainContract.Model provideModel(MainModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    MainContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MainContract.Presenter providePresenter(MainPresenter presenter) {
        return presenter;
    }

}
