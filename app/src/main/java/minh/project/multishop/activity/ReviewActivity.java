package minh.project.multishop.activity;

import android.content.Intent;
import android.os.Bundle;
import minh.project.multishop.activity.viewmodel.ReviewActViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityRatingBinding;
import minh.project.multishop.models.OrderItem;

public class ReviewActivity extends BaseActivity {

    private ActivityRatingBinding mBinding;
    private OrderItem product;
    private double avgRating;
    private ReviewActViewModel mViewModel;
    private boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        Intent intent = getIntent();
        if(null != intent){
            product = intent.getParcelableExtra("PRODUCT_INFO");
            avgRating = intent.getDoubleExtra("AVG_RATING",-1);
        }
        mViewModel = new ReviewActViewModel(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isInit){
            mViewModel.initListRating();
            isInit = true;
        }
    }

    @Override
    public void recreate() {
        super.recreate();
        mViewModel.NewAvgRatingInfo();
    }

    public OrderItem getProduct() {
        return product;
    }

    public ActivityRatingBinding getBinding() {
        return mBinding;
    }

    public double getAvgRating() {
        return avgRating;
    }
}