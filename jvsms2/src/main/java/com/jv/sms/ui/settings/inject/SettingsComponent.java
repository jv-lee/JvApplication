package com.jv.sms.ui.settings.inject;

import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.settings.SettingsActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/5/3.
 */
@ActivityScope
@Component(modules = SettingsModule.class, dependencies = AppComponent.class)
public interface SettingsComponent {

    void inject(SettingsActivity activity);

}
