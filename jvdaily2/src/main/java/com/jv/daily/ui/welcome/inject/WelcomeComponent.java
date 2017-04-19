package com.jv.daily.ui.welcome.inject;

import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.ui.welcome.WelcomeActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/4/19.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface WelcomeComponent {
    void inject(WelcomeActivity activity);
}
