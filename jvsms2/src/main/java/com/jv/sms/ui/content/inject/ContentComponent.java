package com.jv.sms.ui.content.inject;

import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.content.ContentActivity;
import com.jv.sms.ui.content.ContentFragment;

import dagger.Component;

/**
 * Created by Administrator on 2017/4/28.
 */
@ActivityScope
@Component(modules = ContentModule.class, dependencies = AppComponent.class)
public interface ContentComponent {

    void inject(ContentFragment fragment);
}
