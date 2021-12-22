package minh.project.multishop.activity.viewmodel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import minh.project.multishop.R;
import minh.project.multishop.activity.CartActivity;
import minh.project.multishop.activity.MainActivity;
import minh.project.multishop.activity.SearchActivity;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.databinding.ActivityMainBinding;
import minh.project.multishop.fragment.CategoryFragment;
import minh.project.multishop.fragment.HomeFragment;
import minh.project.multishop.fragment.NewInFragment;
import minh.project.multishop.fragment.UserFragment;


public class MainActivityViewModel extends BaseActivityViewModel<MainActivity> {

    private final List<Fragment> fragmentList = new ArrayList<>();
    // Home
    private HomeFragment homeFragment;
    // catalogue
    private CategoryFragment categoryFragment;
    // shopCar
    private NewInFragment newInFragment;

    private UserFragment userFragment;

    private ActivityMainBinding mainBinding;

    private final NavigationBarView.OnItemSelectedListener botNavListener = new NavigationBarView.OnItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                        case R.id.home:{
                            if (homeFragment == null) {
                                homeFragment = new HomeFragment();
                            }
                            addFragment(homeFragment);
                            showFragment(homeFragment);
                            mainBinding.barHomeLayout.setVisibility(View.VISIBLE);
                            break;
                        }
                        case R.id.category:{
                            if (categoryFragment == null) {
                                categoryFragment = new CategoryFragment();
                            }
                            addFragment(categoryFragment);
                            showFragment(categoryFragment);
                            mainBinding.barHomeLayout.setVisibility(View.VISIBLE);
                            break;
                        }
                        case R.id.new_in:{
                            if(newInFragment == null){
                                newInFragment = new NewInFragment();
                            }
                            addFragment(newInFragment);
                            showFragment(newInFragment);
                            mainBinding.barHomeLayout.setVisibility(View.VISIBLE);
                            break;
                        }
                        case R.id.user:{
                            if(userFragment==null){
                                userFragment = new UserFragment();
                            }
                            addFragment(userFragment);
                            showFragment(userFragment);
                            mainBinding.barHomeLayout.setVisibility(View.GONE);
                            break;
                        }
                        default: break;
                    }
            return true;
        }
    };

    /**
     * constructor
     *
     * @param mainActivity Activity object
     */
    public MainActivityViewModel(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void initView() {
        mainBinding = mActivity.getMainBinding();

        mainBinding.barHome.ivTakePhoto.setOnClickListener(mActivity);
        mainBinding.barHome.imageSearch.setOnClickListener(mActivity);
        mainBinding.barHome.barSearch.setOnClickListener(mActivity);
        mainBinding.barHome.ivInterestKits.setOnClickListener(mActivity);
        mainBinding.barHome.ivCart.setOnClickListener(mActivity);

        mainBinding.bottomNavigationView.setOnItemSelectedListener(botNavListener);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId) {
            case R.id.bar_search: // search
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));
                break;
            case R.id.iv_interest_kits: // interest_kits

                break;
            case R.id.iv_take_photo: // take photo

                break;
            case R.id.iv_cart:{
                Intent intent = new Intent(mActivity, CartActivity.class);
                mActivity.startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    public void removeAllFragment() {
        for (Fragment fragment : fragmentList) {
            mActivity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void showFragment(Fragment fragment) {
        for (Fragment frag : fragmentList) {
            if (frag != fragment) {
                mActivity.getSupportFragmentManager().beginTransaction().hide(frag).commit();
            }
        }
        mActivity.getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    private void addFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            mActivity.getSupportFragmentManager().beginTransaction().add(R.id.frame_main, fragment).commit();
            fragmentList.add(fragment);
        }
    }

    public void initFragment() {
        Intent intent = mActivity.getIntent();
        if (null != intent) {
            String tab = intent.getStringExtra("tab");
            if ("newIn".equals(tab)) {
                return;
            }
        }
        homeFragment = new HomeFragment();
        addFragment(homeFragment);
        showFragment(homeFragment);
    }
}
