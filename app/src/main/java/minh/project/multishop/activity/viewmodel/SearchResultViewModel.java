package minh.project.multishop.activity.viewmodel;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import minh.project.multishop.R;
import minh.project.multishop.activity.SearchResultActivity;
import minh.project.multishop.adapter.SearchResultAdapter;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.databinding.ActivitySearchResultBinding;
import minh.project.multishop.models.Product;
import minh.project.multishop.network.repository.ProductNetRepository;

import java.util.List;

public class SearchResultViewModel extends BaseActivityViewModel<SearchResultActivity> {
    private static final String TAG = SearchResultViewModel.class.getSimpleName();

    private final ActivitySearchResultBinding mBinding;
    private final Handler handler = new Handler(mActivity.getMainLooper());
    private List<Product> listSearchResult;
    private final SearchResultAdapter adapter;

    /**
     * constructor
     *
     * @param searchResultActivity Activity object
     */
    public SearchResultViewModel(SearchResultActivity searchResultActivity) {
        super(searchResultActivity);
        mBinding = searchResultActivity.getBinding();
        adapter = new SearchResultAdapter(mActivity);
    }

    private void getSearchResult(String searchContent) {
        Log.i(TAG, "getSearchResult: Here");
        ProductNetRepository.getInstance().getListProductByName(searchContent).observe(mActivity, products -> {
            if(null == products){
                Toast.makeText(mActivity, "Không tải được nội dung kết quả", Toast.LENGTH_SHORT).show();
                return;
            }

            listSearchResult = products;
        });
    }

    @Override
    public void initView() {
        mBinding.searchBar.tvSearchContent.setText(mActivity.getSearchContent());
        mBinding.searchBar.ivBack.setOnClickListener(mActivity);
        mBinding.recyclerProduct.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.recyclerProduct.setAdapter(adapter);
        initSearchResult();
    }

    private void initSearchResult() {
        ProductNetRepository.getInstance().getListProductByName(mActivity.getSearchContent()).observe(mActivity, products -> {
            if(null == products){
                Toast.makeText(mActivity, "Không tải được nội dung kết quả", Toast.LENGTH_SHORT).show();
                mBinding.lvNoProduct.setVisibility(View.VISIBLE);
                return;
            }

            if(products.isEmpty()){
                mBinding.lvNoProduct.setVisibility(View.VISIBLE);
                return;
            }

            listSearchResult = products;
            adapter.setProductList(listSearchResult);
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId){
            case R.id.iv_back:
                mActivity.finish();
                break;
            case R.id.lv_search:
            default: break;
        }
    }
}
