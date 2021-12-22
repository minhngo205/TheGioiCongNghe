package minh.project.multishop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import minh.project.multishop.activity.viewmodel.ProductDetailViewModel;
import minh.project.multishop.base.BaseActivity;
import minh.project.multishop.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ProductDetailActivity";

    private ActivityProductDetailBinding productDetailBinding;
    private ProductDetailViewModel viewModel;
    private int productID;

    public int getProductID() {
        return productID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDetailBinding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        View viewRoot = productDetailBinding.getRoot();
        setContentView(viewRoot);

        Intent intent = getIntent();
        if(intent!=null){
            productID = intent.getIntExtra("productID",-1);
        }

        productDetailBinding.back.setOnClickListener(view -> finish());
        viewModel = new ProductDetailViewModel(this);
        viewModel.initProductView();
    }

    public ActivityProductDetailBinding getProductDetailBinding() {
        return productDetailBinding;
    }

    @Override
    public void onClick(View view) {
        viewModel.onClickEvent(view.getId());
    }
}