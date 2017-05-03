package com.jv.sms.ui.newsms.inject;

import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.scope.ActivityScope;
import com.jv.sms.ui.newsms.NewSmsActivity;
import com.jv.sms.ui.newsms.NewSmsFragment;

import dagger.Component;

/**
 * Created by Administrator on 2017/5/3.
 */
@ActivityScope
@Component(modules = NewSmsModule.class, dependencies = AppComponent.class)
public interface NewSmsComponent {

    void inject(NewSmsActivity newSmsActivity);

    void inject(NewSmsFragment newSmsfragment);

}
