package am.foursteps.pexel.ui.base.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.net.URL;
import java.util.Date;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.SizeData;
import am.foursteps.pexel.data.remote.model.ImageSrc;
import am.foursteps.pexel.databinding.BottomSheetBinding;
import am.foursteps.pexel.ui.main.adapter.ChooseSizeAdapter;
import am.foursteps.pexel.ui.main.adapter.PaginationAdapter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import static android.content.Context.DOWNLOAD_SERVICE;

public class BottomSheetSizeHelper {
    private ChooseSizeAdapter mSizeAdapter;

    private float progress = 0;
    private String dirPath;
    private String fileName;
    private File file;
    private int photoSize = 0;
    private URL url;
    private String mImageSrc;
    SingleObserver<Integer> mObserver;


    public BottomSheetSizeHelper() {
    }

    public void ItemClich(Activity activity, LayoutInflater layoutInflater, PaginationAdapter paginationAdapter, Context context, int position, ImageSrc src) {


        BottomSheetBinding mBinding = BottomSheetBinding.inflate(layoutInflater);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(mBinding.getRoot());

        mSizeAdapter = new ChooseSizeAdapter((v, i, position1) -> {
            if (v.getId() == R.id.item_choose_size) {
                if (i.getCheckIcon() != R.drawable.ic_check) {
                    for (int j = 0; j < mSizeAdapter.getItemCount(); j++) {
                        if (mSizeAdapter.getItem(j).getCheckIcon() == R.drawable.ic_check) {
                            mSizeAdapter.getItem(j).setCheckIcon(R.drawable.ic_check_box);
                            mSizeAdapter.update(j, mSizeAdapter.getItem(j));
                        }
                    }
                    i.setCheckIcon(R.drawable.ic_check);
                    mSizeAdapter.update(position1, i);
                    photoSize = position1;
                }
            }
        });
        mBinding.bottomSheetChooseSizeRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        mSizeAdapter.setChooseSizeList(SizeData.getInstance().getItems());
        mBinding.bottomSheetChooseSizeRecyclerView.setAdapter(mSizeAdapter);

        dialog.show();
        mBinding.bottomSheetChooseSizeClose.setOnClickListener(view1 -> dialog.dismiss());

        mBinding.bottomSheetChooseSizeDownloadButton.setOnClickListener(view12 -> {
            dialog.dismiss();

            switch (photoSize) {
                case 0:
                    mImageSrc = src.getOriginal();
                    break;
                case 1:
                    mImageSrc = src.getLarge();
                    break;
                case 2:
                    mImageSrc = src.getMedium();
                    break;
                case 3:
                    mImageSrc = src.getSmall();
            }
            Uri uri = Uri.parse(mImageSrc);
            DownloadManager.Request request = new DownloadManager.Request(uri)
                    .setDestinationInExternalPublicDir(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), new Date() + ".jpg")// Uri of the destination file
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

            DownloadManager dm = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);

            long downloadId = dm.enqueue(request);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);


            dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            new DownloadTread(downloadId, activity, dm, position).start();

//
//                new SingleObserver<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(Integer integer) {
//                        Toast.makeText(context,"eeeeeeeeeeee"+integer,Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                };



//            public SingleObserver<int[]> getSingleObserver(){
//                return new SingleObserver<Integer[]>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Toast.makeText(context, "subscibe", Toast.LENGTH_SHORT).show();
//                        Timber.e("sybjr" + d.isDisposed());
//                    }
//
//                    @Override
//                    public void onSuccess(Integer[] integers) {
//                        paginationAdapter.updateItem(position, integers[1] - integers[0]);
//                        Timber.e("ok" + integers);
//                        Toast.makeText(context, "okkkkkkkk", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(context, "errrrrr", Toast.LENGTH_SHORT).show();
//                        Timber.e("errorr" + e.getMessage());
//                    }
//                };
//            }


//            CountDownTimer mCountDownTimer;
//            mCountDownTimer = new CountDownTimer(5000, 1000) {
//
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    progress += 20;
//                    paginationAdapter.updateItem(position, progress);
//                }
//
//                @Override
//                public void onFinish() {
//                    paginationAdapter.updateItem(position, 100);
//                    progress = 0;
//                }
//            };
//            mCountDownTimer.start();

        });
    }

//    private String getfileExtension(Context context, Uri uri) {
//        String extension;
//        ContentResolver contentResolver = context.getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//        return extension;
//    }



}
