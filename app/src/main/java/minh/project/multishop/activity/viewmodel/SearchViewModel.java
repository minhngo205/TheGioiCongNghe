package minh.project.multishop.activity.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import minh.project.multishop.R;
import minh.project.multishop.activity.SearchActivity;
import minh.project.multishop.activity.SearchResultActivity;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.database.entity.SearchHistory;
import minh.project.multishop.database.repository.ProductDBRepository;
import minh.project.multishop.database.repository.SearchHistoryRepository;
import minh.project.multishop.databinding.ActivitySearchBinding;
import minh.project.multishop.databinding.LayoutSearchContentBinding;
import minh.project.multishop.dialog.BaseDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static minh.project.multishop.dialog.BaseDialog.*;

public class SearchViewModel extends BaseActivityViewModel<SearchActivity> {
    private static final int MAX_SIZE = 10;

    private static final String TAG = SearchViewModel.class.getSimpleName();

    private final ActivitySearchBinding mBinding;
    private List<String> nameList = new ArrayList<>();
    private List<String> historyList;
    private String searchContent = "";
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * constructor
     *
     * @param searchActivity Activity object
     */
    public SearchViewModel(SearchActivity searchActivity) {
        super(searchActivity);
        mBinding = searchActivity.getBinding();
        handler.post(() -> nameList = ProductDBRepository.getInstance().getListNameRecord());
    }

    @Override
    public void initView() {
        mBinding.searchDelete.setOnClickListener(view -> initDialog());
        mBinding.viewSearch.setIvBackListener(view -> finishActivity());
        handler.post(this::initAutoCompleteSearch);
    }

    public void finishActivity() {
        this.searchContent = "";
        mActivity.finish();
    }

    private void initDialog() {
        Bundle data = new Bundle();

        data.putString(CONFIRM_BUTTON, mActivity.getString(R.string.confirm));
        data.putString(CONTENT, mActivity.getString(R.string.check_delete_history));
        data.putString(CANCEL_BUTTON, mActivity.getString(R.string.cancel));

        BaseDialog dialog = new BaseDialog(mActivity, data, true);
        dialog.setConfirmListener(v -> {
            SearchHistoryRepository.getInstance().clearHistory();
            historyList.clear();
            mBinding.viewHistory.removeAllViews();
            initHistoryList();
            dialog.dismiss();
        });
        dialog.setCancelListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void initAutoCompleteSearch() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity,android.R.layout.simple_list_item_1,nameList);
        mBinding.viewSearch.setAdapter(adapter);
        mBinding.viewSearch.setEditTextSelectedListener((adapterView, view, i, l) -> {
            String name = (String) adapterView.getItemAtPosition(i);
            startSearchResult(name);
        });
        mBinding.viewSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i == EditorInfo.IME_ACTION_SEARCH){
                String content = String.valueOf(mBinding.viewSearch.getEditText().getText());
                if("".equals(content.trim())){
                    Toast.makeText(mActivity, "Bạn cần nhập gì đó để tìm kiếm", Toast.LENGTH_SHORT).show();
                    return false;
                }
                startSearchResult(content.trim());
                return true;
            }
            return false;
        });
    }

    public void initHistoryList(){
        if(null == historyList){
            historyList = new ArrayList<>();
        }
        List<String> data = SearchHistoryRepository.getInstance().getAllSearchHistory();
        Log.i(TAG, "initHistoryList: "+data);
        if(null != data && data.size() > 0){
            historyList.clear();
            mBinding.viewHistory.removeAllViews();
            Collections.reverse(data);
            historyList.addAll(data);
        }
        if(historyList.size() <= 0){
            mBinding.viewHistory.setVisibility(View.GONE);
            mBinding.tvHistory.setVisibility(View.GONE);
            mBinding.searchDelete.setVisibility(View.GONE);
            return;
        }

        mBinding.viewHistory.setVisibility(View.VISIBLE);
        mBinding.tvHistory.setVisibility(View.VISIBLE);
        mBinding.searchDelete.setVisibility(View.VISIBLE);
        for(String search : historyList){
            mBinding.viewHistory.addView(addTagItem(search));
        }
    }

    public void initHotList(){
        String[] hots = mActivity.getResources().getStringArray(R.array.search_hot);
        if (hots.length > 1) {
            for (String item : hots) {
                mBinding.viewHot.addView(addTagItem(item));
            }
        } else {
            mBinding.viewHot.setVisibility(View.GONE);
            mBinding.tvHot.setVisibility(View.GONE);
        }
    }

    private View addTagItem(String item) {
        TextView tv = LayoutSearchContentBinding.inflate(mActivity.getLayoutInflater(), mBinding.viewHistory, false).typeText;
//                (TextView) LayoutInflater.from(mActivity).inflate(R.layout.item_search_conent, scHistory, false);
        tv.setText(item);
        tv.setOnClickListener(v -> startSearchResult(item));
        return tv;
    }



    private void startSearchResult(String content) {
        this.searchContent = content;
        addTagItem(content);
        Intent intent = new Intent(mActivity, SearchResultActivity.class);
        intent.putExtra("SEARCH_CONTENT",content);
        mActivity.startActivity(intent);
    }

    public void addTagItem(){
        if(!"".equals(searchContent.trim())){
            historyList.remove(searchContent.trim());
            historyList.add(0,searchContent);
            if(historyList.size() > MAX_SIZE){
                historyList.remove(MAX_SIZE);
                mBinding.viewHistory.removeViewAt(MAX_SIZE-1);
            }

            SearchHistoryRepository.getInstance().addSearchHistory(new SearchHistory(searchContent));
            if(mBinding.viewHistory.getVisibility()==View.GONE){
                mBinding.viewHistory.setVisibility(View.VISIBLE);
                mBinding.tvHistory.setVisibility(View.VISIBLE);
                mBinding.searchDelete.setVisibility(View.VISIBLE);
            }
            mBinding.viewHistory.removeAllViews();
            for(String search : historyList){
                mBinding.viewHistory.addView(addTagItem(search));
            }
            searchContent = "";
        }
    }

    @Override
    public void onClickEvent(int viewId) {

    }
}
