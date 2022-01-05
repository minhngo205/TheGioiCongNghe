package minh.project.multishop.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import minh.project.multishop.activity.viewmodel.SearchViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivitySearchBinding;

public class SearchActivity extends BaseActivity {

    private ActivitySearchBinding mBinding;
    private SearchViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mViewModel = new SearchViewModel(this);
        mViewModel.initView();
        mViewModel.initHotList();
        mViewModel.initHistoryList();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewModel.addTagItem();
    }

    public ActivitySearchBinding getBinding() {
        return mBinding;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mViewModel.finishActivity();
        return super.onKeyDown(keyCode, event);
    }
}