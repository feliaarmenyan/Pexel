package com.evon.pexel.view.ui;

import android.app.SharedElementCallback;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.evon.pexel.R;
import com.evon.pexel.databinding.FragmentItemBinding;

import java.util.List;
import java.util.Map;

public class ItemFragment extends Fragment {
    private Object mObject;
    private FragmentItemBinding mBinding;
    private ImageView mImageView;

    public static ItemFragment newInstance() {
        Bundle args = new Bundle();
        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(args);
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.imageView.setTransitionName("image");
        mBinding.imageView.setImageResource(R.drawable.ic_profile_5);
        mBinding.phoneNumberToolbar.setOnClickListener(view1 -> requireActivity().onBackPressed());
        setEnterSharedElementCallback(
                new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(
                            List<String> names, Map<String, View> sharedElements) {
                        // Locate the image view at the primary fragment (the ImageFragment
                        // that is currently visible). To locate the fragment, call
                        // instantiateItem with the selection position.
                        // At this stage, the method will simply return the fragment at the
                        // position and will not create a new one.
                        Fragment currentFragment = (Fragment) view.getAdapter()
                                .instantiateItem(view, MainActivity.currentPosition);
                        View view1 = currentFragment.getView();
                        if (view1 == null) {
                            return;
                        }

                        // Map the first shared element name to the child ImageView.
                        sharedElements.put(names.get(0), view1.findViewById(R.id.imageView));
                    }
                });
    }
}
