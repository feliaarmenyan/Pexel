package am.foursteps.pexel.ui.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.local.SizeData;
import am.foursteps.pexel.databinding.ItemChooseBinding;
import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;

public class ChooseSizeAdapter extends RecyclerView.Adapter<ChooseSizeAdapter.ChooseSizeViewHolder> {

    private List<SizeData.Sizes> items;
    private OnRecyclerItemClickListener<SizeData.Sizes> onRecyclerItemClickListener;

    public ChooseSizeAdapter(OnRecyclerItemClickListener<SizeData.Sizes> onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        items = new ArrayList<>();
    }

    public SizeData.Sizes getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public ChooseSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChooseBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_choose, parent, false);
        return new ChooseSizeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseSizeViewHolder holder, int position) {
        holder.bind(items.get(position));
        holder.mBinding.itemChooseSize.setOnClickListener(view -> {
            onRecyclerItemClickListener.onItemClicked(holder.mBinding.itemChooseSize, items.get(position), position);
        });
    }

    public void setChooseSizeList(List<SizeData.Sizes> mChooseSizeList) {
        this.items = mChooseSizeList;
        this.notifyDataSetChanged();
    }

    public void update(int position, SizeData.Sizes chooseSizeItem) {
        this.items.set(position, chooseSizeItem);
        this.notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ChooseSizeViewHolder extends RecyclerView.ViewHolder {

        private ItemChooseBinding mBinding;

        ChooseSizeViewHolder(@NonNull ItemChooseBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(SizeData.Sizes chooseSizeItem) {
            mBinding.itemChooseSizeImageView.setImageResource(chooseSizeItem.getCheckIcon());
            mBinding.itemChooseSizeTextView.setText(chooseSizeItem.getSize());
        }
    }
}
