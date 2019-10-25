package am.foursteps.pexel.ui.base.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.Status;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.SizeData;
import am.foursteps.pexel.data.local.entity.FavoritePhotoEntity;
import am.foursteps.pexel.data.local.rxevent.ProgressEvent;
import am.foursteps.pexel.data.remote.model.Image;
import am.foursteps.pexel.databinding.BottomSheetBinding;
import am.foursteps.pexel.ui.main.adapter.ChooseSizeAdapter;
import timber.log.Timber;

import static com.downloader.Status.CANCELLED;
import static com.downloader.Status.COMPLETED;
import static com.downloader.Status.FAILED;
import static com.downloader.Status.UNKNOWN;

public class BottomSheetSizeHelper {
    private ChooseSizeAdapter mSizeAdapter;

    private int photoSize = 0;
    private String mImageSrc;
    private int downloadId;

    public BottomSheetSizeHelper() {
    }

    public void ItemClick(LayoutInflater layoutInflater, Context context, int position, Object object) {


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
            if (object instanceof Image) {
                Image image = (Image) object;
                switch (photoSize) {
                    case 0:
                        mImageSrc = image.getSrc().getOriginal();
                        break;
                    case 1:
                        mImageSrc = image.getSrc().getLarge();
                        break;
                    case 2:
                        mImageSrc = image.getSrc().getMedium();
                        break;
                    case 3:
                        mImageSrc = image.getSrc().getSmall();
                }
            }
            if (object instanceof FavoritePhotoEntity) {
                FavoritePhotoEntity entity = (FavoritePhotoEntity) object;
                switch (photoSize) {
                    case 0:
                        mImageSrc = entity.getImageSrc().getOriginal();
                        break;
                    case 1:
                        mImageSrc = entity.getImageSrc().getLarge();
                        break;
                    case 2:
                        mImageSrc = entity.getImageSrc().getMedium();
                        break;
                    case 3:
                        mImageSrc = entity.getImageSrc().getSmall();
                }
            }
            Uri uri = Uri.parse(mImageSrc);


             downloadId = PRDownloader.download(uri.toString(),
                     Environment
                             .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                             .getAbsolutePath() + "/" + "Pexel" + "/",
                     new Date() + ".jpg")
                    .build()
                    .setOnStartOrResumeListener(() -> {
                        DownloadIds.getInstance().addDownloadId(position, downloadId);
                        Timber.e("stat downloading");
                    })
                    .setOnProgressListener(progress -> {
                        ProgressEvent event = new ProgressEvent();
                        event.setProgress((float) progress.currentBytes / progress.totalBytes);
                        event.setPosition(position);
                        RxBus.getInstance().publishProgress(event);
                    })
                     .setOnCancelListener(() -> {
                         for (int i = 0; i < DownloadIds.getInstance().getDownloadIds().size(); i++) {
                             int item = DownloadIds.getInstance().getDownloadIds().indexOfValue(i);
                             Status status = PRDownloader.getStatus(item);
                             if (status == COMPLETED || status == CANCELLED || status == FAILED || status == UNKNOWN) {
                                 ProgressEvent event = new ProgressEvent();
                                 event.setProgress(1f);
                                 event.setPosition(position);
                                 DownloadIds.getInstance().removeDownloadIds(position);
                                 PRDownloader.cancel(item);
                                 RxBus.getInstance().publishProgress(event);
                                 Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                             }
                         }
                     })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            for (int i = 0; i < DownloadIds.getInstance().getDownloadIds().size(); i++) {
                                int item = DownloadIds.getInstance().getDownloadIds().indexOfValue(i);
                               Status status = PRDownloader.getStatus(item);
                               if(status == COMPLETED || status== CANCELLED || status==FAILED || status==UNKNOWN){
                                   ProgressEvent event = new ProgressEvent();
                                   event.setProgress(1f);
                                   event.setPosition(position);
                                   DownloadIds.getInstance().removeDownloadIds(position);
                                   PRDownloader.cancel((int)item);
                                   RxBus.getInstance().publishProgress(event);
                                   Toast.makeText(context, "Download", Toast.LENGTH_SHORT).show();
                               }
                           }
                        }

                        @Override
                        public void onError(Error error) {
                            Timber.e(error.getServerErrorMessage());
                        }
                    });
        });
    }
}
