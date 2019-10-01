package am.foursteps.pexel.ui.base.interfaces;

import android.view.View;

public interface OnRecyclerItemClickListener<T> {
    void onItemClicked(View view, T item, int position);
}
