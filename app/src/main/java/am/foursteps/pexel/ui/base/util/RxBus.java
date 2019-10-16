package am.foursteps.pexel.ui.base.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    private static RxBus mInstance;

    public static RxBus getInstance() {
        if (mInstance == null) {
            mInstance = new RxBus();
        }
        return mInstance;
    }

    private RxBus() {
    }

    private PublishSubject<Object> publisher = PublishSubject.create();

    public void publish(Object  object) {
        publisher.onNext(object);
    }

    // Listen should return an Observable
    public Observable<Object> listen() {
        return publisher;
    }
}
