package minh.project.multishop.activity.viewmodel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import minh.project.multishop.R;
import minh.project.multishop.activity.*;
import minh.project.multishop.adapter.ProductViewPagerAdapter;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityProductDetailBinding;
import minh.project.multishop.databinding.DialogCartBuyLayoutBinding;
import minh.project.multishop.dialog.CustomProgress;
import minh.project.multishop.models.Image;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.models.Product;
import minh.project.multishop.models.ProductSpecs;
import minh.project.multishop.network.dtos.DTORequest.EditCartRequest;
import minh.project.multishop.network.repository.CartRepository;
import minh.project.multishop.network.repository.ProductNetRepository;

import java.util.ArrayList;

import static minh.project.multishop.utils.CurrencyFormat.currencyFormat;

public class ProductDetailViewModel extends BaseActivityViewModel<ProductDetailActivity> {

    private static final String TAG = "ProductDetailViewModel";
    private static final int CACHE_PAGE_COUNT = 3;
    private ActivityProductDetailBinding productDetailBinding;
    private Product productDetail;
    private int productCount = 1;

    private TextView delButton;
    private TextView textViewCount;
    private TextView addButton;
    
    private final User mUser;
    private final CartRepository cartRepository;
    
    private final ProductNetRepository productNetRepository;

    private final CustomProgress progressDialog;

    /**
     * constructor
     *
     * @param productDetailActivity Activity object
     */
    public ProductDetailViewModel(ProductDetailActivity productDetailActivity) {
        super(productDetailActivity);
        UserDBRepository dbRepository = UserDBRepository.getInstance();
        mUser = dbRepository.getCurrentUser();
        cartRepository = CartRepository.getInstance();
        productNetRepository = ProductNetRepository.getInstance();
        progressDialog = CustomProgress.getInstance();
    }

