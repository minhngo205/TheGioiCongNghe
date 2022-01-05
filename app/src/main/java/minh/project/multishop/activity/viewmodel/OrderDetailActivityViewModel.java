package minh.project.multishop.activity.viewmodel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import minh.project.multishop.R;
import minh.project.multishop.activity.OrderDetailActivity;
import minh.project.multishop.activity.RateProductActivity;
import minh.project.multishop.adapter.OrderItemAdapter;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityOrderdescriptionBinding;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.network.dtos.DTOResponse.OrderDetailResponse;
import minh.project.multishop.network.repository.OrderRepository;
import minh.project.multishop.utils.CurrencyFormat;
import minh.project.multishop.utils.DateConverter;
import minh.project.multishop.utils.Statistics;

import java.util.ArrayList;

import static minh.project.multishop.utils.Statistics.SUCCESS_ORDER;

public class OrderDetailActivityViewModel extends BaseActivityViewModel<OrderDetailActivity> {

    private final ActivityOrderdescriptionBinding mBinding;
    private RecyclerView rvOrderItems;
    private final OrderItemAdapter mAdapter;
    private OrderDetailResponse orderDetail;
    private final User mUser;
    /**
     * constructor
     *
     * @param orderDetailActivity Activity object
     */
    public OrderDetailActivityViewModel(OrderDetailActivity orderDetailActivity) {
        super(orderDetailActivity);
        mBinding = orderDetailActivity.getBinding();
        mAdapter = new OrderItemAdapter(mActivity);
        mUser = UserDBRepository.getInstance().getCurrentUser();
    }

    @Override
    public void initView() {
        mBinding.toolbars.tvTitle.setText("Chi tiết đơn hàng");
        mBinding.toolbars.ivBack.setOnClickListener(mActivity);
        mBinding.btnRateProduct.setOnClickListener(mActivity);
        rvOrderItems = mBinding.rvOrderItems;
        initRecycleView();
        initOrderDetail();
        initAddressDetail();
        initPaymentDetail();
    }

    private void initPaymentDetail() {
        mBinding.layoutPaymentDetail.setProductPrice(CurrencyFormat.currencyFormat(orderDetail.sumPrice));
        mBinding.layoutPaymentDetail.setShippingPrice(CurrencyFormat.currencyFormat(orderDetail.shipFee));
        mBinding.layoutPaymentDetail.setTotalPrice(CurrencyFormat.currencyFormat(orderDetail.totalCost));
    }

    private void initAddressDetail() {
        UserInfo userInfo = new UserInfo(orderDetail.Name, orderDetail.userAddress, orderDetail.userPhone);
        mBinding.layoutAddress.setUserinfo(userInfo);
    }

    private void initOrderDetail() {
        mBinding.tvOrderId.setText(String.valueOf(orderDetail.orderID));
        mBinding.tvOrderDate.setText(DateConverter.DateTimeFormat(orderDetail.orderDay));
        mBinding.tvPayment.setText(orderDetail.payment.name);
        mBinding.tvStatus.setText(Statistics.getStatusText(orderDetail.orderStatus));
        if(SUCCESS_ORDER.equals(orderDetail.orderStatus)) mBinding.layoutBtnRate.setVisibility(View.VISIBLE);
    }

    private void initRecycleView() {
        rvOrderItems.setLayoutManager(new LinearLayoutManager(mActivity));
        rvOrderItems.setAdapter(mAdapter);
        mAdapter.setOrderItemList(orderDetail.castToModelList());
//        Log.d("TAG", "initRecycleView: "+orderDetail.orderItemResponses.size());
    }

    public void loadDataFromRepos() {
        if (null == mUser){
            Toast.makeText(mActivity, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        OrderRepository.getInstance()
                .getOrderDetailByID(mUser.getAccToken(), mActivity.getOrderID())
                .observe(mActivity, orderDetailResponse -> {
                    if(null == orderDetailResponse){
                        Toast.makeText(mActivity, "Không thể lấy thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    updateView(orderDetailResponse);
                });
    }

    private void updateView(OrderDetailResponse orderDetailResponse) {
        orderDetail = orderDetailResponse;
        initView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId){
            case R.id.iv_back:
                mActivity.finish();
                break;
            case R.id.btnRateProduct:
                ArrayList<OrderItem> listData = orderDetail.castToModelList();
                Intent rateIntent = new Intent(mActivity, RateProductActivity.class);
                rateIntent.putParcelableArrayListExtra("LIST_ITEM_DATA", listData);
                mActivity.startActivity(rateIntent);
                break;
            default: break;
        }
    }
}
