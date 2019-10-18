package am.foursteps.pexel.ui.main.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import am.foursteps.pexel.ui.base.interfaces.OnRecyclerItemClickListener;

public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> items;
    protected OnRecyclerItemClickListener<T> mOnRecyclerItemClickListener;


    public abstract RecyclerView.ViewHolder setViewHolder(ViewGroup parent);

    public abstract void onBindData(RecyclerView.ViewHolder holder, T val, int position);

    public RecyclerBaseAdapter(OnRecyclerItemClickListener<T> onRecyclerItemClickListener) {
        this.mOnRecyclerItemClickListener = onRecyclerItemClickListener;
        this.items = new ArrayList<>();
    }

    protected void addItems(List<T> items) {
        int startIndex = this.items.size();
        this.items.addAll(items);
        this.notifyItemRangeChanged(startIndex, items.size());
    }

    public void updateItem(int position, T obj) {
        this.items.set(position, obj);
        this.notifyItemChanged(position, obj);
    }

    public void removeItem(int position) {
        items.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, items.size());
    }

    public void clearItems() {
        this.items.clear();
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = setViewHolder(parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        onBindData(holder, items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }
}