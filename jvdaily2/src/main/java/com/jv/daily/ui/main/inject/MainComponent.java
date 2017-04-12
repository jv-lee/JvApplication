package com.jv.daily.ui.main.inject;

import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.ui.main.MainActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/4/11.
 */
@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);
}
