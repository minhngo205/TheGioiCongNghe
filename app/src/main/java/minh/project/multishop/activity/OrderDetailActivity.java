package minh.project.multishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import minh.project.multishop.activity.viewmodel.OrderDetailActivityViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityOrderdescriptionBinding;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private ActivityOrderdescriptionBinding mBinding;
    private int orderID;
    private OrderDetailActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOrderdescriptionBinding.inflate(getLayoutInflater());
        View viewRoot = mBinding.getRoot();
        setContentView(viewRoot);

        Intent intent = getIntent();
        if(null!=intent){
            orderID = intent.getIntExtra("ORDER_ID",-1);
        }
        mViewModel = new OrderDetailActivityViewModel(this);
        mViewModel.loadDataFromRepos();
    }

    public ActivityOrderdescriptionBinding getBinding() {
        return mBinding;
    }

    @Override
    public void onClick(View view) {
        mViewModel.onClickEvent(view.getId());
    }

    public int getOrderID() {
        return orderID;
    }
}