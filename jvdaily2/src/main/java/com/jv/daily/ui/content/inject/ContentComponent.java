package com.jv.daily.ui.content.inject;

import com.jv.daily.base.app.AppComponent;
import com.jv.daily.base.scope.ActivityScope;
import com.jv.daily.ui.content.ContentActivity;

import dagger.Component;

/**
 * Created by 64118 on 2017/4/17.
 */

@ActivityScope
@Component(modules = ContentModule.class, dependencies = AppComponent.class)
public interface ContentComponent {

    void inject(ContentActivity activity);
}
