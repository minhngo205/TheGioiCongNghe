package minh.project.multishop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import minh.project.multishop.base.BaseFragment;
import minh.project.multishop.databinding.FragmentHomeBinding;
import minh.project.multishop.fragment.fragmentviewmodel.HomeFragmentViewModel;

public class HomeFragment extends BaseFragment {

    private static final String TAG = "HomeFragment";
    private FragmentHomeBinding homeBinding;
    private final HomeFragmentViewModel homeViewModel;

    public HomeFragment() {
        // Required empty public constructor
        homeViewModel = new HomeFragmentViewModel(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false);
        View viewRoot = homeBinding.getRoot();
        homeViewModel.initView(viewRoot);
        homeViewModel.initViewPager();
        return viewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.initHomeProductRecyclerView();
        homeViewModel.StartAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        homeViewModel.StopAnimation();
    }

    public FragmentHomeBinding getHomeBinding() {
        return homeBinding;
    }
}