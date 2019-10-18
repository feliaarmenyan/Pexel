package am.foursteps.pexel.ui.base.util;

import am.foursteps.pexel.data.local.rxevent.ProgressEvent;
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

    private PublishSubject<String> publisherKey = PublishSubject.create();
    private PublishSubject<ProgressEvent> publisherProgress = PublishSubject.create();

    public void publishKey(String key) {
        publisherKey.onNext(key);
    }

    public void publishProgress(ProgressEvent progressEvent) {
        publisherProgress.onNext(progressEvent);
    }

    // Listen should return an Observable
    public Observable<String> listenKey() {
        return publisherKey;
    }

    public Observable<ProgressEvent> listenProgress() {
        return publisherProgress;
    }
}
