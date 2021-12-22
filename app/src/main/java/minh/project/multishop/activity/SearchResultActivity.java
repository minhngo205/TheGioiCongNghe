package minh.project.multishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import minh.project.multishop.activity.viewmodel.SearchResultViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivitySearchResultBinding;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {

    private ActivitySearchResultBinding mBinding;
    private String searchContent;
    private SearchResultViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = getIntent();
        if(null != intent){
            searchContent = intent.getStringExtra("SEARCH_CONTENT");
        }
        mViewModel = new SearchResultViewModel(this);
        mViewModel.initView();
    }

    public ActivitySearchResultBinding getBinding() {
        return mBinding;
    }

    public String getSearchContent() {
        return searchContent;
    }

    @Override
    public void onClick(View view) {
        mViewModel.onClickEvent(view.getId());
    }
}