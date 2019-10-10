package am.foursteps.pexel.ui.base.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.database.Cursor;

import am.foursteps.pexel.ui.main.adapter.PaginationAdapter;


public class DownloadTread extends Thread {

    private final long downloadId;
    private final DownloadManager.Query query;
    private Cursor cursor;
    private int lastBytesDownloadedSoFar;
    private int totalBytes;
    private Activity activity;
    private DownloadManager manager;
    private BottomSheetSizeHelper mBottomSheetSizeHelper = new BottomSheetSizeHelper();
    private PaginationAdapter mPaginationAdapter;

    public DownloadTread(long downloadId, Activity activity, DownloadManager manager) {
        this.downloadId = downloadId;
        this.query = new DownloadManager.Query();
        this.activity = activity;
        this.manager = manager;
        query.setFilterById(this.downloadId);
    }

    @Override

    public void run() {
        while (downloadId > 0) {
            try {
                Thread.sleep(300);
                cursor = manager.query(query);
                if (cursor.moveToFirst()) {
                    //get total bytes of the file
                    if (totalBytes <= 0) {
                        totalBytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        lastBytesDownloadedSoFar=totalBytes;
                    }
                    final int bytesDownloadedSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    if (bytesDownloadedSoFar == totalBytes && totalBytes > 0) {
                        this.interrupt();
                    } else {
                        //update progress bar
                        activity.runOnUiThread(() -> {
                            RxBus.getInstance().publish((bytesDownloadedSoFar / lastBytesDownloadedSoFar)*100);
//                            Observable.create(new ObservableOnSubscribe() {
//                                @Override
//                                public void subscribe(ObservableEmitter emitter) throws Exception {
//                                    emitter.onNext((bytesDownloadedSoFar / lastBytesDownloadedSoFar)*100);
//                                    if ((int) (bytesDownloadedSoFar / lastBytesDownloadedSoFar) == 1) {
//                                        emitter.onComplete();
//                                    }
//                                }
//                            });
                        });
                    }
                }
                cursor.close();
            } catch (Exception e) {
                return;
            }
        }
    }
//                             new SingleObserver<Integer>() {
//                                    @Override
//                                    public void onSubscribe(Disposable d) {
//                                       Timber.e(" onSubscribe : %s", d.isDisposed());
//                                    }
//
//                                    @Override
//                                    public void onSuccess(Integer value) {
//                                        value=bytesDownloadedSoFar;
//                                        Timber.e( " onNext value : %s", value);
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        Timber.e( " onError : %s", e.getMessage());
//                                    }
//                                };

//                            int[] a = new int[2];
//                            a[0]=lastBytesDownloadedSoFar;
//                            a[1]=bytesDownloadedSoFar;
//                            Single.just(a).subscribe(mBottomSheetSizeHelper.getSingleObserver);

//                            mPaginationAdapter.updateItem(position, (bytesDownloadedSoFar - lastBytesDownloadedSoFar));
////                                        .setProgress(mProgressBar.getProgress() + (bytesDownloadedSoFar - lastBytesDownloadedSoFar));
//                            lastBytesDownloadedSoFar = bytesDownloadedSoFar;

//    public SingleObserver<Integer> fullbites(int positiona){
//
//        new SingleObserver<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Timber.e(" onSubscribe : %s", d.isDisposed());
//            }
//
//            @Override
//            public void onSuccess(Integer value) {
//                value=positiona;
//                Timber.e( " onNext value : %s", value);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Timber.e( " onError : %s", e.getMessage());
//            }
//        };
//    }
}