package com.jv.sms.rx;


import android.support.annotation.NonNull;

import com.jv.sms.base.EventBase;

import java.util.Vector;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2016/11/29.
 */

public class RxBus {

    private volatile static RxBus mRxBus = null;

    private Vector<Subject> mSubjects = new Vector<>();

    private RxBus() {
    }

    public static RxBus getInstance() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    public synchronized <T> Observable<T> register(@NonNull Object object) {
        Subject<T, T> subject = PublishSubject.create();
        mSubjects.add(subject);
        return subject;
    }

    public synchronized void unregister(Object object) {
        mSubjects.remove(object);
    }

    public void post(@NonNull EventBase content) {
        synchronized (this) {
            for (Subject subject : mSubjects) {
                if (subject != null) {
                    subject.onNext(content);
                }
            }
        }
    }

}
