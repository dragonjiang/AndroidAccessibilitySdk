package com.demo.dragonjiang.accessilibility_sdk.utils;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author DragonJiang
 * @Date 2016/8/11
 * @Time 13:34
 * @description
 */
public class RxBus {
    private static volatile RxBus instance;

    private final Subject<Object, Object> mBus;

    public RxBus() {
        this.mBus = new SerializedSubject<>(PublishSubject.create());
    }

    public static final RxBus get() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }

        return instance;
    }

    public void post(Object o) {
        mBus.onNext(o);
    }

    public <T> Observable<T> tObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
}

