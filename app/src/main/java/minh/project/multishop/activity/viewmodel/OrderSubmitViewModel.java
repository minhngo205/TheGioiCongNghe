package minh.project.multishop.activity.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import minh.project.multishop.R;
import minh.project.multishop.activity.AddAddressActivity;
import minh.project.multishop.activity.OrderDetailActivity;
import minh.project.multishop.activity.OrderSubmitActivity;
import minh.project.multishop.adapter.OrderItemAdapter;
import minh.project.multishop.base.BaseActivityViewModel;
import minh.project.multishop.database.entity.User;
import minh.project.multishop.database.entity.UserInfo;
import minh.project.multishop.database.repository.UserDBRepository;
import minh.project.multishop.databinding.ActivityOrderSubmitBinding;
import minh.project.multishop.dialog.BaseDialog;
import minh.project.multishop.models.OrderItem;
import minh.project.multishop.network.dtos.DTORequest.CreateOrderRequest;
import minh.project.multishop.network.dtos.DTOmodels.DTOOrderItemRequest;
import minh.project.multishop.network.repository.CartRepository;
import minh.project.multishop.network.repository.OrderRepository;
import minh.project.multishop.utils.CurrencyFormat;

import java.util.ArrayList;
import java.util.List;

import static minh.project.multishop.dialog.BaseDialog.*;

public class OrderSubmitViewModel extends BaseActivityViewModel<OrderSubmitActivity> {

    private final ActivityOrderSubmitBinding mBinding;
    private RecyclerView rvOrderItems;
    private final OrderItemAdapter adapter;
    private UserInfo userInfo;
    private final User mUser;
    private final OrderRepository orderRepository;
    private ArrayList<OrderItem> orderItemList;

    private static final int SHIPPING_FEE = 30000;

    /**
     * constructor
     *
     * @param orderSubmitActivity Activity object
     */
    public OrderSubmitViewModel(OrderSubmitActivity orderSubmitActivity) {
        super(orderSubmitActivity);
        mBinding = mActivity.getBinding();
        orderItemList = new ArrayList<>();
        adapter = new OrderItemAdapter(mActivity);
        UserDBRepository userDBRepository = UserDBRepository.getInstance();
        userInfo = userDBRepository.getUserInfo();
        mUser = userDBRepository.getCurrentUser();
        orderRepository = OrderRepository.getInstance();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        rvOrderItems = mBinding.rvItems;
        mBinding.btnChangeAddress.setOnClickListener(mActivity);
        mBinding.toolbarLay.ivBack.setOnClickListener(mActivity);
        mBinding.tvApply.setOnClickListener(mActivity);
        mBinding.toolbarLay.tvTitle.setText("Đặt hàng");
        initUserInfo();
        initRecycleView();
        initPaymentDetail();
//        mBinding.paymentMethode.paymentRadioGroup.get
    }

    private void initUserInfo() {
        if (null==userInfo){
            mBinding.layoutAddress.setUserinfo(new UserInfo("Không có thông tin","",""));
            return;
        }
        mBinding.layoutAddress.setUserinfo(userInfo);
    }

    private void initPaymentDetail() {
        mBinding.paymentDetail.setProductPrice(CurrencyFormat.currencyFormat(mActivity.getProductsPrice()));
        mBinding.paymentDetail.setShippingPrice(CurrencyFormat.currencyFormat(SHIPPING_FEE));
        mBinding.paymentDetail.setTotalPrice(CurrencyFormat.currencyFormat(mActivity.getProductsPrice()+SHIPPING_FEE));
        mBinding.tvTotal.setText(CurrencyFormat.currencyFormat(mActivity.getProductsPrice()+SHIPPING_FEE));
    }

    private void initRecycleView() {
        orderItemList = mActivity.getOrderData();
        rvOrderItems.setLayoutManager(new LinearLayoutManager(mActivity));
        rvOrderItems.setAdapter(adapter);
        adapter.setOrderItemList(orderItemList);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = mActivity.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(null != data){
                            userInfo = data.getParcelableExtra("ADDRESS_INFO");
                            mBinding.layoutAddress.setUserinfo(userInfo);
                            mBinding.layoutAddress.tvDefault.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
    );

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClickEvent(int viewId) {
        switch (viewId){
            case R.id.btnChangeAddress: {
                Intent intent = new Intent(mActivity, AddAddressActivity.class);
                intent.putExtra("CURRENT_INFO",userInfo);
                activityResultLauncher.launch(intent);
                break;
            }
            case R.id.tvApply: {
                Bundle data = new Bundle();
                data.putString(CONFIRM_BUTTON, mActivity.getString(R.string.confirm));
                data.putString(CANCEL_BUTTON, mActivity.getString(R.string.cancel));
                data.putString(CONTENT, mActivity.getString(R.string.confirm_order));

                BaseDialog dialog = new BaseDialog(mActivity, data, true);
                dialog.setConfirmListener(v -> {
                    crateOrder();
                    dialog.dismiss();
                });
                dialog.setCancelListener(v -> dialog.dismiss());
                dialog.show();
                break;
            }
            case R.id.iv_back:{
                mActivity.finish();
                break;
            }
        }
    }

    private void crateOrder() {
        List<DTOOrderItemRequest> DTOOrderItemRequests = new ArrayList<>();
        for (OrderItem item : orderItemList){
            DTOOrderItemRequests.add(item.castToDTO());
        }
        CreateOrderRequest request = new CreateOrderRequest(
                userInfo.getName(),
                userInfo.getAddress(),
                userInfo.getPhone_number(),
                getPaymentMethode(),
                DTOOrderItemRequests
        );
        orderRepository.getCreateOrderData(mUser.getAccToken(),request).observe(mActivity, createOrderResponse -> {
            if(null == createOrderResponse){
                Toast.makeText(mActivity, "Không thể đặt hàng", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(mActivity, OrderDetailActivity.class);
            intent.putExtra("ORDER_ID", createOrderResponse.orderID);
            mActivity.startActivity(intent);
            deleteCartItem();
            mActivity.finish();
        });
    }

    private void deleteCartItem() {
        List<Integer> listCartID = mActivity.getCartIDList();
        if(null == listCartID) {
            Log.d("TAG", "Not delete CartItem");
            return;
        }
        for (int id : listCartID){
            CartRepository.getInstance().deleteCartItem(mUser.getAccToken(),id);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private int getPaymentMethode() {
        int selectedID = mBinding.paymentMethode.paymentRadioGroup.getCheckedRadioButtonId();
        switch (selectedID){
            case R.id.cash_methode: return 1;
            case R.id.momo_methode: return 2;
            default: return -1;
        }
    }
}
