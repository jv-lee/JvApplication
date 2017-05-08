package com.jv.sms.base.module;

import com.jv.sms.rx.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/4/27.
 */
@Module
public class RxModule {

    @Singleton
    @Provides
    RxBus provideRxBus() {
        return RxBus.getInstance();
    }

}
