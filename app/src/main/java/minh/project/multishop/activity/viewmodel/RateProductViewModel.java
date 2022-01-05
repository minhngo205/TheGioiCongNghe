package minh.project.multishop.activity.viewmodel;

import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.button.MaterialButton;
import minh.project.multishop.R;
import minh.project.multishop.activity.RateProductActivity;
import minh.project.multishop.adapter.RateProductAdapter;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityProductRatingBinding;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.models.Rating;
import minh.project.multishop.network.dtos.DTORequest.RateProductRequest;
import minh.project.multishop.network.repository.RatingRepository;
import minh.project.multishop.utils.DateConverter;

import java.util.ArrayList;
import java.util.List;

public class RateProductViewModel extends BaseActivityViewModel<RateProductActivity> {

    private final ActivityProductRatingBinding mBinding;
    private final RateProductAdapter adapter;
    private final User mUser;
    private final List<Rating> ratingData;

    /**
     * constructor
     *
     * @param rateProductActivity Activity object
     */
    public RateProductViewModel(RateProductActivity rateProductActivity) {
        super(rateProductActivity);
        ratingData = new ArrayList<>();
        mBinding = rateProductActivity.getBinding();
        adapter = new RateProductAdapter(rateProductActivity,rateProductActivity.getItemList());
        mUser = UserDBRepository.getInstance().getCurrentUser();
    }

    private void getRateData() {
        for(OrderItem item : mActivity.getItemList()){
            RatingRepository.getInstance().getMyRate(mUser.getAccToken(), item.getProductID()).observe(mActivity, ratings -> {
                if(null == ratings) return;
                if(ratings.isEmpty()) ratingData.add(null);

                ratingData.addAll(ratings);
                if(ratingData.size() == mActivity.getItemList().size()){
                    adapter.setRatingList(ratingData);
                }
            });
        }
    }

    @Override
    public void initView() {
        mBinding.toolbarLay.tvTitle.setText(mActivity.getString(R.string.title_rate_product));
        mBinding.btnCancel.setOnClickListener(mActivity);
        InitRateRecycleView();
    }

    private void InitRateRecycleView() {
        mBinding.rcvProductRate.setLayoutManager(new LinearLayoutManager(mActivity));
        getRateData();
        mBinding.rcvProductRate.setAdapter(adapter);
        adapter.setOnProductRateListener(mActivity);
    }

    @Override
    public void onClickEvent(int viewId) {

    }

    public void onRateProduct(int productID, String comment, int rateIndex, View rateResult, View rateComment, View rateBar) {
        if(null == mUser){
            return;
        }

        RateProductRequest request = new RateProductRequest(productID,comment,rateIndex);
        RatingRepository.getInstance().rateProduct(mUser.getAccToken(),request).observe(mActivity, rating -> {
            if(null == rating){
                Toast.makeText(mActivity, "Không thể đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            ((MaterialButton)rateResult).setText(mActivity.getString(R.string.already_rate, DateConverter.DateTimeFormat(rating.getUpdated_at())));
            rateResult.setClickable(false);

            ((EditText) rateComment).setClickable(false);
            ((EditText) rateComment).setCursorVisible(false);
            ((EditText) rateComment).setFocusable(false);
            ((EditText) rateComment).setFocusableInTouchMode(false);

            ((RatingBar) rateBar).setIsIndicator(true);
        });
    }
}
