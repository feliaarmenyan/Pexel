package am.foursteps.pexel.ui.base.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.SizeData;
import am.foursteps.pexel.databinding.BottomSheetBinding;
import am.foursteps.pexel.ui.main.adapter.ChooseSizeAdapter;
import am.foursteps.pexel.ui.main.adapter.PaginationAdapter;

public class BottomSheetSizeHelper {
    private ChooseSizeAdapter mSizeAdapter;

    private float progress = 0;

    public BottomSheetSizeHelper() {
    }

    public void ItemClich(LayoutInflater layoutInflater,PaginationAdapter paginationAdapter, Context context, int position) {

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


            CountDownTimer mCountDownTimer;
            mCountDownTimer = new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    progress += 20;
                    paginationAdapter.updateItem(position, progress);
                }

                @Override
                public void onFinish() {
                    paginationAdapter.updateItem(position, 100);
                    progress = 0;
                }
            };
            mCountDownTimer.start();
        });
    }
}