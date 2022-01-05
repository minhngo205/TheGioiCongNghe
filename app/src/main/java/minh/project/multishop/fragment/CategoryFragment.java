package minh.project.multishop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import minh.project.multishop.base.BaseFragment;
import minh.project.multishop.databinding.FragmentCategoryBinding;
import minh.project.multishop.fragment.fragmentviewmodel.CategoryFragmentViewModel;

public class CategoryFragment extends BaseFragment {

    private FragmentCategoryBinding mBinding;
    private final CategoryFragmentViewModel mViewModel;

    public CategoryFragment() {
        mViewModel = new CategoryFragmentViewModel(this);
        mViewModel.setFirstPosition(0);
    }

    public CategoryFragment(int firstPosition){
        mViewModel = new CategoryFragmentViewModel(this);
        mViewModel.setFirstPosition(firstPosition);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentCategoryBinding.inflate(inflater,container,false);
        View viewRoot = mBinding.getRoot();
        mViewModel.initView(viewRoot);
        return viewRoot;
    }

    public FragmentCategoryBinding getBinding() {
        return mBinding;
    }
}