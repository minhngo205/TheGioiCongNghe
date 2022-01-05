package minh.project.multishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import minh.project.multishop.activity.viewmodel.RateProductViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityProductRatingBinding;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.utils.OnProductRateListener;

import java.util.List;

public class RateProductActivity extends BaseActivity implements View.OnClickListener, OnProductRateListener {

    private ActivityProductRatingBinding mBinding;
    private List<OrderItem> itemList;
    private RateProductViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityProductRatingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = getIntent();
        if(null != intent){
            itemList = intent.getParcelableArrayListExtra("LIST_ITEM_DATA");
            Log.d("TAG", "onCreate: "+itemList.size());
        }

        mViewModel = new RateProductViewModel(this);
        mViewModel.initView();
    }

    public ActivityProductRatingBinding getBinding() {
        return mBinding;
    }

    public List<OrderItem> getItemList() {
        return itemList;
    }

    @Override
    public void onClick(View view) {
        mViewModel.onClickEvent(view.getId());
    }


    @Override
    public void onRateProduct(int productID, String comment, int rateIndex, View rateResult, View rateComment, View rateBar) {
        mViewModel.onRateProduct(productID,comment,rateIndex,rateResult,rateComment,rateBar);
    }
}
