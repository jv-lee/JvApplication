package com.jv.sms.ui.settings.inject;

import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.settings.SettingsContract;
import com.jv.sms.ui.settings.SettingsModel;
import com.jv.sms.ui.settings.SettingsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/5/3.
 */
@Module
public class SettingsModule {

    private SettingsContract.View view;

    public SettingsModule(SettingsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    SettingsContract.Model provideModel(SettingsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    SettingsContract.View provideView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SettingsContract.Presenter providePresenter(SettingsPresenter presenter) {
        return presenter;
    }

}
