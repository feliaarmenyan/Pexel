package am.foursteps.pexel.ui.main.fragment;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import am.foursteps.pexel.R;
import am.foursteps.pexel.data.remote.model.ImageSrc;
import am.foursteps.pexel.databinding.FragmentItemOfListBinding;
import am.foursteps.pexel.ui.base.util.ImageUrlHelper;
import am.foursteps.pexel.ui.main.activity.MainActivity;

public class ItemFragment extends Fragment {
    private FragmentItemOfListBinding mBinding;
    private ImageSrc src;

    public static ItemFragment newInstance(ImageSrc src) {
        Bundle args = new Bundle();
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
        fragment.src = src;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_of_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.imageView.setTransitionName("image");
        ImageUrlHelper.ImageUrl(mBinding.imageView,src);
        mBinding.phoneNumberToolbar.setOnClickListener(view1 -> requireActivity().onBackPressed());
        ((MainActivity) requireActivity()).updateStatusBarColor("#000000");

    }
}
