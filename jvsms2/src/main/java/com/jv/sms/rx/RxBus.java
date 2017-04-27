package com.jv.sms.rx;


import android.support.annotation.NonNull;

import java.util.Vector;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2016/11/29.
 */

public class RxBus {


    private Vector<Subject> mSubjects = new Vector<>();

    public RxBus() {
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