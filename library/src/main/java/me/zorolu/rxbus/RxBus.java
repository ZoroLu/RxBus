package me.zorolu.rxbus;

import com.trello.rxlifecycle.LifecycleProvider;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by luguanquan on 2016/10/29.
 */

public class RxBus {

    private final Subject bus;

    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        return RxBusHolder.defInstance;
    }

    private static class RxBusHolder {
        static RxBus defInstance = new RxBus();
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T extends Object> Observable<T> toObservable(final Class<T> eventType) {
        return bus.ofType(eventType);
    }

    public <T extends Object, E> Observable<T> toAutoLifecycleObservable(LifecycleProvider<E> rxLifecycle, E untilEvent, final Class<T> eventType) {
        return bus.ofType(eventType).compose(rxLifecycle.<T>bindUntilEvent(untilEvent));
    }

    public <T extends Object, E> Observable<T> toAutoLifecycleObservable(LifecycleProvider<E> rxLifecycle, final Class<T> eventType) {
        return bus.ofType(eventType).compose(rxLifecycle.<T>bindToLifecycle());
    }
}
