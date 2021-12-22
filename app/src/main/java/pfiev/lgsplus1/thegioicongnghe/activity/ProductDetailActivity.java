package pfiev.lgsplus1.thegioicongnghe.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pfiev.lgsplus1.thegioicongnghe.activity.viewmodel.ProductDetailViewModel;
import pfiev.lgsplus1.thegioicongnghe.base.BaseActivity;
import pfiev.lgsplus1.thegioicongnghe.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity extends BaseActivity {

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