package minh.project.multishop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import minh.project.multishop.base.BaseFragment;
import minh.project.multishop.databinding.FragmentUserBinding;
import minh.project.multishop.fragment.fragmentviewmodel.UserFragmentViewModel;

public class UserFragment extends BaseFragment implements View.OnClickListener {

    private final UserFragmentViewModel mViewModel;
    private FragmentUserBinding mBinding;


    public UserFragment() {
        mViewModel = new UserFragmentViewModel(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUserBinding.inflate(inflater,container,false);
        View viewRoot = mBinding.getRoot();
        mViewModel.initView(viewRoot);
        return viewRoot;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mViewModel.checkSignIn();
    }

    public FragmentUserBinding getBinding() {
        return mBinding;
    }

    @Override
    public void onClick(View view) {
        mViewModel.onClickEvent(view.getId());
    }
}