    @Override
    public void initView() {
        productDetailBinding = mActivity.getProductDetailBinding();

        productDetailBinding.productActionBar.addBag.setOnClickListener(mActivity);
        productDetailBinding.productActionBar.buyNow.setOnClickListener(mActivity);
        productDetailBinding.productActionBar.layoutBag.setOnClickListener(mActivity);
        productDetailBinding.productActionBar.layoutEvaluate.setOnClickListener(mActivity);

        initViewPager(productDetail.getImageList());
        if(productDetail.isDiscount!=0){
            productDetailBinding.textDisplayPrice.setText(currencyFormat(productDetail.getProductPrice()));
            productDetailBinding.textDisplayPrice.setPaintFlags(productDetailBinding.textDisplayPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        productDetailBinding.textPrice.setText(currencyFormat(productDetail.getSalePrice()));
        productDetailBinding.textCategory.setText(productDetail.getCategory().getName());
        productDetailBinding.textName.setText(productDetail.getProductName());
        productDetailBinding.textBrand.setText(productDetail.getBrand().getBrandName());
        productDetailBinding.textDescription.setText(Html.fromHtml(productDetail.getShortDes()));
        productDetailBinding.informationLayout.setOnClickListener(mActivity);
        initSpecsTable();
    }

    private void initSpecsTable() {
        for(ProductSpecs specs:productDetail.getSpecs()){
            @SuppressLint("InflateParams")
            View tableRow = LayoutInflater.from(mActivity).inflate(R.layout.table_specs_item,null,false);
            TextView tv_name = tableRow.findViewById(R.id.tv_name);
            TextView tv_value = tableRow.findViewById(R.id.tv_value);

            if ("".equals(specs.getValue())) {
                tv_name.setTextColor(ContextCompat.getColor(mActivity,R.color.red_type_2));
            }

            tv_name.setText(specs.getName());
            tv_value.setText(Html.fromHtml(specs.getValue()));

            productDetailBinding.tableSpecs.addView(tableRow);
        }
    }

    public void initProductView() {
        progressDialog.showProgress(mActivity,"Đang tải...",false);
        productNetRepository.getProductByID(mActivity.getProductID()).observe(mActivity, this::updateView);
    }

    private void updateView(Product product) {
        if(product==null){
            Toast.makeText(mActivity, "No product founded", Toast.LENGTH_SHORT).show();
            mActivity.finish();
            return;
        }
        productDetail = product;
        initView();
        progressDialog.hideProgress();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId){
            case R.id.add_bag:{
                productCount = 1;
                showBottomSheetDialog("Thêm vào giỏ hàng");
                break;
            }
            case R.id.buy_now:{
                productCount = 1;
                showBottomSheetDialog("Mua ngay");
                break;
            }
            case R.id.layout_bag:{
                Intent cartIntent = new Intent(mActivity, CartActivity.class);
                mActivity.startActivity(cartIntent);
                break;
            }
            case R.id.layout_evaluate:{
                Intent reviewIntent = new Intent(mActivity, ReviewActivity.class);
                OrderItem product = new OrderItem(
                        productDetail.productID,
                        productDetail.imgThumbnail,
                        productDetail.productName,
                        productDetail.salePrice,
                        -1
                );
                reviewIntent.putExtra("PRODUCT_INFO",product);
                reviewIntent.putExtra("AVG_RATING",productDetail.getAvgRate());
                mActivity.startActivity(reviewIntent);
                break;
            }
            case R.id.btn_add:{
                ++productCount;
                textViewCount.setText(String.valueOf(productCount));
                delButton.setTextColor(ContextCompat.getColor(mActivity,R.color.red));
                delButton.setBackgroundResource(R.drawable.circle_add_delete_type_red);
                break;
            }
            case R.id.btn_delete:{
                if (productCount > 1) {
                    --productCount;
                    textViewCount.setText(String.valueOf(productCount));
                    if (productCount == 1) {
                        delButton.setTextColor(ContextCompat.getColor(mActivity,R.color.black));
                        delButton.setBackgroundResource(R.drawable.circle_add_delete_type);
                    }
                }
                break;
            }
            case R.id.information_layout:{
                if(productDetail.description.length()<=0){
                    Toast.makeText(mActivity, "Chưa có thông tin chi tiết về sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent desIntent = new Intent(mActivity, ProductDescriptionActivity.class);
                desIntent.putExtra("DESCRIPTION",productDetail.description);
                mActivity.startActivity(desIntent);

                break;
            }
            default: break;
        }
    }

    private void showBottomSheetDialog(String action) {
        final DialogCartBuyLayoutBinding binding = DialogCartBuyLayoutBinding.inflate(mActivity.getLayoutInflater());
        View viewRoot = binding.getRoot();

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity);
        bottomSheetDialog.setContentView(viewRoot);

        Glide.with(mActivity)
                .load(productDetail.getImageThumbnail())
                .placeholder(R.drawable.progress_bar_loading)
                .into(binding.thumbnailImg);

        addButton = binding.btnAdd;
        delButton = binding.btnDelete;
        textViewCount = binding.textCount;

        binding.actionName.setText(action);
        binding.productAction.setText(action);
        binding.productName.setText(productDetail.getProductName());

        if(productDetail.isDiscount!=0){
            binding.productPrice.setText(currencyFormat(productDetail.getProductPrice()));
            binding.productPrice.setPaintFlags(productDetailBinding.textDisplayPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        binding.productSalePrice.setText(currencyFormat(productDetail.getSalePrice()));
        addButton.setTextColor(ContextCompat.getColor(mActivity,R.color.red));
        addButton.setBackgroundResource(R.drawable.circle_add_delete_type_red);

        addButton.setOnClickListener(mActivity);
        delButton.setOnClickListener(mActivity);

        binding.productAction.setOnClickListener(view -> {
            switch (action){
                case "Thêm vào giỏ hàng":{
                    OnAddToCart();
                    bottomSheetDialog.dismiss();
                    break;
                }
                case "Mua ngay":{
                    OnBuyNow();
                    bottomSheetDialog.dismiss();
                    break;
                }
                default: break;
            }
        });

        bottomSheetDialog.show();
    }

    private void OnBuyNow() {
        if(mUser == null){
            Toast.makeText(mActivity, "Bạn chưa đăng nhập tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<OrderItem> orderItemData = generateOrder();
        int totalPrice = productDetail.getSalePrice()*productCount;
        Intent intent = new Intent(mActivity, OrderSubmitActivity.class);
        intent.putParcelableArrayListExtra("ORDER_DATA",orderItemData);
        intent.putExtra("TOTAL_PRICE",totalPrice);
        mActivity.startActivity(intent);
    }

    private ArrayList<OrderItem> generateOrder() {
        ArrayList<OrderItem> result = new ArrayList<>();
        result.add(new OrderItem(
                productDetail.getID(),
                productDetail.getImageThumbnail(),
                productDetail.getProductName(),
                productDetail.getSalePrice(),
                productCount));
        return result;
    }

    private void OnAddToCart() {
        if(mUser == null){
            Toast.makeText(mActivity, "Bạn chưa đăng nhập tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }
        EditCartRequest editCartRequest = new EditCartRequest(productDetail.productID,productCount);
        cartRepository.getAddCartData(mUser.getAccToken(), editCartRequest).observe(mActivity, addCartResponse -> {
            if(addCartResponse==null){
                Toast.makeText(mActivity, "Đã xảy ra lỗi, không thể thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(mActivity, "Số lượng sản phẩm: "+addCartResponse.getProductID()+" trong giỏ hàng là: "+addCartResponse.getCount(), Toast.LENGTH_SHORT).show();
        });
    }

    public void initViewPager(Image[] linkIMG) {
        ViewPager viewPager = mActivity.findViewById(R.id.view_pager_product);
        ProductViewPagerAdapter adapter = new ProductViewPagerAdapter(linkIMG, mActivity);

        TextView tvTip = mActivity.findViewById(R.id.tv_count);
        int total = adapter.getCount();
        tvTip.setText(mActivity.getString(R.string.current_position, 1, total));

        viewPager.setOffscreenPageLimit(CACHE_PAGE_COUNT);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                tvTip.setText(mActivity.getString(R.string.current_position, position + 1, total));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(adapter);
    }
}
