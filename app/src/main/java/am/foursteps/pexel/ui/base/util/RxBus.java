package am.foursteps.pexel.ui.base.util;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {
    private static final RxBus ourInstance = new RxBus();

   public static RxBus getInstance() {
        return ourInstance;
    }

    private RxBus() {
    }
    private PublishSubject<Integer> publisher = PublishSubject.create();

    void publish(Integer event) {
        publisher.onNext(event);
    }

    // Listen should return an Observable
    public Observable<Integer> listen() {
        return publisher;
    }
}
